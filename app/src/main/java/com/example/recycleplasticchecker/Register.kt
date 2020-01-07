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
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 */

class Register : Fragment() {

    class Account(val name: String, val email: String, val username: String, val password: String)

    lateinit var editText: EditText
    lateinit var editText2: EditText
    lateinit var editText3: EditText
    lateinit var editText4: EditText
    lateinit var editText5: EditText
    lateinit var button: Button
    lateinit var textView2: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = activity!!.findViewById(R.id.editText)
        editText2 = activity!!.findViewById(R.id.editText2)
        editText3 = activity!!.findViewById(R.id.editText3)
        editText4 = activity!!.findViewById(R.id.editText4)
        editText5 = activity!!.findViewById(R.id.editText5)
        button = activity!!.findViewById(R.id.button)
        textView2 = activity!!.findViewById(R.id.textView2)


        button.setOnClickListener{
            register()
            view.findNavController().navigate(R.id.action_register_to_login)
        }

        textView2.setOnClickListener{
            view.findNavController().navigate(R.id.action_register_to_login)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    private fun register() {
        val name = editText.text.toString().trim()
        val email = editText2.text.toString().trim()
        val username = editText3.text.toString().trim()
        val password = editText4.text.toString().trim()
        val cpassword = editText5.text.toString().trim()

        if (name.isEmpty() && email.isEmpty() && username.isEmpty() && password.isEmpty() && cpassword.isEmpty()) {
            editText.error = "Please enter a name"
            editText2.error = "Please enter an email"
            editText3.error = "Please enter a username"
            editText4.error = "Please enter a password"
            editText5.error = "Please enter a confirm password"
            return
        }

        if (password != cpassword) {
            editText5.error = "The password is not same"
            return
        }

        val database = FirebaseDatabase.getInstance().getReference("Account")
        val id = database.push().key

        val account = Account(name, email, username, password)

        database.child(id.toString()).setValue(account).addOnCompleteListener {
            Toast.makeText(activity, "Register Successfully", Toast.LENGTH_SHORT).show()
        }
    }
}
