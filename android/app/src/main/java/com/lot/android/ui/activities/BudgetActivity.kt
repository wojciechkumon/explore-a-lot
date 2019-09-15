package com.lot.android.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lot.android.R
import com.lot.android.api.Storage
import kotlinx.android.synthetic.main.activity_budget.*

class BudgetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        budget1.setOnClickListener {
            sendDataToLoader(500)
        }

        budget2.setOnClickListener {
            sendDataToLoader(1500)
        }

        budget3.setOnClickListener {
            sendDataToLoader(3000)
        }

        budget4.setOnClickListener {
            sendDataToLoader(6000)
        }
    }

    private fun sendDataToLoader(budget: Int) {
        Storage.budget = budget

        val intent = Intent(this, LoaderActivity::class.java)
        intent.putExtras(getIntent())
        startActivity(intent)
    }
}
