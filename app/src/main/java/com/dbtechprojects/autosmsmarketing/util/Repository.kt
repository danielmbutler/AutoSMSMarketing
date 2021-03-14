package com.dbtechprojects.autosmsmarketing.util


import android.content.Context
import com.dbtechprojects.autosmsmarketing.db.DatabaseHandler
import com.dbtechprojects.autosmsmarketing.db.entities.MessageItem
import com.dbtechprojects.autosmsmarketing.db.entities.PhoneNumber
import javax.inject.Inject

class Repository @Inject constructor(
    private val db: DatabaseHandler
) {

    suspend fun updatePhoneNumber(phonenumber: PhoneNumber){
        db.updatePhoneNumber(phonenumber)
    }

    suspend fun updateMessage(message: MessageItem){
        db.updateMessage(message)
    }

    suspend fun addPhonenumber(phonenumber: PhoneNumber){
        db.addPhoneNumber(phonenumber)
    }


    fun returnstring() = "test"

    suspend fun addmessage(message: MessageItem){
        db.addMessage(message)
    }

    fun getmessages(): List<MessageItem>{

        val list = db.getMessages()
        if (list.isNotEmpty()){
            return list
        } else {
            return emptyList()
        }
    }

    fun getphonenumbers(): List<PhoneNumber>{

        val list = db.getPhoneNumbers()
        if (list.isNotEmpty()){
            return list
        } else {
            return emptyList()
        }
    }

    suspend fun deleteMessage(message: MessageItem){
        db.deleteMessage(message)
    }


}