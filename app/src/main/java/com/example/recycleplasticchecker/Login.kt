package com.example.recycleplasticchecker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */

class Login : Fragment() {

    class Account(val name: String = "", val email: String = "", val username: String = "", val password: String = "")

    lateinit var editText: EditText
    lateinit var editText2: EditText
    lateinit var button: Button
    lateinit var textView2: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = activity!!.findViewById(R.id.editText)
        editText2 = activity!!.findViewById(R.id.editText2)
        button = activity!!.findViewById(R.id.button)
        textView2 = activity!!.findViewById(R.id.textView2)

        button.setOnClickListener() {
            login()
        }

        textView2.setOnClickListener {
            view.findNavController().navigate(R.id.action_login_to_register)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun login() {
        val username = editText.text.toString().trim()
        val password = editText2.text.toString().trim()

        if (username.isEmpty() && password.isEmpty()) {
            editText.error = "Please enter username"
            editText2.error = "Please enter password"
            return
        }

        val database = FirebaseDatabase.getInstance().getReference()

        database.child("Account").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(h in dataSnapshot.children) {
                    val account = h.getValue(Account::class.java)
                    val username1 = account!!.username
                    val password1 = account.password

                    if (username.equals(username1) && password.equals(password1)) {
                        Toast.makeText(activity, "Login Successfully", Toast.LENGTH_SHORT).show()
                        view!!.findNavController().navigate(R.id.action_login_to_profile)
                    } else {
                        Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }

        })
    }
}
