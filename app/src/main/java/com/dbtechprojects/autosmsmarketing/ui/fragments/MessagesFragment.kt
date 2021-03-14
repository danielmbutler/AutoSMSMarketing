package com.dbtechprojects.autosmsmarketing.ui.fragments

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dbtechprojects.autosmsmarketing.databinding.FragmentMessagesBinding
import com.dbtechprojects.autosmsmarketing.db.entities.MessageItem
import kotlinx.android.synthetic.main.fragment_messages.*
import kotlinx.android.synthetic.main.spinner.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MessagesFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentMessagesBinding
    private var mSelectedMessageItem:MessageItem? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val view = binding.root


        getmessages()

        binding.messagesFab.setOnClickListener {
            messages_form_title.setText("")
            messages_form_text.setText("")
            mSelectedMessageItem = null
        }

        binding.messageDeleteBtn.setOnClickListener {

            Log.d("MessageFragment", mSelectedMessageItem.toString())
            if(mSelectedMessageItem != null){
                GlobalScope.launch(Dispatchers.IO) {
                    viewModel.deleteMessage(mSelectedMessageItem!!,requireContext())
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "Message Deleted",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Message or Message Title is empty",
                        Toast.LENGTH_SHORT).show()
            }
        }

        binding.messageSavebutton.setOnClickListener {
            if (binding.messagesFormTitle.text.isNullOrEmpty() ||
                binding.messagesFormText.text.isNullOrEmpty() ) {

                Toast.makeText(requireContext(), "please fill out full form",
                        Toast.LENGTH_SHORT).show()

            } else {

                GlobalScope.launch(Dispatchers.IO) {
                    val messageitem = MessageItem(
                            if (mSelectedMessageItem == null) 0 else mSelectedMessageItem!!.id,
                            binding.messagesFormTitle.text.toString(),
                            binding.messagesFormText.text.toString()
                    )

                    if (messageitem.id == 0 ){
                        println("added message")
                        viewModel.addmessage(messageitem, requireContext())
                    } else {
                        println("updated message")
                        viewModel.updateMessage(messageitem, requireContext())
                    }

                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "Message Saved", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }

        binding.messageClearbutton.setOnClickListener {
            binding.messagesFormText.setText("")
        }
        return view
    }


    private fun getmessages(){
        val list = viewModel.getmessages(requireContext())

        if (list.isNotEmpty()){
            Log.d("messagefragment", list.toString())

            // set up spinner


            val MessageAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(),
                    com.dbtechprojects.autosmsmarketing.R.layout.spinner, list)

            val messageSpinner = binding.messagesSpinner as Spinner
            messageSpinner.adapter = MessageAdapter

            messageSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    // Get the value selected by the user
                    // e.g. to store it as a field or immediately call a method
                    val message: MessageItem = parent.selectedItem as MessageItem
                    Log.d("selectedItem", message.toString())

                    // set up form with selected spinner
                    val title = message.Title
                    val messsage = message.Message
                    binding.messagesFormTitle.setText(title)
                    binding.messagesFormText.setText(messsage)

                    mSelectedMessageItem = message

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }


    }
}