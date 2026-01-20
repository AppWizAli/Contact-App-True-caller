package com.amna.dailler

import android.net.Uri

enum class CallType {
    INCOMING, OUTGOING, MISSED, REJECTED, BLOCKED
}

enum class CallLogFilter {
    ALL, INCOMING, OUTGOING, MISSED, BLOCKED
}

enum class SimSlot {
    SIM1, SIM2
}

data class Contact(
    val id: String,
    val name: String,
    val number: String,
    val imageUri: String? = null,
    val accountType: String? = null,
    val accountName: String? = null,
    val isFavorite: Boolean = false
)

data class AccountInfo(
    val name: String,
    val type: String
)

data class FavoriteContact(
    val id: String,
    val name: String,
    val imageUri: String? = null
)

data class CallLogEntry(
    val id: String,
    val name: String?,
    val number: String,
    val time: String,
    val type: CallType,
    val country: String? = "Pakistan",
    val simSlot: SimSlot = SimSlot.SIM1,
    val duration: String? = null
)
