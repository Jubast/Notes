package com.jubast.notes

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class ListFactory(context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private val _context: Context = context
    private val _intent: Intent = intent
    private val _items: MutableList<Item> = mutableListOf()

    override fun onCreate() {
        updateItems()
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onDataSetChanged() {
        updateItems()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getViewAt(position: Int): RemoteViews {
        val item = _items[position]

        val views = RemoteViews(_context.packageName, R.layout.list_view_item)
        views.setTextViewText(R.id.text, item.toString())

        if(item.checked){
            views.setInt(R.id.text, "setPaintFlags", Paint.STRIKE_THRU_TEXT_FLAG)
        }
        else{
            views.setInt(R.id.text, "setPaintFlags", Paint.ANTI_ALIAS_FLAG)
        }

        // Send a toString() of the current item as extra data in the Intent
        val extras = Bundle()
        extras.putString(NotesWidgetProvider.TEXT_CHECK, item.toIdentifyString())

        val intent = Intent()
        intent.putExtras(extras)

        // Set the onClick for this item
        views.setOnClickFillInIntent(R.id.itemContainer, intent)

        return views
    }

    override fun getCount(): Int {
        return _items.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
        _items.clear()
    }

    private fun updateItems(){
        val updater = DBHelperOLD(_context)
        val items = updater.getItems()
        _items.clear()
        _items.addAll(items)
    }
}