package com.atul.truecallercontactapp

data class CallLogModel(
    val name: String?,
    val number: String?,
    val date: String?,
    val duration: String?,
    val time: String?,
    val callType: Int,
    val label: String // ADDED THIS LINE
)