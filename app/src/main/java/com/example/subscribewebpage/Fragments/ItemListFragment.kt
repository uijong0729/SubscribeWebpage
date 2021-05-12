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
import androidx.recyclerview.widget.RecyclerView
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("[Debug]", "onViewCreated in ItemListFragment")

        // 리사이클러 뷰 (리스트)
        val recyclerViewAdapter = RecyclerViewAdapter(this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.item_list)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = recyclerViewAdapter
        
        // 뷰 모델 
        viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]
        viewModel.getAllWebInfoObservers().observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.setListDaa(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        boundList = null
    }

    override fun onItemClickListener(WebInfo: WebInfoEntity, itemView: View){
        //val itemDetailFragmentContainer: View? = view?.findViewById(R.id.item_detail_nav_container)
        //val bundle = Bundle()
        //bundle.putInt(Const.DETAIL_WEB_INFO_ID, WebInfo.id)
        val bundle = bundleOf(Const.DETAIL_WEB_INFO_ID to WebInfo.id )
        Log.d(Const.DEBUG_TAG, "webInfo id set : ${WebInfo.id}")
        //if (itemDetailFragmentContainer != null) {
        //    itemDetailFragmentContainer.findNavController().navigate(R.id.fragment_item_detail, bundle)
        //}else{
        //super.onSaveInstanceState(bundle)
            itemView.findNavController().navigate(R.id.nav_detail_action, bundle)
        //}

    }
}