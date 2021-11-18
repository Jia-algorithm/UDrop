package com.yudi.udrop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.adapter.ProfileTextAdapter
import com.yudi.udrop.databinding.FragmentProfileBinding
import com.yudi.udrop.model.data.ProfileModel

class ProfileFragment : Fragment() {
    lateinit var binding:FragmentProfileBinding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        binding.model= ProfileModel("Rebecca Dai","Fighting, and Keeping!","134","20")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
    }
    fun setupRecyclerView(view:View) {
        val adapter = context?.let { ProfileTextAdapter(it) }
        val adapter1 = context?.let { ProfileTextAdapter(it) }
        val TextRecyclerView = view.findViewById<RecyclerView>(R.id.review_item)
        val ScheduleRecyclerView = view.findViewById<RecyclerView>(R.id.schedule_item)
        TextRecyclerView.adapter = adapter
        ScheduleRecyclerView.adapter = adapter1
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation= RecyclerView.HORIZONTAL
        val layoutManager1 = LinearLayoutManager(context)
        layoutManager1.orientation= RecyclerView.HORIZONTAL
        TextRecyclerView.layoutManager = layoutManager
        ScheduleRecyclerView.layoutManager = layoutManager1
    }
}