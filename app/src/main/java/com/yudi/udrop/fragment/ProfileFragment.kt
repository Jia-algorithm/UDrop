package com.yudi.udrop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yudi.udrop.R
import com.yudi.udrop.adapter.ProfileFeatureAdapter
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.databinding.FragmentProfileBinding
import com.yudi.udrop.model.data.ProfileModel

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val SQLiteManager = SQLiteManager(binding.root.context, "udrop.db", null, 1)
        SQLiteManager.getInfo()?.let {
            binding.model = ProfileModel(it.name, it.motto, it.days)
        }
        // TODO: get real data
        Glide.with(view)
            .load(R.drawable.logo)// Test mock data
            .into(binding.profileHeadIcon)
        val recyclerviewLayoutManager = LinearLayoutManager(context)
        recyclerviewLayoutManager.orientation = RecyclerView.VERTICAL
        with(binding.profileFeatures) {
            adapter = ProfileFeatureAdapter()
            layoutManager = recyclerviewLayoutManager
        }
    }
}