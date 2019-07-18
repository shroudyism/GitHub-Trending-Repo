package com.example.shroudyism.trendingGitHub.ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.View
import com.example.shroudyism.trendingGitHub.dataModels.TrendingListItems
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import kotlin.collections.ArrayList
import android.app.PendingIntent
import android.app.AlarmManager
import android.arch.lifecycle.ViewModelProviders
import android.util.Log
import android.widget.Toast
import com.example.shroudyism.gojekassignment.R
import com.example.shroudyism.trendingGitHub.viewModel.TrendingListViewModel
import com.example.shroudyism.trendingGitHub.utils.AlarmReceiver
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var trendingListAdapter: TrendingListAdapter
    private var mShimmerViewContainer: ShimmerFrameLayout? = null
    private val cacheTimeLimit: Long = 7200000  //Limit Set To 2 Hours - Cache Will Be Cleared After 2 hours
    private lateinit var viewModel : TrendingListViewModel
    private var alarmMgr: AlarmManager? = null
    private var alarmIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        rv_list.layoutManager = layoutManager

        //Use of shimmer to show loading state
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container)

        viewModel = ViewModelProviders.of(this).get(TrendingListViewModel::class.java)
        viewModel.liveData.observe(this , android.arch.lifecycle.Observer<ArrayList<TrendingListItems?>> {
            if (it != null) {
                inflateList(it)
            }
        })

        startAlarm()    //Set CacheTimeLimit Alarm to clear-cache Or fetch new data after given time

        val appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(applicationContext)

        var cacheExist = isCache()

        Log.d("Cache Exist ? " , ""+cacheExist)
        var trendingItemList : ArrayList<TrendingListItems?> = ArrayList()

        if (cacheExist) {
            Log.d("DebugTesting","CacheExist")
            trendingItemList = jsonToList(appSharedPrefs.getString(R.string.trending_cache_key.toString(), ""))
            inflateList(trendingItemList)

            Toast.makeText(applicationContext,"Showing Data From Cache",Toast.LENGTH_LONG).show()
        }
        else
        {
            Log.d("DebugTesting","Cache DoesNot Exist , Refreshing Data")
            viewModel.refreshData(applicationContext)
        }

        //Pull to refresh implementation
        val pullToRefresh = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            rv_list.visibility=View.INVISIBLE
            mShimmerViewContainer?.visibility=View.VISIBLE
            mShimmerViewContainer?.startShimmerAnimation()

            viewModel.refreshData(applicationContext)
            pullToRefresh.isRefreshing = false
        }

    }

    private fun isCache(): Boolean {

         val appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(applicationContext)

        val json = appSharedPrefs.getString(R.string.trending_cache_key.toString(), "")

        if (json.equals(""))
            return false
        return true
    }


    private fun jsonToList(json: String?): ArrayList<TrendingListItems?> {
       return  Gson().fromJson(json, Array<TrendingListItems?>::class.java).toCollection(ArrayList())
    }

    private fun inflateList(heroNames: ArrayList<TrendingListItems?>) {

        trendingListAdapter = TrendingListAdapter(heroNames, applicationContext)
        rv_list.adapter = trendingListAdapter
        (rv_list.adapter as TrendingListAdapter).notifyDataSetChanged()

        mShimmerViewContainer?.stopShimmerAnimation()
        mShimmerViewContainer?.visibility = View.GONE
        rv_list.visibility=View.VISIBLE
    }

    fun startAlarm() {
        alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        alarmMgr!!.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+cacheTimeLimit,
                cacheTimeLimit, alarmIntent);
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.trending_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        mShimmerViewContainer?.startShimmerAnimation()
    }

    override fun onPause() {
        mShimmerViewContainer?.stopShimmerAnimation()
        super.onPause()
    }
}