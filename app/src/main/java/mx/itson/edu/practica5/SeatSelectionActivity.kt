package mx.itson.edu.practica5

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach

class SeatSelectionActivity : AppCompatActivity() {

    var seatsTaken: java.util.ArrayList<Int?>? = ArrayList<Int?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seat_selection)

        val title: TextView = findViewById(R.id.titleSeats)
        var selectedSeat = -1

        val bundle = intent.extras
        if(bundle!=null){
            title.text = bundle.getString("name")
            seatsTaken = bundle.getIntegerArrayList("asientos")
        }

        val row1: RadioGroup = findViewById(R.id.row1)
        val row2: RadioGroup = findViewById(R.id.row2)
        val row3: RadioGroup = findViewById(R.id.row3)
        val row4: RadioGroup = findViewById(R.id.row4)
        val rows = listOf(row1, row2, row3, row4)

        rows.forEach { row ->
            row.forEach { view ->
                if (view is RadioButton) {
                    val seatNumber = view.text.toString().toIntOrNull()
                    if (seatsTaken != null && seatsTaken!!.contains(seatNumber)) {
                        view.isEnabled = false
                        view.setBackgroundResource(R.drawable.radio_disabled)
                    }
                }
            }
        }

        val listener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                rows.forEach { otherGroup ->
                    if (otherGroup != group) otherGroup.clearCheck()
                }
                val radioButton = group.findViewById<RadioButton>(checkedId)
                selectedSeat = radioButton.text.toString().toInt()
            }
        }

        row1.setOnCheckedChangeListener(listener)
        row2.setOnCheckedChangeListener(listener)
        row3.setOnCheckedChangeListener(listener)
        row4.setOnCheckedChangeListener(listener)

        val confirm: Button = findViewById(R.id.confirm_button)
        confirm.setOnClickListener {
            if (selectedSeat > -1) {
                Toast.makeText(this, "Enjoy the movie :D", Toast.LENGTH_LONG).show()
                val resultIntent = Intent()
                resultIntent.putExtra("selectedSeat", selectedSeat)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "You haven't selected any seat :(", Toast.LENGTH_LONG).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.seat_selection)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}