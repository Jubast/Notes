package com.jubast.notes

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.jubast.notes.activities.NoteTypeActivity
import com.jubast.notes.activities.redirect.*
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
                    Note(noteId, context).also {it.setChecked(!it.getActorState().checked)}
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

        val noteTypeId = NoteWidget(appWidgetId.toString(), context).getActorState().noteTypeId

        // if widget has no NoteType reference don't do anything
        if(noteTypeId.isEmpty()){
            views.setTextViewText(R.id.twNoteTypeName, "No data.")
            appWidgetManager.updateAppWidget(appWidgetId, views)
            return
        }

        // HACK
        var serviceType : Class<*>? = null
        var activityType : Class<*>? = null
        val serviceManager = ServiceManager(context)
        // if appWidgetId exists
        when(appWidgetId.toString()){
            serviceManager.getActorState().service1Used -> {
                serviceType = ListService1::class.java
                activityType = RedirectActivity1::class.java
            }
            serviceManager.getActorState().service2Used -> {
                serviceType = ListService2::class.java
                activityType = RedirectActivity2::class.java
            }
            serviceManager.getActorState().service3Used -> {
                serviceType = ListService3::class.java
                activityType = RedirectActivity3::class.java
            }
            serviceManager.getActorState().service4Used -> {
                serviceType = ListService4::class.java
                activityType = RedirectActivity4::class.java
            }
        }

        val noteType = NoteType(noteTypeId, context)
        views.setTextViewText(R.id.twNoteTypeName, noteType.getActorState().name)

        // OnClick for Opening the Application
        val appIntent = Intent(context, activityType)
        appIntent.putExtra(NoteSettings.NOTE_TYPE_ID, noteTypeId)
        val pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0)
        views.setOnClickPendingIntent(R.id.twNoteTypeName, pendingIntent)

        // add new appWidgetId
        if(serviceType == null){
            when{
                serviceManager.getActorState().service1Used.isEmpty() -> {
                    serviceType = ListService1::class.java
                    serviceManager.setServiceUsed(1, appWidgetId.toString())
                }
                serviceManager.getActorState().service2Used.isEmpty() -> {
                    serviceType = ListService2::class.java
                    serviceManager.setServiceUsed(2, appWidgetId.toString())
                }
                serviceManager.getActorState().service3Used.isEmpty() -> {
                    serviceType = ListService3::class.java
                    serviceManager.setServiceUsed(3, appWidgetId.toString())
                }
                serviceManager.getActorState().service4Used.isEmpty() -> {
                    serviceType = ListService4::class.java
                    serviceManager.setServiceUsed(4, appWidgetId.toString())
                }
            }
        }

        // Activate ListView
        val intent = Intent(context, serviceType)
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

