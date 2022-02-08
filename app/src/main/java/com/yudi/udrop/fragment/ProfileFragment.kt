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
import com.yudi.udrop.interfaces.InputInterface
import com.yudi.udrop.model.data.ProfileModel

class ProfileFragment : Fragment(), InputInterface {
    lateinit var binding: FragmentProfileBinding
    lateinit var SQLiteManager: SQLiteManager

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
        SQLiteManager = SQLiteManager(binding.root.context, "udrop.db", null, 1)
        setupBinding()
        setupRecyclerView()
    }

    private fun setupBinding() {
        SQLiteManager.getInfo()?.let {
            binding.model =
                ProfileModel(it.name, if (it.motto == "") "点击修改个性签名" else it.motto, it.days)
        }
        // TODO: use default head icon
        Glide.with(binding.root)
            .load(R.drawable.logo)
            .into(binding.profileHeadIcon)
        binding.profileSignature.setOnClickListener {
            binding.model?.let { it.doEdit.set(true) }
        }
        binding.profileSignatureEditIcon.setOnClickListener {
            binding.model?.let { it.doEdit.set(false) }
        }
    }

    private fun setupRecyclerView() {
        val recyclerviewLayoutManager = LinearLayoutManager(context)
        recyclerviewLayoutManager.orientation = RecyclerView.VERTICAL
        with(binding.profileFeatures) {
            adapter = ProfileFeatureAdapter()
            layoutManager = recyclerviewLayoutManager
        }
    }
}