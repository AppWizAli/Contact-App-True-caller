package com.amna.dailler

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import android.accounts.AccountManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepository(private val context: Context) {

    suspend fun getContacts(): List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.STARRED
        )

        context.contentResolver.query(uri, projection, null, null, "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC")?.use { cursor ->
            val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)
            val starredIndex = cursor.getColumnIndex(ContactsContract.Contacts.STARRED)

            // Limit to prevent loading too many contacts at once
            while (cursor.moveToNext() && contacts.size < 1000) {
                val id = cursor.getString(idIndex)
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex)
                val photoUri = cursor.getString(photoIndex)
                val isFavorite = cursor.getInt(starredIndex) == 1
                contacts.add(Contact(id, name, number, photoUri, isFavorite = isFavorite))
            }
        }
        return@withContext contacts
    }

    suspend fun getFavorites(): List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.STARRED
        )
        
        // Query only favorites directly - much faster than loading all contacts
        val selection = "${ContactsContract.Contacts.STARRED} = ?"
        val selectionArgs = arrayOf("1")

        context.contentResolver.query(
            uri, projection, selection, selectionArgs,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)
            val starredIndex = cursor.getColumnIndex(ContactsContract.Contacts.STARRED)

            while (cursor.moveToNext()) {
                val id = cursor.getString(idIndex)
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex)
                val photoUri = cursor.getString(photoIndex)
                val isFavorite = cursor.getInt(starredIndex) == 1
                contacts.add(Contact(id, name, number, photoUri, isFavorite = isFavorite))
            }
        }
        return@withContext contacts
    }

    suspend fun getAccounts(): List<AccountInfo> = withContext(Dispatchers.IO) {
        val accounts = mutableListOf<AccountInfo>()
        val manager = AccountManager.get(context)
        manager.accounts.forEach { account ->
            accounts.add(AccountInfo(account.name, account.type))
        }
        // Always add local "Device" account
        if (accounts.none { it.name == "Device" }) {
            accounts.add(AccountInfo("Device", "com.android.local"))
        }
        return@withContext accounts
    }

    suspend fun saveContact(
        firstName: String,
        lastName: String,
        phone: String,
        accountName: String?,
        accountType: String?
    ): Boolean = withContext(Dispatchers.IO) {
        val ops = arrayListOf<ContentProviderOperation>()

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, accountType)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, accountName)
            .build())

        // Name
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "$firstName $lastName")
            .build())

        // Phone
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            .build())

        return@withContext try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun setContactFavorite(contactId: String, isFavorite: Boolean): Boolean = withContext(Dispatchers.IO) {
        val ops = arrayListOf<ContentProviderOperation>()
        val uri = ContactsContract.Contacts.CONTENT_URI
        
        ops.add(ContentProviderOperation.newUpdate(uri)
            .withSelection("${ContactsContract.Contacts._ID} = ?", arrayOf(contactId))
            .withValue(ContactsContract.Contacts.STARRED, if (isFavorite) 1 else 0)
            .build())

        return@withContext try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    suspend fun deleteContact(contactId: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val uri = ContactsContract.RawContacts.CONTENT_URI
            val deleted = context.contentResolver.delete(
                uri,
                "${ContactsContract.RawContacts.CONTACT_ID} = ?",
                arrayOf(contactId)
            )
            deleted > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    suspend fun getContactVCard(contactId: String): String? = withContext(Dispatchers.IO) {
        return@withContext try {
            val uri = android.net.Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_VCARD_URI,
                contactId
            )
            context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
