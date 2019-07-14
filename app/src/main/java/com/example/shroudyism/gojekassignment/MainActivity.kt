package com.example.shroudyism.gojekassignment

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.ConnectivityManager
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_trending_list.rv_list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var trendingListAdapter: TrendingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isNetworkAvailable()){
            Log.d("TAG Me Fuck" , "1")
            setContentView(R.layout.activity_main)
            Log.d("TAG Me Fuck" , "2")

            refreshData()

        }else {
            setContentView(R.layout.no_internet_banner)
            var btn = findViewById<Button>(R.id.btn_retry)
            btn.setOnClickListener {
                onRestart()
            }
        }

            val pullToRefresh = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)

            pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                refreshData()
                pullToRefresh.setRefreshing(false)
            })

    }

    private fun refreshData(){

        val retrofit = Retrofit.Builder()
                .baseUrl(Api.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create(Api::class.java)

        val call = api.heroes
        Log.d("TAG Me Fuck" , "3")

        val layoutManager = LinearLayoutManager(this)
        rv_list.layoutManager=layoutManager

        call.enqueue(object : Callback<List<ListItems>> {
            override fun onResponse(call: Call<List<ListItems>>, response: Response<List<ListItems>>) {

                val itemList = response.body()

                val heroNames = ArrayList<ListItems?>(itemList)

                if(heroNames==null)
                    Log.d("TAG Me Fuck null" , "4")
                else {
                    Log.d("TAG Me Fuck", heroNames.size.toString())
                    if(heroNames[0]==null)
                        Log.d("TAG Me Fuck,AuthorBC", "is null")
                    else
                        Log.d("TAG me Fuck,AuthorBC", heroNames[0]?.author);
                }

                progressbar.visibility= View.GONE
                trendingListAdapter=TrendingListAdapter(heroNames,this@MainActivity)

                rv_list.adapter=trendingListAdapter
                (rv_list.adapter as TrendingListAdapter).notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<ListItems>>, t: Throwable) {

            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}