package com.lot.android.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lot.android.R
import com.lot.android.api.Spot
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting

class CardStackAdapter(
    private var spots: List<Spot> = emptyList(),
    private val cardStackView: CardStackView,
    private val layoutManager: CardStackLayoutManager
)
    : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.customize_card, parent, false))
    }

    override fun getItemCount(): Int = spots.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = spots[position]
        holder.image.setImageResource(spots[position].img)
        holder.hashtag.text = "#${spot.hashtag}"

        holder.like.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .build()

            layoutManager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        holder.dislike.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .build()

            layoutManager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.card_image)
        val hashtag: TextView = view.findViewById(R.id.item_name)

        val dislike: ImageView = view.findViewById(R.id.button_dislike)
        val like: ImageView = view.findViewById(R.id.button_like)
    }
}