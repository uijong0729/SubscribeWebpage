package com.example.subscribewebpage.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.databinding.FragmentItemDetailBinding
import com.example.subscribewebpage.vm.WebInfoViewModel

/**
 * 리스트 중 하나를 클릭했을 때 표시되는 Fragment
 */
class ItemDetailFragment : Fragment() {

    lateinit var itemDetailTextView: TextView
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    // 데이터 바인딩 관련
    private val viewModel = activity?.let { WebInfoViewModel(it.application) }
    private var item: WebInfoEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("[Debug]", "onCreate in ItemDetailFragment")
        val id: Int? = savedInstanceState?.getInt(Const.DETAIL_WEB_INFO_ID)
        if (id != null) {
            if (viewModel != null) {
                item = viewModel.getWebInfo(id)
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

        binding.toolbarLayout?.title = item?.title

        itemDetailTextView = binding.itemDetail
        // Show the placeholder content as text in a TextView.
        item?.let {
            itemDetailTextView.text = "${it.description}"
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}