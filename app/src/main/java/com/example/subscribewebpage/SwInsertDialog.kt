package com.example.subscribewebpage

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.subscribewebpage.common.AppDateUtils
import com.example.subscribewebpage.common.BlankTokenizer
import com.example.subscribewebpage.common.Const.MINIMUM_INTERVAL
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.vm.WebInfoViewModel
import kotlin.concurrent.thread

class SwInsertDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view :View = inflater.inflate(R.layout.dialog_insert, null)

            // 자동완성
            var tvCssQuery : MultiAutoCompleteTextView = view.findViewById(R.id.add_query)
            var tvTagAttr : MultiAutoCompleteTextView = view.findViewById(R.id.add_tag_attr)
            // <out String> : String 을 상속하고 있는 클래스타입 (제네릭 표현에서 사용)
            val tags: Array<out String> = resources.getStringArray(R.array.css_query_array)
            ArrayAdapter<String>(inflater.context, android.R.layout.simple_list_item_1, tags).also {adapter ->
                //textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
                tvCssQuery.setTokenizer(BlankTokenizer())
                tvCssQuery.setAdapter(adapter)
                tvTagAttr.setTokenizer(BlankTokenizer())
                tvTagAttr.setAdapter(adapter)
            }

            builder.setView(view)
                .setPositiveButton(R.string.dialog_ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        val viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]
                        with (view){
                            var interval:Long = getTextFromView(this, R.id.add_interval, true).toLong()
                            if (interval < 15) {
                                interval = MINIMUM_INTERVAL
                            }

                            thread {
                                // 등록
                                viewModel.insertWebInfo(
                                    WebInfoEntity(
                                        title = getTextFromView(this, R.id.add_title),
                                        searchKeyword = getTextFromView(this, R.id.add_keyword),
                                        url = getTextFromView(this, R.id.add_url),
                                        interval = interval,
                                        date = AppDateUtils.getStringDate()
                                    ).apply {
                                        cssQuery = tvCssQuery.text.toString()
                                        tagAttr = tvTagAttr.text.toString()
                                    }
                                )
                            }
                        }
                    })
                .setNegativeButton(R.string.dialog_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getTextFromView(view : View?, @IdRes id :Int):String {
        var result :String = view?.findViewById<EditText>(id)?.text.toString()
        if (result == null){
            result = ""
        }
        return result
    }

    private fun getTextFromView(view : View?, @IdRes id :Int, isNumberFormat :Boolean):String {
        var result :String = view?.findViewById<EditText>(id)?.text.toString()
        if (isNumberFormat) {
            result = "15"
        }
        return result
    }
}