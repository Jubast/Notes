package com.jubast.notes

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.jubast.notes.activities.NoteTypeActivity
import com.jubast.notes.virtualactors.*

/**
 * Implementation of App Widget functionality.
 */
class NotesWidgetProvider : AppWidgetProvider() {
    companion object {
        fun updateWidgets(context: Context){
            // Send update Request (will execute, onRequest with ACTION_APPWIDGET_UPDATE and onUpdate)
            val updateWidgetIntent = Intent(context, NotesWidgetProvider::class.java)
            updateWidgetIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val array = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, NotesWidgetProvider::class.java))
            updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, array)

            context.sendBroadcast(updateWidgetIntent)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_DISABLED -> {
                val component = ComponentName(context, NotesWidgetProvider::class.java)
                val appIds = AppWidgetManager.getInstance(context).getAppWidgetIds(component)
                NoteWidgetManager(context).updateWidgets(appIds)
            }
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val component = ComponentName(context, NotesWidgetProvider::class.java)
                val manager = AppWidgetManager.getInstance(context)

                // Update NoteWidgetManager
                NoteWidgetManager(context).updateWidgets(manager.getAppWidgetIds(component))

                // Update Service number hack
                ServiceManager(context).updateServices(manager.getAppWidgetIds(component))

                // Update the listViewItems
                manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(component), R.id.notesListView)
            }
            NoteSettings.UPDATE_NOTE_CHECKED -> {
                val noteId = intent.extras?.getString(NoteSettings.NOTE_ID)
                if(noteId != null){
                    Note(noteId, context).also {it.setChecked(!it.state.checked)}
                    updateWidgets(context)
                }
            }
        }

        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int){
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.widget_notes)

        val noteTypeId = NoteWidget(appWidgetId.toString(), context).state.noteTypeId

        // if widget has no NoteType reference don't do anything
        if(noteTypeId.isEmpty()){
            views.setTextViewText(R.id.twNoteTypeName, "No data.")
            appWidgetManager.updateAppWidget(appWidgetId, views)
            return
        }

        val noteType = NoteType(noteTypeId, context)

        views.setTextViewText(R.id.twNoteTypeName, noteType.state.name)

        // OnClick for Opening the Application
        val appIntent = Intent(context, NoteTypeActivity::class.java)
        appIntent.putExtra(NoteSettings.NOTE_TYPE_ID, noteTypeId)
        val pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0)
        views.setOnClickPendingIntent(R.id.twNoteTypeName, pendingIntent)

        // HACK
        var type : Class<*>? = null
        val serviceManager = ServiceManager(context)
        // if appWidgetId exists
        when(appWidgetId.toString()){
            serviceManager.state.service1Used -> {
                type = ListService1::class.java
            }
            serviceManager.state.service2Used -> {
                type = ListService2::class.java
            }
            serviceManager.state.service3Used -> {
                type = ListService3::class.java
            }
            serviceManager.state.service4Used -> {
                type = ListService4::class.java
            }
        }

        // add new appWidgetId
        if(type == null){
            when{
                serviceManager.state.service1Used.isEmpty() -> {
                    type = ListService1::class.java
                    serviceManager.state.service1Used = appWidgetId.toString()
                    serviceManager.stateChanged()
                }
                serviceManager.state.service2Used.isEmpty() -> {
                    type = ListService2::class.java
                    serviceManager.state.service2Used = appWidgetId.toString()
                    serviceManager.stateChanged()
                }
                serviceManager.state.service3Used.isEmpty() -> {
                    type = ListService3::class.java
                    serviceManager.state.service3Used = appWidgetId.toString()
                    serviceManager.stateChanged()
                }
                serviceManager.state.service4Used.isEmpty() -> {
                    type = ListService4::class.java
                    serviceManager.state.service4Used = appWidgetId.toString()
                    serviceManager.stateChanged()
                }
            }
        }

        // Activate ListView
        val intent = Intent(context, type)
        intent.putExtra(NoteSettings.NOTE_TYPE_ID, noteTypeId)
        views.setRemoteAdapter(R.id.notesListView, intent)

        // OnClick for ListView
        // Prepares onClick Intent template for the ListView (Doesn't work any other way)
        val smIntent = Intent(context, NotesWidgetProvider::class.java)
        smIntent.action = NoteSettings.UPDATE_NOTE_CHECKED
        val smPendingIntent = PendingIntent.getBroadcast(context, 0, smIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setPendingIntentTemplate(R.id.notesListView, smPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

