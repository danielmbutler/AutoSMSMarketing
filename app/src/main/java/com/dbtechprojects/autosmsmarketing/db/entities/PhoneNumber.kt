package com.dbtechprojects.autosmsmarketing.db.entities


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "phone_numbers")
@Parcelize
data class PhoneNumber(

        var id: Int? = null,
    @ColumnInfo(name = "number") var number: String,
    @ColumnInfo(name = "messagesent") var messagesent: Int? = null
): Parcelable