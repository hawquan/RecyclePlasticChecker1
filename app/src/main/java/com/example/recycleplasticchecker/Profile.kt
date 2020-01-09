package com.example.recycleplasticchecker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {

    class Users(val name: String, val email: String, val username: String, val password: String, val point: Int)

    lateinit var databaseReference : DatabaseReference
    lateinit var user : FirebaseUser
    lateinit var uid : String
    lateinit var editName: EditText
    lateinit var editEmail: EditText
    lateinit var editUsername: EditText
    lateinit var editPassword: EditText
    lateinit var editPoint: EditText
    lateinit var editbutton: Button
    lateinit var save: Button

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
        editbutton = activity!!.findViewById(R.id.editbutton)
        save = activity!!.findViewById(R.id.save)

        editName.isEnabled = false
        editEmail.isEnabled = false
        editUsername.isEnabled = false
        editPassword.isEnabled = false
        editPoint.isEnabled = false
        save.isGone = true

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
                editPassword.setText((user_password))
                editPoint.setText(user_point)
            }
        })

        editbutton.setOnClickListener(){
            editName.isEnabled = true
            editEmail.isEnabled = true
            editUsername.isEnabled = true
            editPassword.isEnabled = true
            editbutton.isGone = true
            save.isGone = false
        }

        save.setOnClickListener(){
            save()
        }
    }

    private fun save(){
        val mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.currentUser!!.uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user_name = dataSnapshot.child("name").value.toString()
                var user_username = dataSnapshot.child("username").value.toString()
                var user_point = dataSnapshot.child("point").value.toString()

                val user = Users(user_name, email, user_username, password, user_point.toInt())
                databaseReference.setValue(user)
            }
        })
    }
}
