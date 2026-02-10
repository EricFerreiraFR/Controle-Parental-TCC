package com.example.tcccontroleparental




import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.VpnService
import android.os.IBinder
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat


class MyVpnService : VpnService(){

    private var vpnInterface: ParcelFileDescriptor? = null



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(/* id = */ 1, /* notification = */ createNotification())
        startVpn()
        return START_STICKY
    }

    private fun startVpn(){
        val builder = Builder()
            .setSession("ParentalVPN")
            .setMtu(1500)
            .addAddress("172.16.11.1",32) //RFC1918  mais a mascara 32 para evitar conflitos
            .addRoute("0.0.0.0",0)//tunnel
            .addDnsServer("8.8.8.8")//dns
            .addDnsServer("8.8.4.4")
            .addDisallowedApplication(packageName)//evita loop de rede

        vpnInterface = builder.establish()
    }

    private fun createNotification(): Notification{
        val channelId = "vpnID"

        val channel = NotificationChannel(
            channelId,
            "Parental VPN",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Parental control active")
            .setContentText("monitoring network traffic")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onDestroy() {
        try {
            vpnInterface?.close()
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            vpnInterface = null
        }

        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

}