# AutoSMSMarketing
Android App, Send messages to phone numbers that call you using premade messages, this app uses Notifications, Swipe To Refresh RecyclerView, Broadcast Receiver, Custom Spinner and SQLLite


# Main Features

Swipe to Refresh Layout for RecyclerView with onRefreshListner
```kotlin
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
```

Notification Setup when new phone calls come in

```kotlin

                // set up notification

                fun createNotificationChannel() {
                    // Create the NotificationChannel, but only on API 26+ because
                    // the NotificationChannel class is new and not in the support library
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val name = "Notification Channel"
                        val descriptionText = "Notification Channel for AutoSMSMarketing"
                        val importance = NotificationManager.IMPORTANCE_DEFAULT
                        val channel = NotificationChannel(1.toString(), name, importance).apply {
                            description = descriptionText
                        }
                        // Register the channel with the system
                        val notificationManager: NotificationManager? = arg0.getSystemService<NotificationManager>()

                        notificationManager?.createNotificationChannel(channel)
                    }
                }

                //Create an explicit intent for an Activity in your app
                val intent = Intent(arg0, MainActivity::class.java)
                val pendingIntent: PendingIntent = PendingIntent.getActivity(arg0, 0, intent, 0)

                var builder = NotificationCompat.Builder(arg0, "1")
                        .setSmallIcon(R.drawable.ic_baseline_local_phone_24)
                        .setContentTitle("New Phone Call Received")
                        .setContentText("PhoneNumber: $number")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true) // cancel notification after being clicked on

                with(NotificationManagerCompat.from(arg0)) {
                    // notificationId is a unique int for each notification that you must define
                    createNotificationChannel()
                    notify(1, builde
```
Custom Spinner Layout

```kotlin
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
  ```          
