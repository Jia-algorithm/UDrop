package com.yudi.udrop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.adapter.HomeAdapter
import com.yudi.udrop.databinding.FragmentHomeBinding

class HomeFragment:Fragment() {
    lateinit var binding:FragmentHomeBinding
    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        //setupToolBar()
    }
    fun setupRecyclerView(view: View) {
        val adapter = context?.let { HomeAdapter(it) }
        val adapter1 = context?.let { HomeAdapter(it) }
        val ScheduleRecyclerView = view.findViewById<RecyclerView>(R.id.home_current_schedule)
        val ReviewRecyclerView = view.findViewById<RecyclerView>(R.id.home_review)
        ReviewRecyclerView.adapter = adapter
        ScheduleRecyclerView.adapter = adapter1
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation= RecyclerView.HORIZONTAL
        val layoutManager1 = LinearLayoutManager(context)
        layoutManager1.orientation= RecyclerView.HORIZONTAL
        ReviewRecyclerView.layoutManager = layoutManager
        ScheduleRecyclerView.layoutManager = layoutManager1
    }
//    private fun setupToolBar() {
//        var firstName = "Rebecca"
//        view?.findViewById<Toolbar>(R.id.home_toolbar)?.title = "Good Day, $firstName"
//    }
}