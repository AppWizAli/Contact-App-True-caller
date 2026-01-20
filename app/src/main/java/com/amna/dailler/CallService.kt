package com.amna.dailler

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log

class CallService : InCallService() {
    companion object {
        var currentCall: Call? = null
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        currentCall = call
        Log.d("CallService", "Call added: ${call.details.handle}")
        
        val intent = Intent(this, CallActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        currentCall = null
        Log.d("CallService", "Call removed")
    }
}
