package com.example.subscribewebpage.Fragments.Adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.subscribewebpage.databinding.ItemListContentBinding
import com.example.subscribewebpage.placeholder.PlaceholderContent

class SimpleItemRecyclerViewAdapter(
    private val values: List<PlaceholderContent.PlaceholderItem>,
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener
) :
    RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
            }
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.idText
        val contentView: TextView = binding.content
    }

}