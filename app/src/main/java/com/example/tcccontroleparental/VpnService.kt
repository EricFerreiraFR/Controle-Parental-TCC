package com.example.tcccontroleparental

import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.content.Intent
import android.os.IBinder

class MyVpnService : VpnService(){

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}