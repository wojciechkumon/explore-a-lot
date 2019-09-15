package com.lot.android.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lot.android.R
import com.lot.android.api.Offer
import com.lot.android.api.Storage
import com.lot.android.ui.adapters.ResultsAdapter
import kotlinx.android.synthetic.main.activity_results.*

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        Storage.clearStorage()

        val jsonString = intent.getStringExtra("json")
        val typeToken = object : TypeToken<List<Offer>>() {}.type
        val offers = Gson().fromJson<List<Offer>>(jsonString, typeToken)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        results.layoutManager = layoutManager
        results.adapter = ResultsAdapter(offers, this)

        results.isNestedScrollingEnabled = false
        results.onFlingListener = null

        results.postDelayed(500) {
            nested.scrollTo(0,0)

            if (offers.isEmpty()) {
                no_data.visibility = View.VISIBLE
                nested.visibility = View.GONE
            } else {
                no_data.visibility = View.GONE
                nested.visibility = View.VISIBLE
            }
        }
    }
}
