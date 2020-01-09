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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 */

class Register : Fragment() {

    class User(val name: String, val email: String, val username: String, val password: String, val point: Int)

    lateinit var editName: EditText
    lateinit var editEmail: EditText
    lateinit var editUsername: EditText
    lateinit var editPassword: EditText
    lateinit var editConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var  progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editName = activity!!.findViewById(R.id.txtName)
        editEmail = activity!!.findViewById(R.id.editEmail)
        editUsername = activity!!.findViewById(R.id.editUsername)
        editPassword = activity!!.findViewById(R.id.editPassword)
        editConfirmPassword = activity!!.findViewById(R.id.editConfirmPassword)
        btnRegister = activity!!.findViewById(R.id.btn_register)
        progressBar = activity!!.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE


        btnRegister.setOnClickListener{
            register()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        mAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onStart() {
        super.onStart()

        if(mAuth.currentUser != null){
            //handle the already login user

        }
    }

    private fun register() {
        val name = editName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val username = editUsername.text.toString().trim()
        val password = editPassword.text.toString().trim()
        val cpassword = editConfirmPassword.text.toString().trim()
        val point = 0

        if(name.isEmpty() && email.isEmpty() && username.isEmpty() && password.isEmpty() && cpassword.isEmpty()) {
            editName.error = "Please enter a name"
            editEmail.error = "Please enter an email"
            editUsername.error = "Please enter a username"
            editPassword.error = "Please enter a password"
            editConfirmPassword.error = "Please enter a confirm password"
            return
        }

        if(name.isEmpty()){
            editName.error = "Please enter a name"
            return
        }

        if(email.isEmpty()){
            editEmail.error = "Please enter an email"
            return
        }

        if(username.isEmpty()){
            editUsername.error = "Please enter a username"
            return
        }

        if(password.isEmpty()){
            editPassword.error = "Please enter a password"
            return
        }

        if(cpassword.isEmpty()){
            editConfirmPassword.error = "Please enter a confirm password"
            return
        }

        if(password != cpassword) {
            editConfirmPassword.error = "The password is not same"
            return
        }

        progressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(object: OnCompleteListener<AuthResult>{
                override fun onComplete(task: Task<AuthResult>) {
                    progressBar.visibility = View.GONE
                    if(task.isSuccessful) {
                        //store additional fields in firebase database

                        var user : User = User(name,email,username,password,point)
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .setValue(user).addOnCompleteListener(object: OnCompleteListener<Void>{
                                override fun onComplete(task: Task<Void>){
                                    if(task.isSuccessful){
                                        Toast.makeText(activity,getString(R.string.registeration_success), Toast.LENGTH_LONG).show()

                                        view!!.findNavController().navigate(R.id.action_register_to_login)
                                    }
                                    else{
                                        //display a failure message
                                        if(task.exception is FirebaseAuthUserCollisionException){
                                            Toast.makeText(activity, "You are already registered", Toast.LENGTH_SHORT).show()
                                        }else{
                                            Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_SHORT).show()
                                        }


                                    }

                                }

                            })
                    }else{
                        Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }
            })

    }
}
