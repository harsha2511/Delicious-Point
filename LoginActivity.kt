package com.example.adminwaveoffood

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivityLoginBinding
import com.example.adminwaveoffood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient



    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        // initialize Firebase Auth
        auth = Firebase.auth
        // initialize Firebase database
        database = Firebase.database.reference
        // INITIALIZE google sign in
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginButton.setOnClickListener{
            // get text from edit text
            email=binding.emailLogin.text.toString().trim()
            password=binding.passwordLogin.text.toString().trim()

            if(email.isBlank()||password.isBlank())
            {
                Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show()
            }else{
                createUserAccount(email,password)
            }

        }
        binding.googleButton.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
        binding.dontHaveAccountButton.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if(task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                updateUi(user)
            }
            else
            {
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
                    if(task.isSuccessful){
                        val user=auth.currentUser
                        Toast.makeText(
                            this,
                            "Create User and Login Successfully", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        updateUi(user)
                    }
                    else{
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account","createUserAccount: Authentication failed",task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        // get text from edit text

        email=binding.emailLogin.text.toString().trim()
        password=binding.passwordLogin.text.toString().trim()

        val user = UserModel(userName,nameOfRestaurant,email,password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{authTask->
                    if(authTask.isSuccessful){
                        //successfully sign in with Google
                        Toast.makeText(this,"Successfully sign in with Google",Toast.LENGTH_SHORT).show()
                        updateUi(null)
                    }else{
                        Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}