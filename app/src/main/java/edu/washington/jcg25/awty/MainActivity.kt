package edu.washington.jcg25.awty

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.telephony.SmsManager
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val messageText = findViewById<EditText>(R.id.messageText)
        val phoneNumberText = findViewById<EditText>(R.id.phoneText)
        val frequencyText = findViewById<EditText>(R.id.frequencyText)

        val startButton = findViewById<Button>(R.id.startButton)

        val manager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        startButton.setOnClickListener{

            val intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("message", messageText.text.toString())
            intent.putExtra("number", phoneNumberText.text.toString())



            val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            if(startButton.text == "Start"){

                if(messageText.text.toString() != "" && phoneNumberText.text.toString().length == 10 && frequencyText.text.toString() != ""){
                    startButton.text = "Stop"

                    val frequency : Long = (frequencyText.text.toString().toInt() * 60 * 1000).toLong()

                    manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), frequency, pendingIntent)

                }
            } else {
                manager.cancel(pendingIntent)
                startButton.text = "Start"
            }
        }
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        SmsManager.getDefault().sendTextMessage(intent!!.extras.getString("number"), null, intent!!.extras.getString("message"), null, null)
        Toast.makeText(context, intent!!.extras.getString("message"), Toast.LENGTH_SHORT).show()
    }
}