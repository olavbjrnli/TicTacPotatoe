package no.olavbjrnli.tictactoe

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.random.Random


/* Class made with help and inspiration from https://github.com/iampawan/TicTacToeGame
*
* */
class GameActivity : AppCompatActivity() {

    private lateinit var timer: Chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val playerOne: TextView = findViewById(R.id.playerOne)
        val playerTwo: TextView = findViewById(R.id.playerTwo)
        val playerWinner : TextView = findViewById(R.id.playerWinner)
        timer = findViewById(R.id.timer)
        timer.start()
        playerWinner.text = "GAME IS ON"

        val pref = getSharedPreferences("key_name", Context.MODE_PRIVATE)
        val nameOne = pref.getString("key_name_one", null)
        val nameTwo = pref.getString("key_name_two", null)

        playerOne.text = nameOne
        playerTwo.text = nameTwo
    }

    @SuppressLint("ResourceAsColor")
    fun btnClick(view: View) {
        val btnPressed: Button = view as Button
        var cellID = 0
        /*val btnList = arrayListOf<Button>(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
        for(btn in btnList){

            btnPressed.id = cellID++
        }*/
        when (btnPressed.id) {
            R.id.btn1 -> cellID = 1
            R.id.btn2 -> cellID = 2
            R.id.btn3 -> cellID = 3
            R.id.btn4 -> cellID = 4
            R.id.btn5 -> cellID = 5
            R.id.btn6 -> cellID = 6
            R.id.btn7 -> cellID = 7
            R.id.btn8 -> cellID = 8
            R.id.btn9 -> cellID = 9
        }
        if(intent.getBooleanExtra("singleplayer", false)) {
            playSingle(btnPressed, cellID)
        }
        if(intent.getBooleanExtra("multiplayer", false)){
            playGame(btnPressed, cellID)
        }
        determineWinner()
        btnPressed.isEnabled = false
    }

    private var player1 = ArrayList<Int>()
    private var player2 = ArrayList<Int>()
    private var activePlayer = 1

    private fun playGame(btnSelected: Button, cellID: Int) {

        if (activePlayer == 1) {
            btnSelected.setBackgroundResource(R.drawable.yellow_potato)
            isPlayingOne.visibility = View.INVISIBLE
            isPlayingTwo.visibility = View.VISIBLE
            player1.add(cellID)
            activePlayer = 2
        } else {
            btnSelected.setBackgroundResource(R.drawable.red_potato)
            isPlayingOne.visibility = View.VISIBLE
            isPlayingTwo.visibility = View.INVISIBLE
            player2.add(cellID)
            activePlayer = 1
        }

    }
    private fun playSingle(btnSelected: Button, cellID: Int) {
            if (activePlayer == 1) {
                btnSelected.setBackgroundResource(R.drawable.yellow_potato)
                player1.add(cellID)
                activePlayer = 2
                aiPlay()
            } else {
                btnSelected.setBackgroundResource(R.drawable.red_potato)
                player2.add(cellID)
                activePlayer = 1
            }

        }
    //Determines winner by looping through a Map containing winning combinations
    private fun determineWinner() {
        val pref = getSharedPreferences("key_name", Context.MODE_PRIVATE)
        val edit = pref.edit()
        var wins = pref.getInt("key_name_win", 0)
        var winsTwo = pref.getInt("key_name_winTwo", 0)
        var winner = -1

        val rows = HashMap<Int, ArrayList<Int>>()
        rows[1] = arrayListOf(1, 2, 3)
        rows[2] = arrayListOf(4, 5, 6)
        rows[3] = arrayListOf(7, 8, 9)
        rows[4] = arrayListOf(1, 4, 7)
        rows[5] = arrayListOf(2, 5, 8)
        rows[6] = arrayListOf(3, 6, 9)
        rows[7] = arrayListOf(1, 5, 9)
        rows[8] = arrayListOf(3, 5, 7)

        for (i in rows.entries) {
            if (player1.containsAll(i.value))
                winner = 1

            if (winner == 1)
                playerWinner.text = getString(R.string.playerWinner) + " " + playerOne.text.toString()

            if (player2.containsAll(i.value))
                winner = 2
            if (winner == 2)
                playerWinner.text = getString(R.string.playerWinner) + " " + playerTwo.text.toString()

            if (player1.size == 5 && !player1.containsAll(i.value))
                playerWinner.text = getString(R.string.draw)

        }
        //Adds win to the winner in leaderboard
        if(winner == 1){
            wins++
            edit.putInt("key_name_win", wins)
            edit.commit()
            timer.stop()
        }
        if(winner == 2){
            winsTwo++
            edit.putInt("key_name_winTwo", winsTwo)
            edit.commit()
            timer.stop()
        }
        if (winner != -1 || player1.size == 5) {

            isPlayingOne.visibility = View.INVISIBLE
            isPlayingTwo.visibility = View.INVISIBLE
            1.until(10).forEach {btnIndex ->
            val disableButton : Button = findViewById(resources.getIdentifier("btn$btnIndex", "id", packageName))
            disableButton.isEnabled = false
            }
        }
    }
    //restarts activity when button is clicked
    fun restartGame(view: View) {
        recreate()
    }
    //takes you back to main menu
    fun menuBack(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    //goes to leaderboard on button click
    fun seeLeaderboard(view: View) {
        val fragment = LeaderboardFragment()

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.game_frame, fragment).commit()
        btnLeaderboard.visibility = View.INVISIBLE
        btnRestart.visibility = View.INVISIBLE
        menuBack.visibility = View.INVISIBLE
        1.until(10).forEach {btnIndex ->
            val disableButton : Button = findViewById(resources.getIdentifier("btn$btnIndex", "id", packageName))
            disableButton.isEnabled = false
        }
    }
    
    //Not my creation at all!
    private fun aiPlay() {
        val board = ArrayList<Int>()
        for (cellID in 1..9) {
            if (!player1.contains(cellID) || player2.contains(cellID))
                board.add(cellID)

        }
        val r = Random
        val randomID = r.nextInt(board.size-0)+0
        val cellID = board[randomID]

        val btnSelected:Button
        btnSelected = when(cellID){
            1-> btn1
            2-> btn2
            3-> btn3
            4-> btn4
            5-> btn5
            6-> btn6
            7-> btn7
            8-> btn8
            9-> btn9
            else -> btn1
        }
        playGame(btnSelected, cellID)
        btnSelected.isEnabled = false
    }
}