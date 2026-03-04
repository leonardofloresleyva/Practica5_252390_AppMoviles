package mx.itson.edu.practica5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SeatSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seat_selection)

        val title: TextView = findViewById(R.id.titleSeats)
        var selectedSeat = -1

        val bundle = intent.extras
        if(bundle!=null){
            title.text = bundle.getString("name")
        }

        val row1: RadioGroup = findViewById(R.id.row1)
        val row2: RadioGroup = findViewById(R.id.row2)
        val row3: RadioGroup = findViewById(R.id.row3)
        val row4: RadioGroup = findViewById(R.id.row4)

        row1.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId > -1){
                row2.clearCheck()
                row3.clearCheck()
                row4.clearCheck()

                row1.check(checkedId)
                selectedSeat = checkedId
            }
        }

        row2.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId > -1){
                row1.clearCheck()
                row3.clearCheck()
                row4.clearCheck()

                row2.check(checkedId)
                selectedSeat = checkedId + 5
            }
        }

        row3.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId > -1){
                row2.clearCheck()
                row1.clearCheck()
                row4.clearCheck()

                row3.check(checkedId)
                selectedSeat = checkedId + 10
            }
        }

        row4.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId > -1){
                row2.clearCheck()
                row3.clearCheck()
                row1.clearCheck()

                row4.check(checkedId)
                selectedSeat = checkedId + 15
            }
        }

        val confirm: Button = findViewById(R.id.confirm_button)
        confirm.setOnClickListener {
              if(selectedSeat > -1){
                  Toast.makeText(this, "Enjoy the movie :D", Toast.LENGTH_LONG).show()
                  setResult(Activity.RESULT_OK, Intent().putExtra("selectedSeat", selectedSeat))
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