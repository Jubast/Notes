package com.jubast.notes

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.jubast.notes.virtualactors.Note
import com.jubast.notes.virtualactors.NoteType
import com.jubast.notes.virtualactors.exceptions.InvalidOperationException

class ListFactory(val context: Context, val intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private val _noteIds: MutableList<String> = mutableListOf()

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
        val noteId = _noteIds[position]
        val note = Note(noteId, context)

        val views = RemoteViews(context.packageName, R.layout.widget_note_layout)
        views.setTextViewText(R.id.text, note.state.text)

        if(note.state.checked){
            views.setInt(R.id.text, "setPaintFlags", Paint.STRIKE_THRU_TEXT_FLAG)
        }
        else{
            views.setInt(R.id.text, "setPaintFlags", Paint.ANTI_ALIAS_FLAG)
        }

        // Send note id
        val extras = Bundle()
        extras.putString(NoteSettings.NOTE_ID, noteId)

        val intent = Intent()
        intent.putExtras(extras)

        // Set the onClick for this item
        views.setOnClickFillInIntent(R.id.itemContainer, intent)

        return views
    }

    override fun getCount(): Int {
        return _noteIds.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
        _noteIds.clear()
    }

    private fun updateItems(){
        val noteTypeId = intent.extras?.getString(NoteSettings.NOTE_TYPE_ID, null)
                ?: throw InvalidOperationException("Note type id is required")
        val notes= NoteType(noteTypeId, context).state.notes
        _noteIds.clear()
        _noteIds.addAll(notes)
    }
}