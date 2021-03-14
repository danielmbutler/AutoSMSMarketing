package com.dbtechprojects.autosmsmarketing.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "messages")
@Parcelize
data class MessageItem(

        val id: Int,
        @ColumnInfo(name = "Title") val Title: String,
        @ColumnInfo(name = "Message") val Message: String
): Parcelable{
    @PrimaryKey(autoGenerate = true)



    // set Title for Spinner in Message Activity

    override fun toString(): String {
        return this.Title // What to display in the Spinner list.
    }
}