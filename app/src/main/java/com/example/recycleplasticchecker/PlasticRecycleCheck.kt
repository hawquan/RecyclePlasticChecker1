package com.example.recycleplasticchecker


import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


private const val PICK_IMAGE = 1

/**
 * A simple [Fragment] subclass.
 */
class PlasticRecycleCheck : Fragment() {

    private lateinit var mAuth : FirebaseAuth
    lateinit var btnUpload: Button
    lateinit var btnChoose: Button
    lateinit var alertView: TextView

    var ImageList: ArrayList<Uri> = ArrayList<Uri>()
    private lateinit var ImageUri: Uri
    private lateinit var progressDialog: ProgressDialog
    private var upload_count: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plastic_recycle_check, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertView = activity!!.findViewById(R.id.alert)
        btnUpload = activity!!.findViewById(R.id.upload_image)
        btnChoose = activity!!.findViewById(R.id.chooser)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Image Upload Please Wait..............")

        btnChoose.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {
                var intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                startActivityForResult(intent, PICK_IMAGE)
            }
        })

        btnUpload.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {

                progressDialog.show()
                alertView.setText("If Loading Takes too long please Press the button again")
                var ImageFolder: StorageReference =
                    FirebaseStorage.getInstance().reference.child("ImageFolder")


                while (upload_count < ImageList.size) {

                    var IndividualImage: Uri = ImageList.get(upload_count)
                    var ImageName: StorageReference =
                        ImageFolder.child("Image" + IndividualImage.lastPathSegment)

                    ImageName.putFile(IndividualImage)
                        .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {

                            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                                ImageName.downloadUrl.addOnSuccessListener(object :
                                    OnSuccessListener<Uri> {
                                    override fun onSuccess(uri: Uri) {
                                        var url: String = uri.toString()
                                        StoreLink(url)
                                    }
                                })
                            }
                        })
                    upload_count++
                }
            }
        })
    }

    private fun StoreLink(url: String) {
        var databasereference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("UserOne").child(mAuth.currentUser!!.uid);
        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("Imglink", url)

        databasereference.push().setValue(hashMap)
        progressDialog.dismiss()
        alertView.setText("Image Uploaded Successfully")
        btnUpload.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {

                if (data!!.clipData != null) {

                    var countClipData: Int = data.clipData.itemCount

                    var currentImageSelect: Int = 0;
                    while (currentImageSelect < countClipData) {
                        ImageUri = data.clipData.getItemAt(currentImageSelect).uri
                        ImageList.add(ImageUri)
                        currentImageSelect += 1

                    }
                    alertView.visibility = View.VISIBLE
                    alertView.setText("You Have Selected " + ImageList.size + "Images")
                    btnChoose.visibility = View.GONE

                } else {

                    Toast.makeText(activity, "Please Select Multiple Image", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


}
