package com.jubast.notes

import android.content.Intent
import android.widget.RemoteViewsService
import com.jubast.notes.virtualactors.ServiceManager

class ListService1: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ListFactory(applicationContext, intent)
    }
}

class ListService2: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ListFactory(applicationContext, intent)
    }
}
class ListService3: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ListFactory(applicationContext, intent)
    }
}
class ListService4: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ListFactory(applicationContext, intent)
    }
}