package com.example.subscribewebpage.Fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.subscribewebpage.Fragments.Adapters.SimpleItemRecyclerViewAdapter
import com.example.subscribewebpage.R
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.placeholder.PlaceholderContent;
import com.example.subscribewebpage.databinding.FragmentItemListBinding
import com.example.subscribewebpage.databinding.ItemListContentBinding

class ItemListFragment : Fragment() {

    private var binded_list: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = binded_list!!

    // onCreateView -> onViewCreated
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binded_list = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding.itemList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as PlaceholderContent.PlaceholderItem
            val bundle = Bundle()
            bundle.putString(
                    Const.ARG_ITEM_ID,
                    item.id
            )
            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.fragment_item_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        /**
         * Context click listener to handle Right click events
         * from mice and trackpad input to provide a more native
         * experience on larger screen devices
         */
        val onContextClickListener = View.OnContextClickListener { v ->
            val item = v.tag as PlaceholderContent.PlaceholderItem
            Toast.makeText(
                    v.context,
                    "Context click of item " + item.id,
                    Toast.LENGTH_LONG
            ).show()
            true
        }

        recyclerView.adapter = SimpleItemRecyclerViewAdapter(
            PlaceholderContent.ITEMS,
            onClickListener,
            onContextClickListener
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binded_list = null
    }
}