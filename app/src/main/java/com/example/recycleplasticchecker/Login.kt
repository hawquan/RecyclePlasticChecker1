package com.example.recycleplasticchecker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {

    class Account(val name: String, val email: String, val username: String, val password: String)

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

        button.setOnClickListener(){
            login();
            view.findNavController().navigate(R.id.action_login_to_profile)
        }

        textView2.setOnClickListener{
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
        val username = editText.toString().trim()
        val password = editText2.toString().trim()

        if (username.isEmpty() && password.isEmpty()) {
            editText.error = "Please enter username"
            editText2.error = "Please enter password"
            return
        }

        val database = FirebaseDatabase.getInstance().getReference("Account")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    for(h in p0.children){
                        val account = h.getValue(Account::class.java)
                    }
                }

            }

        })

    }
}
