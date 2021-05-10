package com.example.subscribewebpage.Fragments.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.databinding.ItemListContentBinding

class RecyclerViewAdapter(
    private val onClickListener: RowClickListener)
    :RecyclerView.Adapter<RecyclerViewAdapter.InfoViewHolder>() {

    var items = ArrayList<WebInfoEntity>()

    fun setListDaa(data: ArrayList<WebInfoEntity>){
        this.items = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val binding = ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onClickListener.onItemClickListener(items[position], holder.itemView)
        }
    }

    override fun getItemCount() = items.size

    class InfoViewHolder(binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val idView: TextView = binding.title
        private val contentView: TextView = binding.keyword
        fun bind(data: WebInfoEntity){
            idView.text = data.title
            contentView.text = data.keyword
        }
    }

    interface RowClickListener {
        fun onItemClickListener(WebInfo: WebInfoEntity, itemView: View)
    }
}