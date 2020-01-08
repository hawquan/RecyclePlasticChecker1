package com.example.recycleplasticchecker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */

class  Login : Fragment() {

    class Account(val name: String = "", val email: String = "", val username: String = "", val password: String = "")

    lateinit var editUsername: EditText
    lateinit var editPassword: EditText
    lateinit var btnLogin: Button
    lateinit var linkRegisterPage: TextView
    lateinit var mAuth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editUsername = activity!!.findViewById(R.id.editUsername)
        editPassword= activity!!.findViewById(R.id.editPassword)
        btnLogin = activity!!.findViewById(R.id.btnLogin)
        linkRegisterPage = activity!!.findViewById(R.id.linkRegisterPage)

        btnLogin.setOnClickListener() {
            login()
        }

        linkRegisterPage.setOnClickListener {
            view.findNavController().navigate(R.id.action_login_to_register)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun login() {
        val username = editUsername.text.toString().trim()
        val password = editPassword.text.toString().trim()

        if (username.isEmpty() && password.isEmpty()) {
            editUsername.error = "Please enter username"
            editPassword.error = "Please enter password"
            return
        }


        if(!isEmailValid(username)) {
            //Login with username and password
            val database = FirebaseDatabase.getInstance().getReference()

            database.child("Users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (h in dataSnapshot.children) {
                        val account = h.getValue(Account::class.java)
                        val username1 = account!!.username
                        val password1 = account.password

                        if (username.equals(username1) && password.equals(password1)) {
                            Toast.makeText(activity, "Login Successfully", Toast.LENGTH_SHORT).show()
                            view!!.findNavController().navigate(R.id.action_login_to_home)
                        } else {
                            Toast.makeText(
                                activity,
                                "Invalid Username or password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }
            })
        }else{
            //Login with email and password, firebase authentication
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(object: OnCompleteListener<AuthResult>{
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful){
                        Toast.makeText(activity, "Login Successfully", Toast.LENGTH_SHORT).show()
                        view!!.findNavController().navigate(R.id.action_login_to_home)

                    }else{
                        Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
