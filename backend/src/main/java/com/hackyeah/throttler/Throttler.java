package com.hackyeah.throttler;

import reactor.core.publisher.Mono;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * This class provides throttling for reactive programming.
 * It sets maximum concurrent executions (for example max number of parallel HTTP requests).
 * Implementation is thread safe.
 *
 * @author Wojciech Kumoń
 */
public class Throttler {
    private final BlockingDeque<ThrottlerTask<?>> pendingTasks;
    private final Semaphore semaphore;

    public Throttler(String throttlerName, int maxConcurrentExecutions, int maxWaitingTasks) {
        if (maxConcurrentExecutions <= 0) {
            throw new IllegalArgumentException(
                "maxConcurrentExecutions can't be less or equals 0: " + maxConcurrentExecutions);
        }
        if (maxWaitingTasks < 0) {
            throw new IllegalArgumentException(
                "maxConcurrentExecutions can't be less than 0: " + maxConcurrentExecutions);
        }
        this.pendingTasks = new LinkedBlockingDeque<>(maxWaitingTasks);
        this.semaphore = new Semaphore(maxConcurrentExecutions);
    }

    public <T> Mono<T> throttle(Mono<T> taskToThrottle) {
        return Mono.fromCallable(() -> {
            if (semaphore.tryAcquire()) {
                return withCleanUp(taskToThrottle);
            } else {
                return addPendingTaskToQueue(taskToThrottle);
            }
        }).flatMap(Function.identity());
    }

    private <T> Mono<T> withCleanUp(Mono<T> taskToThrottle) {
        return taskToThrottle.doFinally(x -> {
            semaphore.release();
            checkPendingTasks();
        });
    }

    private <T> Mono<T> addPendingTaskToQueue(Mono<T> taskToThrottle) {
        CompletableFuture<Mono<T>> emptyFuture = new CompletableFuture<>();
        ThrottlerTask<T> throttlerTask = new ThrottlerTask<>(emptyFuture, taskToThrottle);
        boolean success = pendingTasks.offerLast(throttlerTask);
        if (success) {
            return Mono.fromFuture(emptyFuture)
                .flatMap(Function.identity());
        } else {
            return Mono.error(new IllegalStateException("Throttler queue full"));
        }
    }

    private void checkPendingTasks() {
        ThrottlerTask<?> nextTask = pendingTasks.pollFirst();
        if (nextTask != null) {
            if (semaphore.tryAcquire()) {
                completePendingTask(nextTask);
            } else {
                returnBackToQueue(nextTask);
            }
        }
    }

    private <T> void completePendingTask(ThrottlerTask<T> nextTask) {
        nextTask.getFutureToFill().complete(withCleanUp(nextTask.getMonoToThrottle()));
    }

    private void returnBackToQueue(ThrottlerTask nextTask) {
        boolean success = pendingTasks.offerFirst(nextTask);
        if (!success) {
            nextTask.getFutureToFill()
                .completeExceptionally(new IllegalStateException("Throttler queue full, can't return back"));
        }
    }

    private static class ThrottlerTask<T> {
        private final CompletableFuture<Mono<T>> futureToFill;
        private final Mono<T> monoToThrottle;

        ThrottlerTask(CompletableFuture<Mono<T>> futureToFill, Mono<T> monoToThrottle) {
            this.futureToFill = futureToFill;
            this.monoToThrottle = monoToThrottle;
        }

        CompletableFuture<Mono<T>> getFutureToFill() {
            return futureToFill;
        }

        Mono<T> getMonoToThrottle() {
            return monoToThrottle;
        }
    }
}
