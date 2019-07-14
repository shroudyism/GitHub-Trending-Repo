package com.example.shroudyism.gojekassignment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import kotlinx.android.synthetic.main.no_internet_banner.*


class NoNetwork : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_internet_banner)

        btn_retry.setOnClickListener {

            if(isNetworkAvailable()) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
