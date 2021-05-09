package com.example.subscribewebpage

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.subscribewebpage.common.Const.DEBUG_TAG
import com.example.subscribewebpage.data.Transaction
import com.example.subscribewebpage.data.WebInfo
import com.example.subscribewebpage.databinding.ActivityItemDetailBinding
import kotlin.concurrent.thread

/**
 * 메인 액티비티
 */
class ItemDetailHostActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Log
        Log.d("[Debug]", "onCreate in ItemDetailHostActivity")

        val binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        thread(start = true) {
            val transaction = Transaction.getInstance(context)
            val dao = transaction?.webInfoDao()
//            dao?.insertAll(
//                WebInfo("name ${Math.random()}" , "keyword", "url", 5, 20201231000000)
//            )

            dao?.getAll()?.run {
                forEach {
                    Log.d(DEBUG_TAG, it.title)
                }
            }
        }

        return super.onCreateView(name, context, attrs)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        Log.d("[Debug]", "onSupportNavigateUp in ItemDetailHostActivity")
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}