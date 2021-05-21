package com.example.subscribewebpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.subscribewebpage.cron.SwWorkRequest
import com.example.subscribewebpage.databinding.ActivityItemDetailBinding
import com.example.subscribewebpage.vm.WebInfoViewModel


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

        // viewBinding
        // https://tourspace.tistory.com/314
        val binding = ActivityItemDetailBinding.inflate(layoutInflater)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 데이터 초기화
        viewModel.getAllWebInfo().also {
            SwWorkRequest.updateData(this)
        }

        // view binding 적용
        with (binding){
            // 플로팅 버튼
            floatingActionButton.setOnClickListener {
                floatingActionButton.isExpanded = true
            }

            tvClose.setOnClickListener {
                floatingActionButton.isExpanded = false
            }

            tvWebInfoInsert.setOnClickListener {
//            // 자동완성을 위한 리스트 작성
//            SwThreadPool.es.submit {
//                Transaction.getInstance(applicationContext)?.autoFillDao()?.getAll()
//            }
                val insertIntent = Intent(this@ItemDetailHostActivity, SwInsertActivity::class.java)
                insertIntent.putExtra("isInsert", true)
                startActivity(insertIntent)
            }
        }

        // viewBinding 사용시 bind.root를 인수로 넘기는 것은 필수
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        Log.d("[Debug]", "onSupportNavigateUp in ItemDetailHostActivity")
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}