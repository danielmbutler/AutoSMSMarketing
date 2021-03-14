package com.dbtechprojects.autosmsmarketing.util


import com.dbtechprojects.autosmsmarketing.db.DatabaseHandler
import com.dbtechprojects.autosmsmarketing.db.entities.PhoneNumber
import javax.inject.Inject

//class Repository @Inject constructor(
//    private val db: DatabaseHandler
//) {
//
//    suspend fun upsert(item: PhoneNumber) = db.getPhoneNumbersDAO().upsert(item)
//
//    suspend fun delete(item: PhoneNumber) = db.getPhoneNumbersDAO().delete(item)
//
//
//    fun getAllPhoneNumbers() = db.getPhoneNumbersDAO().getAllPhoneNumbers()
//
//
//}