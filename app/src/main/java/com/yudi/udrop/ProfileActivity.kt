package com.yudi.udrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.adapter.ProfileAdapter
import com.yudi.udrop.databinding.ActivityProfileBinding
import com.yudi.udrop.model.data.ProfileModel

class ProfileActivity : AppCompatActivity() {
    lateinit var binding:ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile)
        binding.model= ProfileModel("Rebecca Dai","Fighting, and Keeping!","134","20")
        setupRecyclerView()
    }
    fun setupRecyclerView() {
        val adapter = ProfileAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.review_item)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation= RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
    }
}