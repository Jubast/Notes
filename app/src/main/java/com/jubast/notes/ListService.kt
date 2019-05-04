package com.jubast.notes

import android.content.Intent
import android.widget.RemoteViewsService

class ListService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ListFactory(applicationContext, intent)
    }
}