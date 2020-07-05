package com.jubast.notes.virtualactors

import android.content.Context
import com.jubast.notes.containers.ServiceManagerState
import com.jubast.notes.virtualactors.abstract.StateActor

class ServiceManager(context: Context): StateActor<ServiceManagerState>(".", context, ::ServiceManagerState) {
    fun updateServices(appWidgetIds: IntArray){
        val stringAppWidgetIds = mutableListOf<String>()
        for (ids in appWidgetIds){
            stringAppWidgetIds.add(ids.toString())
        }

        if(!stringAppWidgetIds.contains(state.service1Used)){
            state.service1Used = ""
        }
        if(!stringAppWidgetIds.contains(state.service2Used)){
            state.service2Used = ""
        }
        if(!stringAppWidgetIds.contains(state.service3Used)){
            state.service3Used = ""
        }
        if(!stringAppWidgetIds.contains(state.service4Used)){
            state.service4Used = ""
        }

        saveState()
    }

    fun setServiceUsed(index: Int, appWidgetId: String) {
        when (index) {
            1 -> state.service1Used = appWidgetId
            2 -> state.service2Used = appWidgetId
            3 -> state.service3Used = appWidgetId
            4 -> state.service4Used = appWidgetId
        }

        saveState()
    }
}