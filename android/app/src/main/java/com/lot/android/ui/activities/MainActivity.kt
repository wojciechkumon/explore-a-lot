package com.lot.android.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lot.android.R
import com.lot.android.api.Storage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Storage.clearStorage()

        customize_trip.setOnClickListener {
            startActivity(Intent(this, CustomizeTripActivity::class.java))
        }

        feeling_lucky.setOnClickListener {
            startActivity(Intent(this, PassengersActivity::class.java))
        }
    }
}
