package com.example.subscribewebpage.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.databinding.FragmentItemDetailBinding
import com.example.subscribewebpage.vm.WebInfoViewModel

/**
 * 리스트 중 하나를 클릭했을 때 표시되는 Fragment
 */
class ItemDetailFragment : Fragment() {

    private lateinit var itemDetailTextView: TextView
    private lateinit var viewModel: WebInfoViewModel
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    // 데이터 바인딩 관련
    private var item: WebInfoEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // onCreateView -> onViewCreated
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        Log.d("[Debug]", "onCreateView in ItemDetailFragment")
        // view Model
        viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]
        Log.d("[Debug]", "onCreate in ItemDetailFragment")
        val id: Int? = arguments?.getInt(Const.DETAIL_WEB_INFO_ID) //savedInstanceState?.getInt(Const.DETAIL_WEB_INFO_ID)

        Log.d("[Debug]", "view model id : $id")
        if (id != null) {
            if (viewModel != null) {
                item = viewModel.getWebInfo(id)
            }
        }

        //view bind
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root
        binding.toolbarLayout?.title = item?.title
        itemDetailTextView = binding.itemDetail
        item?.let {
            itemDetailTextView.text = "${it.description}"
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id3: Int? = savedInstanceState?.getInt(Const.DETAIL_WEB_INFO_ID)
        Log.d("[Debug]", "view model id : $id3")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}