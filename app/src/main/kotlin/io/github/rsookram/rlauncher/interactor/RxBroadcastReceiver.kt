package io.github.rsookram.rlauncher.interactor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Observable
import io.reactivex.android.MainThreadDisposable

data class Broadcast(val context: Context, val intent: Intent)

fun Context.broadcasts(filter: IntentFilter): Observable<Broadcast> =
        Observable.create { emitter ->
            MainThreadDisposable.verifyMainThread()

            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    emitter.onNext(Broadcast(context, intent))
                }
            }

            registerReceiver(receiver, filter)

            emitter.setDisposable(object : MainThreadDisposable() {
                override fun onDispose() {
                    unregisterReceiver(receiver)
                }
            })
        }
