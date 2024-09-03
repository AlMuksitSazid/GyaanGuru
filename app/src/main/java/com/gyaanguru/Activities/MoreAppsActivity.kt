package com.gyaanguru.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.ui.graphics.Color
import androidx.core.view.ViewCompat

import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyaanguru.Adapter.AppListAdapter
import com.gyaanguru.Domain.AppInfo
import com.gyaanguru.R

class MoreAppsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppListAdapter
    private val firebaseDatabase = Firebase.database

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_more_apps)
        window.statusBarColor = resources.getColor(R.color.navy_blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.moreAppsRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val appLinkRef = firebaseDatabase.getReference("AppLink").child("downloadLink")
        appLinkRef.get().addOnSuccessListener { dataSnapshot ->
            Log.d("MoreAppsActivity", "Number of children: ${dataSnapshot.childrenCount}") // Check if any children are fetched
            val appList = mutableListOf<AppInfo>()
            for (childSnapshot in dataSnapshot.children) {
                val appName = childSnapshot.key
                val downloadLink = childSnapshot.value as? String // Directly get download link
                if (appName != null && downloadLink != null) {
                    val iconResId = getIconResIdForApp(appName)
                    val description = getDescriptionForApp(appName)
                    appList.add(AppInfo(appName, description, iconResId, downloadLink))
                    Log.d("MoreAppsActivity", "Added app: $appName, $downloadLink") // Verify app details
                }
            }

            // Dynamically add CardViews to GridLayout
            for ((index, appInfo) in appList.withIndex()) {
                val appCardView = LayoutInflater.from(this).inflate(R.layout.item_app, null)

                val appNameTextView = appCardView.findViewById<TextView>(R.id.appNameTextView)
                val appDescriptionTextView = appCardView.findViewById<TextView>(R.id.appDescriptionTextView)
                val appIconImageView = appCardView.findViewById<ImageView>(R.id.appIconImageView)
                val downloadButton = appCardView.findViewById<Button>(R.id.downloadButton)

                appNameTextView.text = appInfo.name
                appDescriptionTextView.text = appInfo.description
                appIconImageView.setImageResource(appInfo.iconResId)

                downloadButton.setOnClickListener {
                    // Fetch the download link for the specific app
                    val specificAppLinkRef = appLinkRef.child(appInfo.name)

                    // Create and show the custom progress dialog immediately
                    val dialogBuilder = AlertDialog.Builder(this)
                    val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                    val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
                    dialogBuilder.setView(dialogView)
                    dialogBuilder.setCancelable(false)
                    val dialog = dialogBuilder.create()
                    dialog.show()

                    specificAppLinkRef.get().addOnSuccessListener { linkSnapshot ->
                        val downloadLink = linkSnapshot.value as? String
                        if (downloadLink != null) {
                            dialog.dismiss()
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(downloadLink))
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Download link not found for ${appInfo.name}", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to retrieve download link: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            adapter = AppListAdapter(this, appList)
            recyclerView.adapter = adapter

        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Failed to retrieve app links: ${exception.message}", Toast.LENGTH_SHORT).show()
        }

    }

    fun back(view: View?) {
        finish()
    }

    // Helper functions to map app names to icons and descriptions
    private fun getIconResIdForApp(appName: String): Int {
        return when (appName) {
            "SmashChat" -> R.drawable.smashchat_icon
            "SmashLight" -> R.drawable.smashlight_icon
            "SmashCalculator" -> R.drawable.smashcalculator_icon
            "Smash TV" -> R.drawable.smashtv_icon
            "SmashWeather" -> R.drawable.smashweather_icon
            "Electricity Bill Reader" -> R.drawable.electricitybillreader_icon
            "SmashDictionary" -> R.drawable.smashdictionary_icon
            "Smash Media Player" -> R.drawable.smashmediaplayer_icon
            else -> R.drawable.android_default_logo // Default icon if not found
        }
    }

    private fun getDescriptionForApp(appName: String): String {
        return when (appName) {
            "SmashChat" -> "A chat application."
            "SmashLight" -> "A flashlight app."
            "SmashCalculator" -> "A calculator app."
            "Smash TV" -> "A live TV app."
            "SmashWeather" -> "A weather app."
            "Electricity Bill Reader" -> "A Electricity Bill Reader app."
            "SmashDictionary" -> "A dictionary app."
            "Smash Media Player" -> "A Media Player app.\n "
            else -> "App description not available."
        }
    }

}