package com.example.subscribewebpage.placeholder

import com.example.subscribewebpage.common.Const
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

    val ITEMS: MutableList<PlaceholderItem> = ArrayList()
    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()

    private const val COUNT = 25

    init {
        for (i in 1 until COUNT) {
            val placeholderItem = PlaceholderItem("${Const.ARG_ITEM_ID}_$i", "Item $i", "contents")
            ITEMS.add(placeholderItem)
            ITEM_MAP[placeholderItem.id] = placeholderItem
        }
    }

    data class PlaceholderItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}