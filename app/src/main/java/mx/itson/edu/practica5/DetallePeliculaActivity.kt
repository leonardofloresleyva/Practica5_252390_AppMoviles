package mx.itson.edu.practica5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallePeliculaActivity : AppCompatActivity() {

    var callback: Intent? = null
    var seatsAvailable = 20
    var seats = 0

    var clients: java.util.ArrayList<Int?>? = ArrayList<Int?>()

    var takenSeats: java.util.ArrayList<Int?>? = ArrayList<Int?>()

    var ticketLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            var data = result.data
            if(data != null){
                var selectedSeat = data.getIntExtra("selectedSeat", -1)
                if(selectedSeat > 0){
                    clients?.add(selectedSeat)
                    takenSeats?.add(selectedSeat)
                    seatsAvailable--
                    seats++
                    showSeatsLeft()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_pelicula)

        val peliculaImage: ImageView = findViewById(R.id.image_movie_descrition)
        val nombrePelicula: TextView = findViewById(R.id.movie_title_description)
        val peliculaDesc: TextView = findViewById(R.id.movie_description)
        takenSeats = ArrayList<Int?>()

        val bundle= intent.extras
        if (bundle != null){
            peliculaImage.setImageResource(bundle.getInt("header"))
            nombrePelicula.text = bundle.getString("titulo")
            peliculaDesc.text = bundle.getString("sinopsis")
            seatsAvailable = bundle.getInt("numberSeats")
            clients = bundle.getIntegerArrayList("asientos")
            showSeatsLeft()

            callback = Intent()
            callback?.putExtra("id", bundle.getInt("id"))
            callback?.putExtra("tipo", bundle.getString("tipo"))
        }

        val buttonTickets: Button = findViewById(R.id.btn_buy_tickets)
        buttonTickets.setOnClickListener {
            if(seatsLeft()){
                val intent = Intent(this, SeatSelectionActivity::class.java)
                intent.putExtra("name", bundle?.getString("titulo"))
                intent.putExtra("asientos", clients)
                ticketLauncher.launch(intent)
            } else
                Toast.makeText(this, "All seats were sold!", Toast.LENGTH_LONG).show()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(callback != null && seats > 0){
                    callback?.putExtra("seats", takenSeats)
                    setResult(Activity.RESULT_OK, callback)
                }
                finish()
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun showSeatsLeft(): Unit{
        val seatsLeft: TextView = findViewById(R.id.seat_left)
        if(seatsLeft())
            seatsLeft.text = "$seatsAvailable seats available."
        else
            seatsLeft.text = "No seats available."
    }

    fun seatsLeft() = seatsAvailable > 0
}