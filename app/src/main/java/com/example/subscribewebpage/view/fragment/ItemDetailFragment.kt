package com.example.subscribewebpage.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.subscribewebpage.R
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.databinding.FragmentItemDetailBinding
import com.example.subscribewebpage.model.data.WebInfoEntity
import com.example.subscribewebpage.view.activity.SwInsertActivity
import com.example.subscribewebpage.viewmodel.WebInfoViewModel

/**
 * 리스트 중 하나를 클릭했을 때 표시되는 Fragment
 */
class ItemDetailFragment : Fragment() {

    private lateinit var viewModel: WebInfoViewModel
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    // 데이터 바인딩 관련
    private var webInfo: WebInfoEntity? = null

    // onCreateView -> onViewCreated
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)

        // view Model Access
        viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]
        val id: Int? = arguments?.getInt(Const.DETAIL_WEB_INFO_ID)
        Log.d("[Debug]", "view model id : $id")
        val webInfoObserver = Observer<WebInfoEntity> { obj ->
            with(binding){
                // View bind
                toolbarLayout.title = obj!!.title
                if (obj.enable == Const.ENABLE) {
                    itemDetail.text = obj.searchKeyword
                }else{
                    itemDetail.text = Const.UPDATE_ERROR
                }
                // url 표시
                itemDetailUrl.text = obj.url
            }
        }
        // 갱신
        if (id != null) {
            webInfo = viewModel.getWebInfo(id)
        }
        WebInfoViewModel.currentWebInfo.observe(this.viewLifecycleOwner, webInfoObserver)

        if (webInfo != null) {
            with (binding){
//                // View bind
//                toolbarLayout.title = webInfo!!.title
//                if (webInfo!!.enable == Const.ENABLE) {
//                    itemDetail.text = webInfo?.searchKeyword
//                }else{
//                    itemDetail.text = Const.UPDATE_ERROR
//                }
//                // url 표시
//                itemDetailUrl.text = webInfo!!.url

                // 삭제 버튼
                itemDelete.setOnClickListener {
                    // 삭제 실행의 확인
                    AlertDialog.Builder(this@ItemDetailFragment.context)
                        .setTitle("削除の確認")
                        .setMessage(R.string.dialog_question)
                        .setPositiveButton(R.string.dialog_yes) { _, _ ->
                            // 삭제 수행
                            viewModel.deleteWebInfo(webInfo!!)
                            binding.itemDelete.findNavController().navigate(R.id.item_list_fragment)
                            Toast.makeText(this@ItemDetailFragment.context, Const.SUCCESS_DB_DELETE, Toast.LENGTH_LONG).show()
                        }
                        .setNegativeButton(R.string.dialog_no) { _, _ ->
                            Toast.makeText(this@ItemDetailFragment.context, "取り消しました", Toast.LENGTH_LONG).show()
                        }
                        .show()
                }

                // 편집 버튼
                itemUpdate.setOnClickListener {
                    Toast.makeText(this@ItemDetailFragment.context, "UPDATE", Toast.LENGTH_LONG).show()
                    val updateForIntent = Intent(this@ItemDetailFragment.context, SwInsertActivity::class.java)
                    updateForIntent.putExtra("isInsert", false)
                    updateForIntent.putExtra("id", id)
                    startActivity(updateForIntent)
                }
            }
        }else {
            Toast.makeText(this.context, Const.ERR_DB_DELETE, Toast.LENGTH_LONG)
        }

        return binding.root
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