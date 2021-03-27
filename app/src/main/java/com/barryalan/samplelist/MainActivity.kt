package com.barryalan.samplelist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.barryalan.samplelist.model.ListItem
import com.barryalan.samplelist.repository.Repository


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var listAdapter: ListAdapter
    private lateinit var rvList: RecyclerView
    private lateinit var lytSwipeRefresh: SwipeRefreshLayout
    private lateinit var tvInstructions: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvList = findViewById(R.id.rv_list)
        lytSwipeRefresh = findViewById(R.id.lyt_swipe_refresh)
        tvInstructions = findViewById(R.id.tv_instructions)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        initListRecycler()
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        lytSwipeRefresh.setOnRefreshListener {
            tryToRetrieveData()
        }

        viewModel.myResponse.observe(this, { response ->
            if (response.isSuccessful) {
                tvInstructions.visibility = GONE
                listAdapter.updateList(response.body() as MutableList<ListItem>)
            } else {
                tvInstructions.visibility = VISIBLE
                Toast.makeText(this, response.code().toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initListRecycler() {
        listAdapter = ListAdapter(mutableListOf())

        val linearLayoutManager = LinearLayoutManager(this)

        rvList.layoutManager = linearLayoutManager
        rvList.setHasFixedSize(true)
        rvList.adapter = listAdapter
    }

    private fun tryToRetrieveData() {
        if (isNetworkConnected(this)) {

            viewModel.getListItem()
        } else {
            AlertDialog.Builder(this).setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
        lytSwipeRefresh.isRefreshing = false
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ))
        } else {
            val activeNetwork = cm.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnected
        }
    }
}