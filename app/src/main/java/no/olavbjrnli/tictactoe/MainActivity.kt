package no.olavbjrnli.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

     fun playMultiplayerButton(view: View) {
         val fragment = MultiPlayerFragment()

         val transaction = supportFragmentManager.beginTransaction()

         transaction.replace(R.id.start_frame,fragment).commit()
         btnPlaySingleplayer.isEnabled = false
         btnPlayMultiplayer.isEnabled = false
     }

    fun playSingleplayerButton(view: View) {
        val fragment = SingleplayerFragment()

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.start_frame,fragment).commit()
        btnPlaySingleplayer.isEnabled = false
        btnPlayMultiplayer.isEnabled = false
    }
}
