package com.amna.dailler

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amna.dailler.databinding.ActivityCallBinding

class CallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActions()
        observeCall()
    }

    private fun setupActions() {
        binding.fabEndCall.setOnClickListener {
            CallService.currentCall?.disconnect()
            finish()
        }

        binding.fabAnswer.setOnClickListener {
            CallService.currentCall?.answer(android.telecom.VideoProfile.STATE_AUDIO_ONLY)
        }

        binding.fabDecline.setOnClickListener {
            CallService.currentCall?.disconnect()
            finish()
        }
        
        setupAction(binding.actMute.root, "Mute", R.drawable.ic_more_vert_24)
        setupAction(binding.actKeypad.root, "Keypad", R.drawable.ic_dialpad_24)
        setupAction(binding.actSpeaker.root, "Speaker", R.drawable.ic_search_24)
        setupAction(binding.actAdd.root, "Add call", R.drawable.ic_more_vert_24)
        setupAction(binding.actHold.root, "Hold", R.drawable.ic_more_vert_24)
        setupAction(binding.actVideo.root, "Video", R.drawable.ic_groups_24)
    }

    private fun setupAction(root: View, label: String, icon: Int) {
        val actionBinding = com.amna.dailler.databinding.ViewCallActionBinding.bind(root)
        actionBinding.tvLabel.text = label
        actionBinding.ivIcon.setImageResource(icon)
    }

    private fun observeCall() {
        val call = CallService.currentCall
        call?.registerCallback(object : android.telecom.Call.Callback() {
            override fun onStateChanged(call: android.telecom.Call?, state: Int) {
                updateStatus(state)
            }
        })
    }

    private fun updateStatus(state: Int) {
        when (state) {
            android.telecom.Call.STATE_RINGING -> {
                binding.tvCallStatus.text = "Incoming call"
                binding.layoutIncomingActions.visibility = View.VISIBLE
                binding.fabEndCall.visibility = View.GONE
            }
            android.telecom.Call.STATE_ACTIVE -> {
                binding.tvCallStatus.text = "00:01"
                binding.layoutIncomingActions.visibility = View.GONE
                binding.fabEndCall.visibility = View.VISIBLE
            }
            android.telecom.Call.STATE_DIALING -> {
                binding.tvCallStatus.text = "Calling..."
                binding.layoutIncomingActions.visibility = View.GONE
                binding.fabEndCall.visibility = View.VISIBLE
            }
            android.telecom.Call.STATE_DISCONNECTED -> finish()
            else -> {
                binding.tvCallStatus.text = "Connecting..."
                binding.layoutIncomingActions.visibility = View.GONE
                binding.fabEndCall.visibility = View.VISIBLE
            }
        }
    }
}
