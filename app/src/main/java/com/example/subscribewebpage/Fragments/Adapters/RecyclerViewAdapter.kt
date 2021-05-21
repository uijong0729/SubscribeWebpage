package com.example.subscribewebpage.Fragments.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.databinding.ItemListContentBinding

class RecyclerViewAdapter(
    private val onClickListener: RowClickListener)
    :RecyclerView.Adapter<RecyclerViewAdapter.InfoViewHolder>() {

    private var items = ArrayList<WebInfoEntity>()

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

    class InfoViewHolder(private val binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: WebInfoEntity){
            binding.title.text = data.title
            binding.keyword.text = data.searchKeyword
        }
    }

    interface RowClickListener {
        fun onItemClickListener(WebInfo: WebInfoEntity, itemView: View)
    }
}