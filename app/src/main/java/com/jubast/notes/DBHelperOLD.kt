package com.jubast.notes

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson

class DBHelperOLD(context: Context) {
    private val _context: Context = context
    private val SETTINGS: String = "settings"
    private val NAKUPI: String = "nakupi"

    fun getItems(): Array<Item>{
        return getList().toTypedArray()
    }

    fun saveItem(data: String){
        val list: MutableList<Item> = getList()
        val item = Item(data, false)
        list.add(item)

        saveItems(list)
    }

    fun saveItems(list: MutableList<Item>){
        var index: Int = 0
        while(index < list.size){
            list[index].id = index
            ++index
        }

        val json = Gson().toJson(list)

        val editor = _context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit()
        editor.putString(NAKUPI, json)
        editor.apply()
        update()
    }

    private fun getJsonData(): String{
        val prefs = _context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        return prefs.getString(NAKUPI, "")
    }

    fun getList(): MutableList<Item>{
        val data = getJsonData()
        val list = mutableListOf<Item>()
        if(data != ""){
            val array = Gson().fromJson(data, Array<Item>::class.java)
            list.addAll(array)
        }
        return list
    }

    private fun update(){
        /*val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        intent.component = ComponentName(_context, NotesWidgetProvider::class.java)*/

        // Send update Request (will execute, onRequest with ACTION_APPWIDGET_UPDATE and onUpdate)
        val intent = Intent(_context, NotesWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val array = AppWidgetManager.getInstance(_context).getAppWidgetIds(ComponentName(_context, NotesWidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, array)

        _context.sendBroadcast(intent)
    }

    fun remove(identity: String) {
        val items = getList()
        var changed = false
        var index: Int = 0
        while (index < items.size){
            if(items[index].toIdentifyString() == identity){
                changed = true
                break
            }
            ++index
        }

        if(changed){
            items.removeAt(index)
            saveItems(items)
        }
    }

    fun checkText(identity: String){
        val items = getList()

        for(item in items){
            if(item.toIdentifyString() == identity){
                item.checked = !item.checked
                break
            }
        }

        /*for(item in items){
            Log.d("TAG.KK", item.toString1())
        }*/

        saveItems(items)
    }

    fun editItem(item: Item){
        val items = getList()

        for(i in items){
            if(i.id == item.id){
                i.text = item.text
                i.checked = item.checked
                break
            }
        }

        saveItems(items)
    }
}