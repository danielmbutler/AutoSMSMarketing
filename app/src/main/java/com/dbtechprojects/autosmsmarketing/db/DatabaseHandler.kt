package com.dbtechprojects.autosmsmarketing.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.dbtechprojects.autosmsmarketing.db.entities.MessageItem
import com.dbtechprojects.autosmsmarketing.db.entities.PhoneNumber
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Inject


//creating the database logic, extending the SQLiteOpenHelper base class


class DatabaseHandler (private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {




    companion object {
        private const val DATABASE_VERSION = 14 // Database version
        private const val DATABASE_NAME = "PhoneNumberDatabase" // Database name
        private const val TABLE = "PhoneNumberTable" // Table Name
        private const val TABLE2 = "MessagesTable"

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_NAME = "Name"
        private const val MESSAGE_SENT = "messagesent"
        private const val DB2_KEY_ID = "_id"
        private const val DB2_KEY_TITLE = "Title"
        private const val DB2_KEY_MESSAGE = "Message"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_TABLE_1 = ("CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + MESSAGE_SENT +  " INTEGER)")
        db?.execSQL(CREATE_TABLE_1)

        val CREATE_TABLE_2 = ("CREATE TABLE " + TABLE2 + "("
                + DB2_KEY_ID + " INTEGER PRIMARY KEY,"
                + DB2_KEY_TITLE + " TEXT,"
                + DB2_KEY_MESSAGE + " TEXT)")
        db?.execSQL(CREATE_TABLE_2)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE2")
        onCreate(db)
    }

    /**
     * Function to insert a Happy Place details to SQLite Database.
     */

    fun addPhoneNumber(phonenumber: PhoneNumber): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, phonenumber.number) // HappyPlaceModelClass TITLE

        // Inserting Row
        val result = db.insert(TABLE, null, contentValues)
        //2nd argument is String containing nullColumnHack  

        db.close() // Closing database connection
        return result
    }


    fun updatePhoneNumber(phonenumber: PhoneNumber):   Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, phonenumber.number) // HappyPlaceModelClass TITLE
        contentValues.put(MESSAGE_SENT, phonenumber.messagesent) // HappyPlaceModelClass IMAGE

        // Updating Row
        val success =
                db.update(
                        TABLE, contentValues,
                        KEY_ID + "=" + phonenumber.id,
                        null
                )
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success

    }

    /**
     * Function to read all the list of Happy Places data which are inserted.
     */

    fun getPhoneNumbers(): ArrayList<PhoneNumber> {

        // A list is initialize using the data model class in which we will add the values from cursor.
        val happyPlaceList: ArrayList<PhoneNumber> = ArrayList()

        val selectQuery = "SELECT  * FROM $TABLE" // Database select query

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = PhoneNumber(
                            cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                            cursor.getInt(cursor.getColumnIndex(MESSAGE_SENT))

                    )
                    happyPlaceList.add(place)

                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return happyPlaceList
    }


    fun getMessages(): ArrayList<MessageItem> {

        // A list is initialize using the data model class in which we will add the values from cursor.
        val happyPlaceList: ArrayList<MessageItem> = ArrayList()

        val selectQuery = "SELECT  * FROM $TABLE2" // Database select query

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = MessageItem(
                        cursor.getInt(cursor.getColumnIndex(DB2_KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(DB2_KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DB2_KEY_MESSAGE))

                    )
                    happyPlaceList.add(place)

                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return happyPlaceList
    }

    /**
     * Function to update record
     */

    fun updateMessage(messageItem: MessageItem): Int{

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DB2_KEY_TITLE, messageItem.Title) // Message TITLE
        contentValues.put(DB2_KEY_MESSAGE, messageItem.Message) // MessageITEM Message

        // Updating Row
        val success =
                db.update(
                        TABLE2, contentValues,
                        DB2_KEY_ID+ "=" + messageItem.id,
                        null
                )
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success

    }


    fun addMessage(messageItem: MessageItem): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DB2_KEY_TITLE, messageItem.Title) // HappyPlaceModelClass TITLE
        contentValues.put(DB2_KEY_MESSAGE, messageItem.Message)
        // Inserting Row
        val result = db.insert(TABLE2, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }
    /**
     * Function to delete happy place details.
     */

    fun deletePhoneNumber(phonenumber: PhoneNumber): Int {
        val db = this.writableDatabase
        // Deleting Row
        val success = db.delete(TABLE, KEY_ID + "=" + phonenumber.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }


    fun deleteMessage(messageItem: MessageItem): Int {
        val db = this.writableDatabase
        // Deleting Row
        val success = db.delete(TABLE2, KEY_ID + "=" + messageItem.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}
