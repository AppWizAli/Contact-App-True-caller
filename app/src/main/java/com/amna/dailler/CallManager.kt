package com.amna.dailler

import android.telecom.Call

object CallManager {
    var call: Call? = null
    private var listener: Listener? = null

    interface Listener {
        fun onStateChanged(state: Int)
        fun onCallEnded()
    }

    fun updateCall(call: Call) {
        this.call = call
        call.registerCallback(object : Call.Callback() {
            override fun onStateChanged(call: Call, state: Int) {
                listener?.onStateChanged(state)
            }
            override fun onDetailsChanged(call: Call, details: Call.Details) {
                 // update details if needed
            }
        })
        listener?.onStateChanged(call.state)
    }
    
    fun removeCall() {
        this.call = null
        listener?.onCallEnded()
    }

    fun registerListener(listener: Listener) {
        this.listener = listener
        call?.let { listener.onStateChanged(it.state) }
    }

    fun unregisterListener() {
        this.listener = null
    }

    fun hangup() {
        if (call?.state == Call.STATE_RINGING) {
             call?.reject(false, null)
        } else {
             call?.disconnect()
        }
    }
    
    fun answer() {
        call?.answer(0)
    }
    
    fun mute(isMuted: Boolean) {
        // Implement muting logic if needed via InCallService
    }
    
    fun setSpeaker(isSpeaker: Boolean) {
        // Implement speaker logic if needed
    }
}
