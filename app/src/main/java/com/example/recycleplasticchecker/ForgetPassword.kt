package com.example.recycleplasticchecker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class ForgetPassword : Fragment() {
    class Users(val name: String, val email: String, val username: String, val password: String, val point: Int)

    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var sendEmail: Button
    lateinit var login: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editEmail = activity!!.findViewById(R.id.editEmail)
        editPassword = activity!!.findViewById(R.id.editPassword)
        sendEmail = activity!!.findViewById(R.id.sendEmail)
        login = activity!!.findViewById(R.id.login)

        editPassword.isEnabled = false
        login.isEnabled = false

        sendEmail.setOnClickListener() {
            sendEmail()
        }

        login.setOnClickListener() {
            resetPassword()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    private fun sendEmail() {
        val email = editEmail.text.toString().trim()

        if (email.isEmpty()) {
            editEmail.error = "Please enter an email"
            return
        }

        val mAuth = FirebaseAuth.getInstance()

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(task: Task<Void>) {
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Go to your inbox to complete reset password", Toast.LENGTH_LONG).show()
                        editEmail.isEnabled = false
                        sendEmail.isEnabled = false
                        editPassword.isEnabled = true
                        login.isEnabled = true
                    } else {
                        Toast.makeText(activity, "Your email address is invalid", Toast.LENGTH_LONG).show()
                    }
                }
        })
    }

    private fun resetPassword() {
        val email = editEmail.text.toString().trim()
        val password = editPassword.text.toString().trim()

        if (password.isEmpty()) {
            editPassword.error = "Please enter new password"
            return
        }

        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Reset Password Successfully", Toast.LENGTH_SHORT).show()
                        view!!.findNavController().navigate(R.id.action_forgetPassword_to_login)

                        val database = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.currentUser!!.uid)

                        database.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(databaseError: DatabaseError) {

                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                var user_name = dataSnapshot.child("name").value.toString()
                                var user_username = dataSnapshot.child("username").value.toString()
                                var user_point = dataSnapshot.child("point").value.toString()

                                val user = Users(user_name, email, user_username, password, user_point.toInt())
                                database.setValue(user)
                            }
                        })
                    } else {
                        Toast.makeText(activity, "Invalid new password", Toast.LENGTH_SHORT).show()
                    }
                }
        })
    }
}
