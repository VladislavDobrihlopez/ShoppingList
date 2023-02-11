package com.voitov.todolist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.voitov.todolist.presentation.ShopListApp
import javax.inject.Inject

class ShopListContentProvider : ContentProvider() {
    private val component by lazy {
        (context as ShopListApp).component
    }

    @Inject
    lateinit var dao: ShopListDao

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, PATH, QUERY_GET_ALL_ITEMS)
        addURI(AUTHORITY, PATH2, QUERY_GET_ITEM_BY_ID)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        val operationCode = uriMatcher.match(p0)
        Log.d(TAG, "$operationCode")
        return when (operationCode) {
            QUERY_GET_ALL_ITEMS -> {
                dao.getShopListCursor()
            }
            else -> null
        }
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    companion object {
        private const val AUTHORITY = "com.voitov.todolist"
        private const val PATH = "ShopItems"
        private const val PATH2 = "ShopItems/#"
        private const val QUERY_GET_ALL_ITEMS = 100
        private const val QUERY_GET_ITEM_BY_ID = 101
        private const val TAG = "ShopListContentProvider"
    }
}