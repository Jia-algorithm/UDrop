package com.yudi.udrop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yudi.udrop.R
import com.yudi.udrop.adapter.HomeScheduleAdapter
import com.yudi.udrop.adapter.HomeTextAdapter
import com.yudi.udrop.databinding.FragmentHomeBinding
import com.yudi.udrop.interfaces.TabLayoutInterface

class HomeFragment : Fragment(), TabLayoutInterface {
    lateinit var binding: FragmentHomeBinding

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
        setupViewpager(view)
        setupTabLayout(view)
    }

    private fun setupRecyclerView(view: View) {
        val adapter = HomeTextAdapter(view.context)
        val recyclerView = view.findViewById<RecyclerView>(R.id.home_text_learned)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        recyclerView.layoutManager = layoutManager
    }

    private fun setupViewpager(view: View) {
        val viewpager = view.findViewById<ViewPager2>(R.id.home_schedule_viewpager)
        viewpager.adapter = HomeScheduleAdapter()
    }

    private fun setupTabLayout(view: View) {
        val tabLayout = view.findViewById<TabLayout>(R.id.home_schedule_tab)
        val viewPager = view.findViewById<ViewPager2>(R.id.home_schedule_viewpager)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val view =
                LayoutInflater.from(context)
                    .inflate(R.layout.home_schedule_tab_item, null, false)
            when (position) {
                0 -> view.findViewById<TextView>(R.id.home_schedule_tab_item_title).text =
                    getString(R.string.new_to_learn)
                1 -> view.findViewById<TextView>(R.id.home_schedule_tab_item_title).text =
                    getString(R.string.to_review)
            }
            tab.customView = view
        }.attach()
        super.setupTabLayout(view.context, tabLayout, R.id.home_schedule_tab_item_title)
    }
}