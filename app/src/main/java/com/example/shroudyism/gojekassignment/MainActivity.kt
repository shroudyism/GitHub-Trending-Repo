package com.example.shroudyism.gojekassignment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_internet_banner)

       /* val firstFragment = NoInternet()
        firstFragment.arguments = intent.extras
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.fragment, firstFragment)
        transaction.commit()*/



    }
}