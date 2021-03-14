package com.dbtechprojects.autosmsmarketing.ui.dialog

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.dbtechprojects.autosmsmarketing.R
import com.dbtechprojects.autosmsmarketing.db.DatabaseHandler
import com.dbtechprojects.autosmsmarketing.db.entities.MessageItem
import com.dbtechprojects.autosmsmarketing.db.entities.PhoneNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SendMessageDialog (): DialogFragment() {

    private var mDialogSelectedMessageItem:MessageItem? = null
    private var mDialogPhoneNumber:PhoneNumber? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val fragement_container = container
        val rootview = layoutInflater.inflate(R.layout.dialog_send_message,fragement_container, false)

        val sendbutton = rootview.findViewById<Button>(R.id.message_dialog_SendBtn)
        val spinner    = rootview.findViewById<Spinner>(R.id.messages_dialog_spinner)

        val messagelist = arguments?.getParcelableArrayList<MessageItem>("messages")
        mDialogPhoneNumber = arguments?.getParcelable("number")
        Log.d("dialog","found messages : $messagelist")
        Log.d("dialog", "found arguments: ${arguments.toString()}")

        if (messagelist.isNullOrEmpty()){
            //this.fragmentManager?.beginTransaction()?.remove(this)?.commit()
        } else {

            val listofany = messagelist

            val MessageAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
                    R.layout.spinner, listofany as List<Any?>)

            val messageSpinner = spinner as Spinner
            messageSpinner.adapter = MessageAdapter

            messageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    // Get the value selected by the user
                    // e.g. to store it as a field or immediately call a method
                    val message: MessageItem = parent.selectedItem as MessageItem
                    Log.d("selectedItem", message.toString())

                    // set up form with selected spinner
                    mDialogSelectedMessageItem = message


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        }




        sendbutton.setOnClickListener {
            //close helpDialog
            // send message
            val model = mDialogPhoneNumber
            if (model != null){
                val smsManager = SmsManager.getDefault() as SmsManager
                smsManager.sendTextMessage(model.number, null, mDialogSelectedMessageItem?.Message, null, null)
                model.messagesent = 1

                GlobalScope.launch(Dispatchers.IO) {

                        Log.d("viewmodel", "PhoneNumberMessageStatus: ${model.messagesent}")
                        val db = DatabaseHandler(requireContext())
                        db.updatePhoneNumber(model)
                }
            }

            Toast.makeText(requireContext(), "messagesent", Toast.LENGTH_SHORT).show()
            this.dismiss()
            this.fragmentManager?.beginTransaction()?.remove(this)?.commit()


        }

        return rootview
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity: Fragment? = parentFragment
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }
}