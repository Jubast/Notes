package com.jubast.notes

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.jubast.notes.activities.MainActivity

/**
 * Implementation of App Widget functionality.
 */
class NotesWidgetProvider : AppWidgetProvider() {
    companion object {
        const val TEXT_CHECK: String = "com.jubast.notes.widget.listView_onClick"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action: String = intent.action

        when (action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                // Update the listViewItems
                val manager = AppWidgetManager.getInstance(context)
                val component = ComponentName(context, NotesWidgetProvider::class.java)
                manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(component), R.id.mainListView)
            }
            NotesWidgetProvider.TEXT_CHECK -> {
                val identity = intent.extras.getString(NotesWidgetProvider.TEXT_CHECK)
                DBHelperOLD(context).checkText(identity)
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
        val views = RemoteViews(context.packageName, R.layout.notes_widget)

        // OnClick for Opening the Application
        val appIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0)
        views.setOnClickPendingIntent(R.id.btnAdd, pendingIntent)

        // Activate ListView
        val intent = Intent(context, ListService::class.java)
        views.setRemoteAdapter(R.id.mainListView, intent)

        // OnClick for ListView
        // Prepares onClick Intent template for the ListView (Doesn't work any other way)
        val smIntent = Intent(context, NotesWidgetProvider::class.java)
        smIntent.action = NotesWidgetProvider.TEXT_CHECK
        val smPendingIntent = PendingIntent.getBroadcast(context, 0, smIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setPendingIntentTemplate(R.id.mainListView, smPendingIntent)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

