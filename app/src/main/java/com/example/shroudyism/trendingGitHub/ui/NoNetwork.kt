package com.example.shroudyism.trendingGitHub.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.Toast
import com.example.shroudyism.gojekassignment.R
import com.example.shroudyism.trendingGitHub.utils.NetworkUtils


class NoNetwork : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_internet_banner)
    }

    fun sendMessage(view: View) {

        if(!NetworkUtils(applicationContext).isNetworkAvailable()) {
         Toast.makeText(applicationContext,"No Internet",Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }


}
