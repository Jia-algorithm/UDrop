package com.yudi.udrop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.CollectionActivity
import com.yudi.udrop.R
import com.yudi.udrop.databinding.ProfileItemBinding
import com.yudi.udrop.model.data.FeatureModel

class ProfileFeatureAdapter(val activity: FragmentActivity?) :
    RecyclerView.Adapter<ProfileFeatureAdapter.ProfileViewHolder>() {
    inner class ProfileViewHolder(val binding: ProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = DataBindingUtil.inflate<ProfileItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.profile_item,
            parent,
            false
        )
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(itemViewHolder: ProfileViewHolder, position: Int) {
        with(itemViewHolder.binding) {
            model = FeatureModel(
                when (position) {
                    0 -> R.string.my_collection
                    1 -> R.string.my_errorbook
                    2 -> R.string.game_center
                    3 -> R.string.help_for_new
                    4 -> R.string.feedback
                    else -> R.string.about_us
                }
            )
            divider = position == 3
        }
        if (position == 0)
            itemViewHolder.itemView.setOnClickListener { view ->
                activity?.let {
                    it.startActivity(Intent(view.context, CollectionActivity::class.java))
                }
            }
    }

    override fun getItemCount(): Int = 6
}