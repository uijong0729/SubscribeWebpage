package com.example.subscribewebpage.view.activity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.subscribewebpage.R
import com.example.subscribewebpage.SwThreadPool
import com.example.subscribewebpage.common.AppUtils
import com.example.subscribewebpage.common.BlankTokenizer
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.databinding.DialogInsertBinding
import com.example.subscribewebpage.model.data.WebInfoEntity
import com.example.subscribewebpage.viewmodel.WebInfoViewModel
import java.net.UnknownHostException
import java.util.concurrent.Callable

// 하위버전 지원 : AppCompatActivity
// 하위버전 지원하지 않음 : Activity
class SwInsertActivity : AppCompatActivity() {

    // 데이터 갱신
    // https://patents.google.com/patent/KR101310954B1/
    // RSS는 일부 사이트에서만 이용
    // 댓글/메일 알림등도 해당 사이트에서만 국한됨
    override fun onCreate(savedInstanceState: Bundle?) {
        // 데이터 바인딩
        val binding = DialogInsertBinding.inflate(layoutInflater)
        val viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]

        // 필드
        with(binding) {

            var intervalString: String = addInterval.text.toString()
            var intervalLong: Long = 0
            if (intervalString != null && intervalString != "") {
                intervalLong = intervalString.toLong()
            }
            if (intervalLong < 15) {
                intervalLong = Const.MINIMUM_INTERVAL
            }

            val isInsert = intent.getBooleanExtra("isInsert", true)
            if (!isInsert) {
                itemInsert.text = "更新"
                val id = intent.getIntExtra("id", 0)
                if (id > 0) {
                    val entity = SwThreadPool.es.submit(Callable {
                        viewModel.getWebInfo(id)
                    }).get()
                    if (entity != null) {
                        with(entity) {
                            addQuery.setText(cssQuery)
                            addTagAttr.setText(tagAttr)
                            addTitle.setText(entity.title)
                            addKeyword.setText(entity.searchKeyword)
                            addUrl.setText(url)
                            addInterval.setText(entity.interval.toString())
                        }
                    }
                }
            }

            // 갱신 및 추가
            itemInsert.setOnClickListener {
                val url = addUrl.text.toString()
                val inspectUrl: Boolean = SwThreadPool.es.submit(Callable {
                    try {
                        //
                        //java.lang.IllegalArgumentException
                        //java.net.UnknownHostException
                        return@Callable true
                    } catch (e: Exception) {
                        when (e) {
                            is IllegalArgumentException -> return@Callable false
                            is UnknownHostException -> return@Callable false
                        }
                        Log.d(Const.DEBUG_TAG, e.localizedMessage)
                        return@Callable true
                    }
                }).get()

                Log.d(Const.DEBUG_TAG, "inspectUrl : $inspectUrl")

                if (inspectUrl) {
                    val insertId = intent.getIntExtra("id", 0)
                    val isInsert = intent.getBooleanExtra("isInsert", true)
                    SwThreadPool.es.submit {
                        if (isInsert) {
                            // 등록
                            viewModel.insertWebInfo(
                                WebInfoEntity(
                                    title = addTitle.text.toString(),
                                    searchKeyword = addKeyword.text.toString(),
                                    url = addUrl.text.toString(),
                                    interval = intervalLong,
                                    date = AppUtils.getStringDate()
                                ).apply {
                                    cssQuery = addQuery.text.toString()
                                    tagAttr = addTagAttr.text.toString()
                                }
                            )
                        }else{
                            val entity = WebInfoEntity(
                                title = addTitle.text.toString(),
                                searchKeyword = addKeyword.text.toString(),
                                url = addUrl.text.toString(),
                                interval = intervalLong,
                                date = AppUtils.getStringDate()
                            )
                            with(entity){
                                cssQuery = addQuery.text.toString()
                                tagAttr = addTagAttr.text.toString()
                                id = insertId
                                enable = Const.ENABLE
                            }
                            // 갱신
                            viewModel.updateWebInfo(entity)
                        }
                    }
                    finish()
                }else{
                    Toast.makeText(applicationContext, "URL情報が取得できません", Toast.LENGTH_LONG).show()
                }
            }

            // <out String> : String 을 상속하고 있는 클래스타입 (제네릭 표현에서 사용)
            val tags: Array<out String> = resources.getStringArray(R.array.css_query_array)
            ArrayAdapter<String>(
                this@SwInsertActivity,
                android.R.layout.simple_list_item_1,
                tags
            ).also { adapter ->
                //textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
                addQuery.setTokenizer(BlankTokenizer())
                addQuery.setAdapter(adapter)
                addTagAttr.setTokenizer(BlankTokenizer())
                addTagAttr.setAdapter(adapter)
            }

            // 취소
            itemInsertCancel.setOnClickListener {
                AlertDialog.Builder(this@SwInsertActivity)
                    .setTitle("取り消しの確認")
                    .setMessage(R.string.dialog_question_no)
                    .setPositiveButton(R.string.dialog_yes) { dialogInterface, i ->
                        this@SwInsertActivity.finish()
                    }
                    .setNegativeButton(R.string.dialog_no) { dialogInterface, i ->

                    }
                    .show()
            }
        }

        // 데이터 바인딩
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
    }
}