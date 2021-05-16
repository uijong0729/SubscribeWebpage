package com.example.subscribewebpage.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.subscribewebpage.R
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.databinding.FragmentItemDetailBinding
import com.example.subscribewebpage.vm.WebInfoViewModel

/**
 * 리스트 중 하나를 클릭했을 때 표시되는 Fragment
 */
class ItemDetailFragment : Fragment() {

    private lateinit var itemDetailTextView: TextView
    private lateinit var itemDeleteButton: Button
    private lateinit var viewModel: WebInfoViewModel
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    // 데이터 바인딩 관련
    private var item: WebInfoEntity? = null

    // onCreateView -> onViewCreated
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // view Model Access
        viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]
        val id: Int? = arguments?.getInt(Const.DETAIL_WEB_INFO_ID)
        Log.d("[Debug]", "view model id : $id")
        if (id != null) {
            if (viewModel != null) {
                item = viewModel.getWebInfo(id)
            }
        }

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        if (item != null) {
            // View bind
            binding.toolbarLayout?.title = item!!.title
            itemDetailTextView = binding.itemDetail.apply {
                this.text = item?.description
            }
            itemDeleteButton = binding.itemDelete.apply {
                this.setOnClickListener {
                    viewModel.deleteWebInfo(item!!)
                    findNavController().navigate(R.id.item_list_fragment)
                    Toast.makeText(this.context, Const.SUCCESS_DB_DELETE, Toast.LENGTH_LONG)
                }
            }
        }else {
            Toast.makeText(this.context, Const.ERR_DB_DELETE, Toast.LENGTH_LONG)
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