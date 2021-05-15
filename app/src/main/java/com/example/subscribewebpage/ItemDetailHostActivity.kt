package com.example.subscribewebpage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.subscribewebpage.common.AppDateUtils
import com.example.subscribewebpage.common.Const
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
        viewModel.getAllWebInfo()

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
            viewModel.insertWebInfo(
                WebInfoEntity(
                    title = "title",
                    description = "description",
                    keyword = "keyword",
                    url = "url",
                    interval = 15,
                    date = AppDateUtils.getStringDate()
                )
            )
        }

        createNotification(this, 1, "title", "content")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        Log.d("[Debug]", "onSupportNavigateUp in ItemDetailHostActivity")
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun createNotification(
        context: Context,
        id: Int,
        title: String,
        description: String
    ) {
        createNotificationChannel(title, description)

        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, ItemDetailHostActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        var builder = NotificationCompat.Builder(context, Const.NOTIFICATION_CHANNEL_ID).apply {
            this.priority = NotificationCompat.PRIORITY_DEFAULT
            this.setSmallIcon(R.drawable.ic_launcher_foreground)
            this.setContentTitle(title)
            this.setContentText(description)
            this.setContentIntent(pendingIntent)
            this.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }
    }

    private fun createNotificationChannel(
        title: String,
        des: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Const.NOTIFICATION_CHANNEL_ID, title, importance).apply {
                description = des
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}