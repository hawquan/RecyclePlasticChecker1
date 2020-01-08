package com.example.recycleplasticchecker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {

    class Account(val name: String = "", val email: String = "", val username: String = "", val password: String = "", val point: Int)

    lateinit var li : ListView
    lateinit var adapter : ArrayAdapter<String>
    lateinit var databaseReference : DatabaseReference
    lateinit var  user : FirebaseUser
    lateinit var  info : UserInfo
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

        li = activity!!.findViewById(R.id.listView)
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

                user
                itemlist.add("Name          : $user_name")
                itemlist.add("Email         : $user_email")
                itemlist.add("username      : $user_username")
                itemlist.add("Point Earned  : $user_point")

                adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, itemlist)
                li.adapter = adapter
            }


        })
    }


}
