package com.jubast.notes.virtualactors

import android.content.Context
import com.jubast.notes.containers.ServiceManagerState

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

    fun stateChanged(){
        saveState()
    }
}