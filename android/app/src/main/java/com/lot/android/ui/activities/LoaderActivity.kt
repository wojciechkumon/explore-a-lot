package com.lot.android.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lot.android.R
import com.lot.android.api.ApiService
import com.lot.android.api.Storage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoaderActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-54-93-38-93.eu-central-1.compute.amazonaws.com:8080/api/lot/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        if (Storage.tags == "") {
            getLucky(apiService)
        } else {
            flights(apiService)
        }
    }

    fun flights(apiService: ApiService) {
        disposable = apiService.getFlights(
            Storage.adults.toString(),
            Storage.start_date_1,
            Storage.start_date_2,
            Storage.days,
            Storage.budget.toString(),
            Storage.tags,
            Storage.teenagers.toString(),
            Storage.children.toString(),
            Storage.infants.toString()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                val json = Gson().toJson(it)
                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra("json", json)
                startActivity(intent)
            }, { finish() })
    }

    fun getLucky(apiService: ApiService) {
        disposable = apiService.getLucky(
            Storage.adults.toString(),
            Storage.teenagers.toString(),
            Storage.children.toString(),
            Storage.infants.toString()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                val json = Gson().toJson(it)
                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra("json", json)
                startActivity(intent)
            }, { finish() })
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onBackPressed() {
        disposable?.dispose()
        super.onBackPressed()
    }
}
