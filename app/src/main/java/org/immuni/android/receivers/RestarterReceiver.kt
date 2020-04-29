package org.immuni.android.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.immuni.android.ImmuniApplication
import org.immuni.android.managers.BluetoothManager
import org.immuni.android.service.AlarmsManager
import org.immuni.android.service.DeleteUserDataWorker
import org.immuni.android.service.ImmuniForegroundService
import org.immuni.android.ui.onboarding.Onboarding
import org.immuni.android.util.log
import org.koin.core.KoinComponent
import org.koin.core.inject

class RestarterReceiver : BroadcastReceiver(), KoinComponent {

    private val btManager: BluetoothManager by inject()
    private val onboarding: Onboarding by inject()

    override fun onReceive(context: Context, intent: Intent) {

        log("Restarter event received, restarting workers if needed...")

        btManager.scheduleBLEWorker(context)
        DeleteUserDataWorker.scheduleWork(
            context
        )

        // re-schedule next alarm

        AlarmsManager.scheduleWorks(ImmuniApplication.appContext)

        // if service is stopped
        if(!ImmuniForegroundService.isServiceStarted && intent.extras?.containsKey("alarmManager") == true) {
            log("Foreground service down, restarting it...")
        }
    }
}
