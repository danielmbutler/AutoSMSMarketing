package com.dbtechprojects.autosmsmarketing.db


import androidx.lifecycle.LiveData
import androidx.room.*
import com.dbtechprojects.autosmsmarketing.db.entities.MessageItem
import com.dbtechprojects.autosmsmarketing.db.entities.PhoneNumber

@Dao
interface PhoneNumberDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: PhoneNumber)

    @Delete
    suspend fun delete(item: PhoneNumber)

    @Query("SELECT * FROM phone_numbers")
    fun getAllPhoneNumbers(): LiveData<List<PhoneNumber>>

    @Query("SELECT * FROM messages")
    fun getAllMessages(): List<MessageItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMessage(item: MessageItem)

    @Delete
    suspend fun deleteMessage(item: MessageItem)
}