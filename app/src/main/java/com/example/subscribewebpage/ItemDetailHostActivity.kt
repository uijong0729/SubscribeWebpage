package com.example.subscribewebpage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.databinding.ActivityItemDetailBinding
import com.example.subscribewebpage.vm.WebInfoViewModel

/**
 * 메인 액티비티
 */
class ItemDetailHostActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel : WebInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WebInfoViewModel::class.java]
        //vm = WebInfoViewModel(this.application)
        viewModel.insertWebInfo(WebInfoEntity(
            title = "title",
            description = "description",
            keyword = "keyword",
            url = "url",
            interval = 15
        ))

        // Log
        Log.d("[Debug]", "onCreate in ItemDetailHostActivity")

        val binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리스트 호출
        // viewModel.getAllWebInfo()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        Log.d("[Debug]", "onSupportNavigateUp in ItemDetailHostActivity")
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}