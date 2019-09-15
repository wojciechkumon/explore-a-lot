package com.lot.android.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lot.android.api.Storage
import kotlinx.android.synthetic.main.activity_dates.*
import java.util.*

class DatesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.lot.android.R.layout.activity_dates)

        editTextDate1.setOnClickListener {
            val dayOfMonth = Integer.parseInt(editTextDate1.text.toString().split("-")[2])
            val month = Integer.parseInt(editTextDate1.text.toString().split("-")[1]) - 1
            val year = Integer.parseInt(editTextDate1.text.toString().split("-")[0])
            val mDatePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    editTextDate1.setText(
                        String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay
                        )
                    )
                }, year, month, dayOfMonth
            )
            mDatePicker.show()
        }

        editTextDate2.setOnClickListener {
            val dayOfMonth = Integer.parseInt(editTextDate2.text.toString().split("-")[2])
            val month = Integer.parseInt(editTextDate2.text.toString().split("-")[1]) - 1
            val year = Integer.parseInt(editTextDate2.text.toString().split("-")[0])
            val mDatePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    editTextDate2.setText(
                        String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay
                        )
                    )
                }, year, month, dayOfMonth
            )
            mDatePicker.show()
        }

        fab.setOnClickListener {
            if (editTextDays.text.toString().isNotEmpty()) {
                Storage.start_date_1 = editTextDate1.text.toString()
                Storage.start_date_2 = editTextDate2.text.toString()
                Storage.days = editTextDays.text.toString()

                val intent = Intent(this, PassengersActivity::class.java)
                startActivity(intent)
            }
        }
    }


}
