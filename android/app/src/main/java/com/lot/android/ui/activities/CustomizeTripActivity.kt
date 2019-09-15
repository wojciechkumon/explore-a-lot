package com.lot.android.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lot.android.R
import com.lot.android.api.Spot
import com.lot.android.api.Storage
import com.lot.android.ui.adapters.CardStackAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.android.synthetic.main.activity_customize_trip.*

class CustomizeTripActivity : AppCompatActivity(), CardStackListener {

    private val manager by lazy { CardStackLayoutManager(this, this) }
    private var selectedHashtags = mutableListOf<String>()
    private val spots = createSpots()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_trip)

        card_stack_view.layoutManager = manager
        card_stack_view.adapter = CardStackAdapter(spots, card_stack_view, manager)
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction == Direction.Right) {
            saveChoosenCard()
        }

        if (isCardsStackEmpty()) {
            val intent = Intent(this, DatesActivity::class.java)
            Storage.tags = selectedHashtags.joinToString(separator = ",")
            startActivity(intent)
        }
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }

    private fun isCardsStackEmpty(): Boolean = manager.topPosition == spots.size

    private fun saveChoosenCard() {
        val hashtag = spots[manager.topPosition - 1].hashtag
        selectedHashtags.add(hashtag)
    }

    private fun createSpots(): List<Spot> {
        val spots = ArrayList<Spot>()
        spots.add(Spot("cold", R.drawable.cold))
        spots.add(Spot("hot", R.drawable.hot))
        spots.add(Spot("sightseeing", R.drawable.sightseeing))
        spots.add(Spot("adventure", R.drawable.adventure))
        spots.add(Spot("ancient", R.drawable.ancient))
        spots.add(Spot("modern", R.drawable.wp3544714))
        spots.add(Spot("city", R.drawable.wp2025118))
        spots.add(Spot("countryside", R.drawable.countryside))
        spots.add(Spot("lazy", R.drawable.lazy))
        spots.add(Spot("active", R.drawable.active))
        spots.add(Spot("lake", R.drawable.lake))
        spots.add(Spot("mountain", R.drawable.gory))
        spots.add(Spot("beach", R.drawable.beach))
        spots.add(Spot("forest", R.drawable.forest))
        spots.add(Spot("jungle", R.drawable.amazon))
        spots.add(Spot("oriental", R.drawable.oriental))
        spots.add(Spot("crowd", R.drawable.crowd))
        spots.add(Spot("loneliness", R.drawable.loneliness))
        spots.add(Spot("amusement", R.drawable.amusement))
        spots.add(Spot("contemplation", R.drawable.contemplation))
        return spots
    }
}
