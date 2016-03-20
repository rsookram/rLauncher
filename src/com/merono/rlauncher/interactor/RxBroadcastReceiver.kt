package com.merono.rlauncher.interactor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import rx.Observable
import rx.android.MainThreadSubscription

data class Broadcast(val context: Context, val intent: Intent)

fun Context.broadcasts(filter: IntentFilter): Observable<Broadcast> {
  return Observable.create { subscriber ->
    MainThreadSubscription.verifyMainThread()

    val receiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        subscriber.onNext(Broadcast(context, intent))
      }
    }

    registerReceiver(receiver, filter)

    subscriber.add(object : MainThreadSubscription() {
      override fun onUnsubscribe() {
        unregisterReceiver(receiver)
      }
    })
  }
}
