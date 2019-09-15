package com.lot.android.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lot.android.R
import com.lot.android.api.Offer
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import com.lot.android.ui.activities.ResultsActivity
import java.lang.Exception

class ResultsAdapter(
    private var offers: List<Offer> = emptyList(),
    private val context: Context
)
    : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.results_card, parent, false))
    }

    override fun getItemCount(): Int = offers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val offer = offers[position]
        holder.image.setImageDrawable(getDrawableFromAirportShortcut(offer.inbound.segments.last().departureAirport.toLowerCase()))
        val shortcut = offer.outbound.segments.last().arrivalAirport
        holder.cityname.text = "${getFullCityname(shortcut)} ($shortcut)"
        holder.dates.text = offer.outbound.segments.first().departureDate.replace("T", " ") + " — " + offer.inbound.segments.first().departureDate.replace("T", " ")
        holder.price.text = "${offer.totalPrice.price} PLN"

        holder.cardview.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Do more with this flight")
            builder.setItems(R.array.do_more, object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    if (p1 == 0) {
                        Toast.makeText(context, "Just booked!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Just shared!", Toast.LENGTH_SHORT).show()
                    }

                    (context as ResultsActivity).finish()
                }
            })
            builder.show()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.card_image)
        val cityname: TextView = view.findViewById(R.id.city_name)
        val dates: TextView = view.findViewById(R.id.dates)
        val price: TextView = view.findViewById(R.id.price)

        val cardview: CardView = view.findViewById(R.id.cardview)
    }

    private fun getFullCityname(shortcut: String): String {
        val hashmap = mapOf("AMS" to "Amsterdam",
            "ARN" to "Stockholm",
            "BCN" to "Barcelona",
            "BEY" to "Beiru",
            "BLL" to "Billund",
            "CDG" to "Paris",
            "CFU" to "Corfu",
            "CMB" to "Sri Lanka",
            "DBV" to "Dubrovnik",
            "DEL" to "New Delhi",
            "DPS" to "Ngurah Rai",
            "GDN" to "Gdańsk",
            "GVA" to "Genève",
            "ICN" to "Incheon",
            "IST" to "Istanbul",
            "JFK" to "John F. Kennedy",
            "KGD" to "Khrabrovo",
            "KRK" to "Kraków",
            "LAX" to "Los Angeles",
            "LWO" to "Lviv",
            "MIA" to "Miami",
            "PEK " to "Pekin",
            "PRG" to "Prague",
            "SKP" to "Skopje",
            "SYD" to "Sydney",
            "VIE" to "Vienna",
            "WAW" to "Warszawa",
            "YYZ" to "Toronto")

        return hashmap[shortcut] ?: "Unknown"
    }

    private fun getDrawableFromAirportShortcut(name: String): Drawable {
        val resources = context.resources
        val resourceId = resources.getIdentifier(
            name, "drawable",
            context.packageName
        )

        return try {
            resources.getDrawable(resourceId)
        } catch (e: Exception) {
            val resourceId2 = resources.getIdentifier(
                "waw", "drawable",
                context.packageName
            )
            resources.getDrawable(resourceId2)
        }
    }
}