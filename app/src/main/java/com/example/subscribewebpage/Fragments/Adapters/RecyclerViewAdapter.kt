package com.example.subscribewebpage.Fragments.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.subscribewebpage.common.Const
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
        // InfoViewHolder.bind(WebInfoEntity)
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onClickListener.onItemClickListener(items[position], holder.itemView)
        }
    }

    override fun getItemCount() = items.size

    class InfoViewHolder(private val binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: WebInfoEntity){
            with (binding){
                title.text = data.title
                keyword.text = data.searchKeyword
                if (data.enable == Const.ENABLE) {
                    enableRequest.visibility == View.INVISIBLE
                }else{
                    enableRequest.visibility == View.VISIBLE
                }
                Log.d(Const.DEBUG_TAG, "enable : ${data.enable}")
            }
        }
    }

    interface RowClickListener {
        fun onItemClickListener(WebInfo: WebInfoEntity, itemView: View)
    }
}