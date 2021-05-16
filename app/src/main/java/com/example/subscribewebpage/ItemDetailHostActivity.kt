package com.example.subscribewebpage

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.subscribewebpage.common.AppDateUtils
import com.example.subscribewebpage.cron.SwWorkRequest
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.databinding.ActivityItemDetailBinding
import com.example.subscribewebpage.vm.WebInfoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * 메인 액티비티
 */
class ItemDetailHostActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: WebInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]

        // Log
        Log.d("[Debug]", "onCreate in ItemDetailHostActivity")

        val binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 데이터 초기화
        viewModel.getAllWebInfo().also {
            SwWorkRequest.run(this)
        }

        // 플로팅 버튼
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            floatingActionButton.isExpanded = true
        }

        val tvClose = findViewById<TextView>(R.id.tvClose)
        tvClose.setOnClickListener {
            floatingActionButton.isExpanded = false
        }

        val tvWebInfoInsert = findViewById<TextView>(R.id.tvWebInfoInsert)
        tvWebInfoInsert.setOnClickListener {
//            viewModel.insertWebInfo(
//                WebInfoEntity(
//                    title = "title",
//                    searchKeyword = "description",
//                    cssQuery = "keyword",
//                    url = "url",
//                    interval = 15,
//                    date = AppDateUtils.getStringDate()
//                )
//            )

            val newFragment = SwInsertDialog()
            newFragment.show(supportFragmentManager, "swinsert")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        Log.d("[Debug]", "onSupportNavigateUp in ItemDetailHostActivity")
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}