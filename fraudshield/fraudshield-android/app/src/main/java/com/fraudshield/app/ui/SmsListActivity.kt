package com.fraudshield.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fraudshield.app.R
import com.fraudshield.app.db.AppDatabase

class SmsListActivity : ComponentActivity() {

    private lateinit var adapter: SmsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = SmsListAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // LiveData — list auto-updates instantly when new SMS arrives
        AppDatabase.getInstance(this).smsLogDao().getAllLive().observe(this) { entries ->
            adapter.updateData(entries)
        }
    }
}