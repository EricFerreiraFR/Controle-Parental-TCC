package com.example.tcccontroleparental

//import android.annotation.SuppressLint
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var txtVpnStatus: TextView
    private val vpnPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                startVpnService()
            }
        }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🔹 Inicializa o TextView
        txtVpnStatus = findViewById(R.id.txtVpnStatus)
        txtVpnStatus.text = "VPN DESATIVADA"

        // 🔹 Inicia o fluxo da VPN
        startVpnFlow()
    }

    private fun startVpnFlow() {
        val intent = VpnService.prepare(this)

        if (intent != null) {
            vpnPermissionLauncher.launch(intent)
        } else {
            startVpnService()
        }
    }

    private fun startVpnService() {
        val intent = Intent(this, MyVpnService::class.java)

        startForegroundService(intent)

        setVpnActiveUI()
    }


    @SuppressLint("SetTextI18n")
    private fun setVpnActiveUI() {
        txtVpnStatus.text = "VPN ATIVA"
        txtVpnStatus.setTextColor(getColor(android.R.color.holo_green_dark))
    }
}
