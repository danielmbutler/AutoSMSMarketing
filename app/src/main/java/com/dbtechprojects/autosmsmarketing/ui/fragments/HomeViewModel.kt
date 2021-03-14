package com.dbtechprojects.autosmsmarketing.ui.fragments

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbtechprojects.autosmsmarketing.db.DatabaseHandler
import com.dbtechprojects.autosmsmarketing.db.entities.MessageItem
import com.dbtechprojects.autosmsmarketing.db.entities.PhoneNumber
import com.dbtechprojects.autosmsmarketing.ui.MainActivity

class HomeViewModel @ViewModelInject constructor(

) : ViewModel(

) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    suspend fun updatePhoneNumber(phonenumber: PhoneNumber, context: Context){
        val db = DatabaseHandler(context)
        db.updatePhoneNumber(phonenumber)
    }

    suspend fun updateMessage(message: MessageItem, context: Context){
        val db = DatabaseHandler(context)
        db.updateMessage(message)
    }


     fun returnstring() = "test"

    suspend fun addmessage(message: MessageItem,context: Context){
        val db = DatabaseHandler(context)
        db.addMessage(message)
    }

    fun getmessages(context: Context): List<MessageItem>{
        val db = DatabaseHandler(context)
        val list = db.getMessages()
        if (list.isNotEmpty()){
            return list
        } else {
            return emptyList()
        }
    }

    fun getphonenumbers(context: Context): List<PhoneNumber>{
        val db = DatabaseHandler(context)
        val list = db.getPhoneNumbers()
        if (list.isNotEmpty()){
            return list
        } else {
            return emptyList()
        }
    }

    suspend fun deleteMessage(message: MessageItem, context: Context){
        val db = DatabaseHandler(context)
        db.deleteMessage(message)
    }

}