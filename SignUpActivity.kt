package com.example.adminwaveoffood

import android.accounts.Account
import android.content.Intent
import android.nfc.tech.TagTechnology
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivitySignUpBinding
import com.example.adminwaveoffood.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database

class SignUpActivity : AppCompatActivity() {
    private lateinit var email : String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var nameOfRestaurant : String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference



    private val binding : ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // initializatio Firebase Auth
        auth = Firebase.auth

        // intialization Firebase database
        database = Firebase.database.reference

        binding.createUserButton.setOnClickListener{
            // get text from edittext
            userName=binding.name.text.toString().trim()
            nameOfRestaurant=binding.restaurantName.toString().trim()
            email=binding.emailOrPhone.text.toString().trim()
            password=binding.passsword.text.toString().trim()

            if(userName.isBlank()||nameOfRestaurant.isBlank()||email.isBlank()||password.isBlank()){
                Toast.makeText(this, "Please Fill all details", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }

        }
        binding.alreadyHaveAccountButton.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val locationList = arrayOf("Jaipur","Odisha","Bihar","Sikar")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
            if(task.isSuccessful)
            {

                Toast.makeText(this, "Account created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure",task.exception)
            }
        }
    }
    // save data in database
    private fun saveUserData() {
        userName=binding.name.text.toString().trim()
        nameOfRestaurant=binding.restaurantName.text.toString().trim()
        email=binding.emailOrPhone.text.toString().trim()
        password=binding.passsword.text.toString().trim()
        val user = UserModel(userName,nameOfRestaurant,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        // save user data firebase database
        database.child("user").child(userId).setValue(user)
    }
}