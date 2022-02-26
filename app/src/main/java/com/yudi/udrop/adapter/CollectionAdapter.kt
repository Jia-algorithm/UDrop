package com.yudi.udrop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yudi.udrop.R
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.CollectionTextItemBinding
import com.yudi.udrop.databinding.NoDataItemBinding
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.model.local.TextDetail

class CollectionAdapter(
    private val localManager: SQLiteManager,
    private val handler: ProgressInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val binding: CollectionTextItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DummyHolder(val binding: NoDataItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var collectionList: ArrayList<TextDetail> = arrayListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = DataBindingUtil.inflate<NoDataItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.no_data_item,
                    parent,
                    false
                )
                DummyHolder(binding)
            }
            1 -> {
                val binding = DataBindingUtil.inflate<CollectionTextItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.collection_text_item,
                    parent,
                    false
                )
                ViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.binding.model = collectionList[position]
                holder.binding.handler = handler
                holder.binding.removeFromCollection.setOnClickListener {
                    localManager.getInfo()?.let {
                        ServiceManager().removeCollection(
                            it.id,
                            collectionList[position].title
                        ) { success ->
                            if (success) {
                                collectionList.removeAt(position)
                                notifyItemRemoved(position)
                                notifyDataSetChanged()
                            } else
                                Snackbar.make(
                                    holder.itemView,
                                    R.string.warning,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                        }
                    }
                }
            }
            is DummyHolder -> {
                holder.binding.tip = R.string.no_collection
                holder.binding.icon = R.drawable.ic_no_collection
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (collectionList.isNullOrEmpty()) 0 else 1
    }

    override fun getItemCount(): Int =
        if (collectionList.isNullOrEmpty()) 1 else collectionList.size
}