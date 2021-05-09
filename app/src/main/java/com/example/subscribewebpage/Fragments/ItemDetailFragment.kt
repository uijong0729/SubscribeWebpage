package com.example.subscribewebpage.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.placeholder.PlaceholderContent
import com.example.subscribewebpage.databinding.FragmentItemDetailBinding

/**
 * 리스트 중 하나를 클릭했을 때 표시되는 Fragment
 */
class ItemDetailFragment : Fragment() {

    private var item: PlaceholderContent.PlaceholderItem? = null
    lateinit var itemDetailTextView: TextView
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("[Debug]", "onCreate in ItemDetailFragment")

        arguments?.let {
            if (it.containsKey(Const.ARG_ITEM_ID)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = PlaceholderContent.ITEM_MAP[it.getString(Const.ARG_ITEM_ID)]
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        Log.d("[Debug]", "onCreateView in ItemDetailFragment")

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.toolbarLayout?.title = item?.content

        itemDetailTextView = binding.itemDetail
        // Show the placeholder content as text in a TextView.
        item?.let {
            itemDetailTextView.text = it.details
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}