package com.example.subscribewebpage

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.subscribewebpage.common.AppDateUtils
import com.example.subscribewebpage.common.BlankTokenizer
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.databinding.DialogInsertBinding
import com.example.subscribewebpage.vm.WebInfoViewModel
import io.github.rybalkinsd.kohttp.ext.httpGet
import java.net.UnknownHostException
import java.util.concurrent.Callable

// 하위버전 지원 : AppCompatActivity
// 하위버전 지원하지 않음 : Activity
class SwInsertActivity : AppCompatActivity() {

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
                } else {
                    finish()
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

            // 추가
            itemInsert.setOnClickListener {
                val url = addUrl.text.toString()
                val inspectUrl: Boolean = SwThreadPool.es.submit(Callable {
                    try {
                        url.httpGet()
                        //java.lang.IllegalArgumentException
                        //java.net.UnknownHostException
                        return@Callable true
                    } catch (e: Exception) {
                        when (e) {
                            is IllegalArgumentException -> return@Callable false
                            is UnknownHostException -> return@Callable false
                        }
                        Log.d(Const.DEBUG_TAG, e.localizedMessage)
                    }
                    return@Callable false
                }).get()

                if (inspectUrl) {
                    SwThreadPool.es.submit {
                        // 등록
                        viewModel.insertWebInfo(
                            WebInfoEntity(
                                title = addTitle.text.toString(),
                                searchKeyword = addKeyword.text.toString(),
                                url = addUrl.text.toString(),
                                interval = intervalLong,
                                date = AppDateUtils.getStringDate()
                            ).apply {
                                cssQuery = addQuery.text.toString()
                                tagAttr = addTagAttr.text.toString()
                            }
                        )
                        finish()
                    }
                }else{
                    Toast.makeText(applicationContext, "URL情報が取得できません", Toast.LENGTH_LONG).show()
                }
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