package com.atul.truecallercontactapp

data class ContactModel(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val isStarred: Boolean = false
)