package com.example.subscribewebpage.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subscribewebpage.Fragments.Adapters.RecyclerViewAdapter
import com.example.subscribewebpage.R
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.databinding.FragmentItemListBinding
import com.example.subscribewebpage.vm.WebInfoViewModel

/**
*   리스트를 표시하는 프래그먼트
 *   https://www.youtube.com/watch?v=ARpn-1FPNE4&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118
**/
class ItemListFragment : Fragment(), RecyclerViewAdapter.RowClickListener {

    private lateinit var viewModel: WebInfoViewModel
    private var boundList: FragmentItemListBinding? = null
    private val binding get() = boundList!!

    // onCreateView -> onViewCreated
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        Log.d("[Debug]", "onCreateView in ItemListFragment")
        boundList = FragmentItemListBinding.inflate(inflater, container, false)

        // 리사이클러 뷰 (리스트)
        val recyclerViewAdapter = RecyclerViewAdapter(this)
        binding.itemList.layoutManager = LinearLayoutManager(this.context)
        binding.itemList.adapter = recyclerViewAdapter

        // 뷰 모델
        val viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]

        ViewModelProvider(this)[WebInfoViewModel::class.java]
            .getAllWebInfoObservers()
            .observe(viewLifecycleOwner, Observer {
                recyclerViewAdapter.setListDaa(ArrayList(it))
                recyclerViewAdapter.notifyDataSetChanged()
            })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        boundList = null
    }

    override fun onItemClickListener(WebInfo: WebInfoEntity, itemView: View) {
        // Pair : 2개의 값이 하나의 짝인 데이터 타입
        // to : Pair 타입 데이터인 경우 A to B 구문으로 작성 가능
        val bundle = bundleOf(Const.DETAIL_WEB_INFO_ID to WebInfo.id )
        Log.d(Const.DEBUG_TAG, "webInfo id set : ${WebInfo.id}")
        itemView.findNavController().navigate(R.id.nav_detail_action, bundle)
    }
}