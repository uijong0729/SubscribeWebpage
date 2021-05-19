package com.example.subscribewebpage

import android.os.Bundle
import android.widget.*
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.subscribewebpage.common.AppDateUtils
import com.example.subscribewebpage.common.BlankTokenizer
import com.example.subscribewebpage.common.Const.MINIMUM_INTERVAL
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.vm.WebInfoViewModel
import java.util.concurrent.Callable

// 하위버전 지원 : AppCompatActivity
// 하위버전 지원하지 않음 : Activity
class SwInsertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_insert)
        val viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]

        // 필드
        var tvCssQuery : MultiAutoCompleteTextView = findViewById(R.id.add_query)
        var tvTagAttr : MultiAutoCompleteTextView = findViewById(R.id.add_tag_attr)
        var btItemInsert : Button = findViewById(R.id.item_insert)
        var btItemInsertCancel : Button = findViewById(R.id.item_insert_cancel)
        val tvTitle :TextView = findViewById(R.id.add_title)
        val tvSearchKeyword :TextView = findViewById(R.id.add_keyword)
        val tvUrl :TextView = findViewById(R.id.add_url)

        // interval
        val tvInterval :TextView = findViewById(R.id.add_interval)
        var intervalString:String = tvInterval.text.toString()
        var intervalLong :Long = 0
        if (intervalString != null && intervalString != ""){
            intervalLong = intervalString.toLong()
        }
        if (intervalLong < 15) {
            intervalLong = MINIMUM_INTERVAL
        }

        val isInsert = intent.getBooleanExtra("isInsert", true)
        if (!isInsert) {
            btItemInsert.text = "更新"
            val id = intent.getIntExtra("id", 0)
            if (id > 0){
                val entity = SwThreadPool.es.submit(Callable {
                    viewModel.getWebInfo(id)
                }).get()
                if (entity != null){
                    with(entity){
                        tvCssQuery.setText(cssQuery)
                        tvTagAttr.setText(tagAttr)
                        tvTitle.text = this.title
                        tvSearchKeyword.text = this.searchKeyword
                        tvUrl.text = url
                        tvInterval.text = this.interval.toString()
                    }
                }
            }else{
                finish()
            }
        }

        // <out String> : String 을 상속하고 있는 클래스타입 (제네릭 표현에서 사용)
        val tags: Array<out String> = resources.getStringArray(R.array.css_query_array)
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags).also {adapter ->
            //textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
            tvCssQuery.setTokenizer(BlankTokenizer())
            tvCssQuery.setAdapter(adapter)
            tvTagAttr.setTokenizer(BlankTokenizer())
            tvTagAttr.setAdapter(adapter)
        }

        // 추가
        btItemInsert.setOnClickListener {
            SwThreadPool.es.submit {
                // 등록
                viewModel.insertWebInfo(
                    WebInfoEntity(
                        title = getTextFromView(R.id.add_title),
                        searchKeyword = getTextFromView(R.id.add_keyword),
                        url = getTextFromView(R.id.add_url),
                        interval = intervalLong,
                        date = AppDateUtils.getStringDate()
                    ).apply {
                        cssQuery = tvCssQuery.text.toString()
                        tagAttr = tvTagAttr.text.toString()
                    }
                )
            }

        }

        // 취소
        btItemInsertCancel.setOnClickListener {
            this@SwInsertActivity.finish()
        }

        super.onCreate(savedInstanceState)
    }

    private fun getTextFromView(@IdRes id :Int):String {
        var result :String = this.findViewById<EditText>(id)?.text.toString()
        if (result == null){
            result = ""
        }
        return result
    }


}