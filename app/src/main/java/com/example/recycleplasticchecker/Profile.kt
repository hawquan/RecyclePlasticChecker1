package com.example.recycleplasticchecker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {

    class User(val name: String, val email: String, val username: String, val password: String, val point: Int)

    lateinit var databaseReference : DatabaseReference
    lateinit var user : FirebaseUser
    lateinit var uid : String
    lateinit var editName: EditText
    lateinit var editEmail: EditText
    lateinit var editUsername: EditText
    lateinit var editPassword: EditText
    lateinit var editPoint: EditText
    lateinit var edit: Button
    lateinit var save: Button
    lateinit var editOldPassword: EditText
    lateinit var save1: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = FirebaseAuth.getInstance().currentUser!!
        uid = user.uid
        editName = activity!!.findViewById(R.id.editName)
        editEmail = activity!!.findViewById(R.id.editEmail)
        editUsername = activity!!.findViewById(R.id.editUsername)
        editPassword = activity!!.findViewById(R.id.editPassword)
        editPoint = activity!!.findViewById(R.id.editPoint)
        edit = activity!!.findViewById(R.id.edit)
        save = activity!!.findViewById(R.id.save)
        editOldPassword = activity!!.findViewById(R.id.editOldPassword)
        save1 = activity!!.findViewById(R.id.save1)

        editName.isEnabled = false
        editEmail.isEnabled = false
        editUsername.isEnabled = false
        editPassword.isEnabled = false
        editPoint.isEnabled = false
        save.isGone = true
        editOldPassword.isGone = true
        save1.isGone = true

        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child("Users").child(uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError : DatabaseError) {
                Toast.makeText(activity, "Network ERROR. Please check your connection", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var user_name : String = dataSnapshot.child("name").value.toString()
                var user_email : String = dataSnapshot.child("email").value.toString()
                var user_username : String = dataSnapshot.child("username").value.toString()
                var user_password : String = dataSnapshot.child("password").value.toString()
                var user_point: String = dataSnapshot.child("point").value.toString()

                editName.setText(user_name)
                editEmail.setText(user_email)
                editUsername.setText(user_username)
                editPassword.setText(user_password)
                editPoint.setText(user_point)

            }
        })

        edit.setOnClickListener(){
            editName.isEnabled = true
            editEmail.isEnabled = true
            editUsername.isEnabled = true
            editPassword.isEnabled = true
            save.isGone = false
            edit.isGone = true
        }

        save.setOnClickListener(){
            save()
        }

        save1.setOnClickListener(){
            save1()
        }
    }

    private fun save(){
        user = FirebaseAuth.getInstance().currentUser!!
        uid = user.uid
        val name = editName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val username = editUsername.text.toString().trim()
        val password = editPassword.text.toString().trim()
        val point = editPoint.text.toString().trim()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user_password = dataSnapshot.child("password").value.toString()

                if (!user_password.equals(password)) {
                    editOldPassword.isGone = false
                    save.isGone = true
                    save1.isGone = false
                    Toast.makeText(activity, "Enter the old password to change the new password", Toast.LENGTH_LONG).show()
                } else {
                    val user = User(name, email, username, password, point.toInt())
                    databaseReference.setValue(user)
                    Toast.makeText(activity, "Update Successfully", Toast.LENGTH_SHORT).show()
                    editName.isEnabled = false
                    editEmail.isEnabled = false
                    editUsername.isEnabled = false
                    editPassword.isEnabled = false
                    save.isGone = true
                    edit.isGone = false
                }
            }
        })
    }

    private fun save1(){
        user = FirebaseAuth.getInstance().currentUser!!
        uid = user.uid
        val name = editName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val username = editUsername.text.toString().trim()
        val password = editPassword.text.toString().trim()
        val point = editPoint.text.toString().trim()
        val oldPassword = editOldPassword.text.toString().trim()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user_password = dataSnapshot.child("password").value.toString()

                if (user_password.equals(oldPassword)) {
                    val user = User(name, email, username, password, point.toInt())
                    databaseReference.setValue(user)
                    Toast.makeText(activity, "Update Successfully", Toast.LENGTH_SHORT).show()
                    editName.isEnabled = false
                    editEmail.isEnabled = false
                    editUsername.isEnabled = false
                    editPassword.isEnabled = false
                    editOldPassword.isGone = true
                    save.isGone = true
                    save1.isGone = true
                    edit.isGone = false
                } else {
                    Toast.makeText(activity, "Old Password is wrong, Please enter again", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
