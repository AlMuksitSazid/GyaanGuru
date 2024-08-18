@file:Suppress("PackageName")

package com.gyaanguru.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDex
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gyaanguru.Constants
import com.gyaanguru.Fragments.ChataiFragment
import com.gyaanguru.Fragments.HomeFragment
import com.gyaanguru.Fragments.QuizzesFragment
import com.gyaanguru.R
import com.gyaanguru.databinding.ActivityMainBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var bottomMenu: ChipNavigationBar
    private lateinit var frameLayout: FrameLayout
    private lateinit var profileImage: CircleImageView
    private lateinit var userNameTxt: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.navy_blue)))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        MultiDex.install(this)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userRef = firebaseDatabase.getReference()
        bottomMenu = findViewById<ChipNavigationBar>(R.id.bottom_menu)
        frameLayout = findViewById<FrameLayout>(R.id.fragment_container)
        frameLayout.setBackgroundColor(Color.parseColor("#C0DCEC"))
        profileImage = findViewById<CircleImageView>(R.id.profileImage)
        userNameTxt = findViewById<TextView>(R.id.userNameTxt)

        val window:Window=this@MainActivity.window
        window.statusBarColor=ContextCompat.getColor(this@MainActivity, R.color.navy_blue)

     // Read the username and userEmail from the database
        userRef = firebaseDatabase.getReference("Users").child(firebaseAuth.currentUser!!.uid)
        Log.d("UserRef", userRef.toString())
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userName = dataSnapshot.child("userName").getValue(String::class.java)
                userNameTxt.text = userName ?: "User"

                val profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String::class.java)
                if (profileImageUrl != null) {
                    // Load the image into the CircleImageView
                    loadProfileImage(profileImageUrl)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors
                Log.e("FirebaseError", "Error reading username: ${error.message}")
            }
        })

        binding.apply {
            bottomMenu.setItemSelected(R.id.bottom_home)
            replaceFragment(HomeFragment())
            bottomMenu.setOnItemSelectedListener {
                if (it == R.id.bottom_quizzes) {
                replaceFragment(QuizzesFragment())
     //           frameLayout.setBackgroundColor(Color.parseColor("#ECE3F0"))
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.dark_pink)))
                window.statusBarColor=ContextCompat.getColor(this@MainActivity, R.color.dark_pink)
                }
                else if (it == R.id.bottom_chatAI) {
                    replaceFragment(ChataiFragment())
     //               frameLayout.setBackgroundColor(Color.parseColor("#D1E2C4"))
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.dark_green)))
                    window.statusBarColor=ContextCompat.getColor(this@MainActivity, R.color.dark_green)
                }
                else if (it == R.id.bottom_home) {
                    replaceFragment(HomeFragment())
     //               frameLayout.setBackgroundColor(Color.parseColor("#C0DCEC"))
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.navy_blue)))
                    window.statusBarColor=ContextCompat.getColor(this@MainActivity, R.color.navy_blue)
                }
                return@setOnItemSelectedListener
            }
            profileImage.setOnClickListener {
                val intent: Intent = Intent(this@MainActivity, ProfileActivity::class.java)
                intent.putExtra("userName", userNameTxt.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun loadProfileImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .into(profileImage)
        // Or, using Picasso:
        // Picasso.get().load(imageUrl).into(profileImageView)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    //Main Menu
    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)

            // Tint individual menu items based on theme
            for (i in 0 until menu.size()) {
                val menuItem = menu.getItem(i)
                val icon = menuItem.icon
                if (icon != null) {
                    val tintColor = if (isDarkTheme()) {
                        ContextCompat.getColor(this, R.color.white) // Or your desired dark mode color

                    } else {
                        ContextCompat.getColor(this, R.color.black) // Or your desired light mode color
                    }
                    icon.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
                }
            }
        }
        binding.toolbar.overflowIcon?.let { overflowIcon ->
            val lightColor= ContextCompat.getColor(this, R.color.light_grey)
            overflowIcon.setColorFilter(lightColor, PorterDuff.Mode.SRC_IN)
            val settingsItem = menu?.findItem(R.id.settings)
            settingsItem?.icon?.setColorFilter(lightColor, PorterDuff.Mode.SRC_IN)
        }
        return true
    }

    private fun isDarkTheme(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
          R.id.about -> {
              val intent: Intent = Intent(this@MainActivity, AboutActivity::class.java)
              startActivity(intent)
              true
            }
            R.id.privacy_policy -> {
                val intent: Intent = Intent(this@MainActivity, PrivacyActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.faq -> {
                AlertDialog.Builder(this).apply {
                    setTitle("FAQ")
                    setMessage("Please, Contact us manually. FAQ feature will come soon.")
                    setNeutralButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
                true
            }
            R.id.rate_us -> {
                Toast.makeText(this, "Rating feature will be available soon", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.more_apps -> {
                val intent: Intent = Intent(this@MainActivity, MoreAppsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.share -> {
                val appLinkRef = firebaseDatabase.getReference("AppLink").child("GyaanGuru")
                appLinkRef.get().addOnSuccessListener{ dataSnapshot ->
                    val appLink = dataSnapshot.value as? String
                    if (appLink != null) { // Check if appLink is not null
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Check out GyaanGuru!")
                            putExtra(Intent.EXTRA_TEXT, "I'm loving GyaanGuru app! Download it from here: $appLink")
                        }
                        startActivity(Intent.createChooser(shareIntent, "Share via"))
                    }else {
                        // Handle the case where appLink is null (e.g., show a Toast message)
                        Toast.makeText(this, "App link not found in database.", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to retrieve app link: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.check_updates -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Check Updates")
                    setMessage("No update available. You are using the latest version.")
                    setNeutralButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
                true
            }
            R.id.remove_ads -> {
                Toast.makeText(this, "Contact us to remove ads", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.settings -> {
                Toast.makeText(this, "Settings will come soon", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Confirmation")
            setMessage("Are you sure want to exit the application?")
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { _, _ ->
                finishAffinity()
            }
            create()
            show()
        }
    }
}

