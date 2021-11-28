package com.yudi.udrop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.databinding.ProfileLearnedTextsItemBinding
import com.yudi.udrop.model.data.TextModel

class ProfileTextAdapter(val context: Context) :
    RecyclerView.Adapter<ProfileTextAdapter.ProfileViewHolder>() {
    inner class ProfileViewHolder(val binding:ProfileLearnedTextsItemBinding) : RecyclerView.ViewHolder(binding.root)
    //创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = DataBindingUtil.inflate<ProfileLearnedTextsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.profile_learned_texts_item,
            parent,
            false
        )
        return ProfileViewHolder(binding)
    }
    //数据绑定
    override fun onBindViewHolder(itemViewHolder: ProfileViewHolder, position: Int) {
        itemViewHolder.binding.model = TextModel(
            "Ttile $position",
            "Writer $position",
            "Context Context Context Context Context Context Context Context Context Context Context Context")
        itemViewHolder.binding.executePendingBindings()
    }
    //获取item个数
    override fun getItemCount(): Int = 20//设有20个item
}