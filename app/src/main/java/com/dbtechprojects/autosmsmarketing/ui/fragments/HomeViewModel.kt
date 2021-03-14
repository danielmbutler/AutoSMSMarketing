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
import com.dbtechprojects.autosmsmarketing.util.Repository

class HomeViewModel @ViewModelInject constructor(
private val repository: Repository
) : ViewModel(

) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    suspend fun updatePhoneNumber(phonenumber: PhoneNumber){
        repository.updatePhoneNumber(phonenumber)

    }

    suspend fun updateMessage(message: MessageItem, context: Context){
       repository.updateMessage(message)
    }


     fun returnstring() = "test"

    suspend fun addmessage(message: MessageItem,context: Context){
        repository.addmessage(message)
    }

    fun getmessages(context: Context): List<MessageItem>{
      val list =  repository.getmessages()
        return list
    }

    fun getphonenumbers(context: Context): List<PhoneNumber>{
        val list =  repository.getphonenumbers()
        return list
    }

    suspend fun deleteMessage(message: MessageItem, context: Context){
        repository.deleteMessage(message)
    }

}