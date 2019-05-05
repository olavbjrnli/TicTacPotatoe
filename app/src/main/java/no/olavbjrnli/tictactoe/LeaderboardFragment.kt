package no.olavbjrnli.tictactoe

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

//Created using shared preferences from http://www.kotlincodes.com/kotlin/shared-preferences-with-kotlin/

class LeaderboardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        val pref = activity!!.getSharedPreferences("key_name",  0)

        val winnerList: ListView = view.findViewById(R.id.statsView)
        val list = arrayListOf(pref.getString("key_name_one", null) + " : " ,  pref.getInt("key_name_win", 0).toString(),
            pref.getString("key_name_two", null) + " : " , pref.getInt("key_name_winTwo", 0).toString())

        val adapter : ArrayAdapter<String> = ArrayAdapter(context, R.layout.listrow, R.id.textView2, list)
        winnerList.adapter = adapter
        val btnBack: Button = view.findViewById(R.id.btnBack) as Button
        btnBack.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}