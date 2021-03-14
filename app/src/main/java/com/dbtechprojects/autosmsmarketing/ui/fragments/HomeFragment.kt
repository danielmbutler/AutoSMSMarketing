package com.dbtechprojects.autosmsmarketing.ui.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbtechprojects.autosmsmarketing.R
import com.dbtechprojects.autosmsmarketing.databinding.FragmentHomeBinding
import com.dbtechprojects.autosmsmarketing.db.DatabaseHandler
import com.dbtechprojects.autosmsmarketing.db.entities.MessageItem
import com.dbtechprojects.autosmsmarketing.db.entities.PhoneNumber
import com.dbtechprojects.autosmsmarketing.ui.PhoneNumberAdapter
import com.dbtechprojects.autosmsmarketing.ui.dialog.SendMessageDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.phone_number_item.view.*
import kotlinx.coroutines.*


@AndroidEntryPoint
class HomeFragment() : Fragment(), DialogInterface.OnDismissListener{

//
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private var mMessageItems: ArrayList<MessageItem>? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


        if (ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.READ_PHONE_STATE
                )!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_PHONE_STATE), 4
            );
        }


        if (ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.READ_CALL_LOG
                )!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_CALL_LOG), 4
            );
        }

        if (ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.SEND_SMS
                )!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.SEND_SMS), 4
            );
        }

        binding.homeMessageImage.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_messages)
            Log.d("imageview", "clicked")
        }


        // set variables

        val phonenumbers = viewModel.getphonenumbers(requireContext())
        val messagelist = viewModel.getmessages(requireContext())

        if(messagelist.isNotEmpty()){
            mMessageItems = messagelist as ArrayList<MessageItem>
        }



        if(phonenumbers.isNotEmpty()){
            setuprv(phonenumbers)
        }

        binding.homeSwipetorefreshlayout.setOnRefreshListener {


            // change color
            binding.homeSwipetorefreshlayout.setColorSchemeColors(resources.getColor(R.color.MainBg))
            Log.d("fragment", "called")

            val phonenumberslist = DatabaseHandler(requireContext()).getPhoneNumbers()
            Log.d("fragment", "$phonenumberslist")


            if(phonenumberslist.isNotEmpty()){
                setuprv(phonenumberslist)
                binding.homeSwipetorefreshlayout.isRefreshing = false //remove refreshing animation
            } else{
                binding.homeSwipetorefreshlayout.isRefreshing = false
                return@setOnRefreshListener
            }
        }


//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })




        return view
    }

    private fun setuprv(list: List<PhoneNumber>){
        binding.rv.invalidate()
        val lm = LinearLayoutManager(requireContext())
        lm.reverseLayout = true
        lm.stackFromEnd = true
        binding.rv.layoutManager = lm

        val adapter = PhoneNumberAdapter(list)
        binding.rv.adapter = adapter

        adapter.setOnClickListener(object : PhoneNumberAdapter.OnClickListener {

            override fun onClick(position: Int, model: PhoneNumber, view: View) {

                if (mMessageItems != null){
                    val bundle = Bundle()
                    bundle.putParcelableArrayList("messages",mMessageItems)
                    bundle.putParcelable("number",model)
                    val dialog = SendMessageDialog()
                    val fragmentmanager = childFragmentManager
                    dialog.arguments = bundle
                    dialog.show(fragmentmanager, "show send message")

                } else {
                    Toast.makeText(requireContext(), "Please create some messages",
                            Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    override fun onDismiss(dialog: DialogInterface?) {

        Log.d("DialogOnDismiss", "called")

        val phonenumbers = viewModel.getphonenumbers(requireContext())


        if(phonenumbers.isNotEmpty()){
            setuprv(phonenumbers)
        }

    }


}