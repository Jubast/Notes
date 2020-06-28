package com.jubast.notes.virtualactors

import android.content.Context
import com.jubast.notes.containers.NoteWidgetManagerState
import com.jubast.notes.virtualactors.abstract.StateActor

class NoteWidgetManager(context: Context): StateActor<NoteWidgetManagerState>(".", context, ::NoteWidgetManagerState) {
    fun updateWidgets(appWidgetIds: IntArray){
        val stringAppWidgetIds = mutableListOf<String>()
        for (ids in appWidgetIds){
            stringAppWidgetIds.add(ids.toString())
        }

        // delete if appWidgetIds doesn't contain saved widget id
        val widgetsToGetDeleted = mutableListOf<String>()
        for (widgetId in state.widgets){
            if(!stringAppWidgetIds.contains(widgetId)){
                widgetsToGetDeleted.add(widgetId)
            }
        }
        for (widgetId in widgetsToGetDeleted){
            NoteWidget(widgetId, context).delete()
            state.widgets.remove(widgetId)
        }

        for (widgetId in stringAppWidgetIds){
            val widget = NoteWidget(widgetId, context)
            if (!widget.getActorState().generated) {
                widget.generate()
                state.widgets.add(widgetId)
            }
        }

        saveState()
    }
}