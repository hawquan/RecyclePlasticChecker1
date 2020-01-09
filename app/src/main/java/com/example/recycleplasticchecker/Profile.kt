package com.example.recycleplasticchecker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {


    lateinit var databaseReference : DatabaseReference
    lateinit var  user : FirebaseUser
    lateinit var itemlist : MutableList<String>
    lateinit var uid : String

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
        itemlist = ArrayList()


        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child("Users").child(uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError : DatabaseError) {
                Toast.makeText(activity, "Network ERROR. Please check your connection", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                itemlist.clear()

                var user_name : String = dataSnapshot.child("name").value.toString()
                var user_email : String = dataSnapshot.child("email").value.toString()
                var user_point: String = dataSnapshot.child("point").value.toString()
                var user_username : String = dataSnapshot.child("username").value.toString()

                activity!!.findViewById<EditText>(R.id.nameEdit).setText(user_name)
                activity!!.findViewById<EditText>(R.id.usernameEdit).setText(user_username)
                activity!!.findViewById<EditText>(R.id.emailEdit).setText(user_email)
                activity!!.findViewById<EditText>(R.id.pointEdit).setText(user_point)
            }


        })
    }


}
