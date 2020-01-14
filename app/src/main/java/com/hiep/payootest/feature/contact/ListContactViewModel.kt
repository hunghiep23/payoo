package com.hiep.payootest.feature.contact

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hiep.payootest.model.Response
import com.hiep.payootest.model.Contact


class ListContactViewModel : ViewModel() {
    private var _fullContactList = ArrayList<Contact>()
    val contactsLiveData = MutableLiveData<Response<List<Contact>>>()

    fun getContacts(applicationContext: Context?) {
        val contentResolver = applicationContext?.contentResolver
        val sortByName = "upper (${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}) ASC"
        val cursor = contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            sortByName
        )
        cursor?.let {
            _fullContactList.clear()
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phone =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val contact = Contact("", name, phone)
                    _fullContactList.add(contact)
                }
                contactsLiveData.value = Response.succeed(_fullContactList)
            } else {
                contactsLiveData.value = Response.empty()
            }
            cursor.close()
        }

    }

    fun searchContacts(keyword: String) {
        val filteredList = ArrayList<Contact>()
        _fullContactList.forEach { contact ->
            if (contact.name?.contains(keyword) == true || contact.phone?.contains(keyword) == true) {
                filteredList.add(contact)
            }
        }
        if (filteredList.size == 0)
            contactsLiveData.value = Response.empty()
        else
            contactsLiveData.value = Response.succeed(filteredList)
    }
}