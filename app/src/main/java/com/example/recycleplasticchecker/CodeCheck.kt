package com.example.recycleplasticchecker


import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.graphics.green
import com.example.recycleplasticchecker.Helper.InternetCheck
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import com.wonderkiln.camerakit.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_code_check.*
import kotlinx.android.synthetic.main.fragment_code_check.view.*


/**
 * A simple [Fragment] subclass.
 */

class CodeCheck : Fragment() {


    lateinit var waitingDialog: AlertDialog

    override fun onResume(){
        super.onResume()
        camera_view.start()
    }

    override fun onPause(){
        super.onPause()
        camera_view.stop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        waitingDialog = SpotsDialog.Builder()
            .setContext(activity)
            .setCancelable(false)
            .setMessage("Please waiting...")
            .build()


        btn_detect.setOnClickListener{

            camera_view.start()
            camera_view.captureImage()
        }

        camera_view.addCameraKitListener(object: CameraKitEventListener{
            override fun onVideo(p0: CameraKitVideo?) {
            }

            override fun onEvent(p0: CameraKitEvent?) {
            }

            override fun onImage(p0: CameraKitImage?) {
                waitingDialog.show()

                var bitmap : Bitmap = p0!!.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, camera_view.width, camera_view.height, false)
                camera_view.stop()
                runDetector(bitmap)
            }

            override fun onError(p0: CameraKitError?) {
            }

        })

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_code_check, container, false)
        }




    private fun runDetector(bitmap: Bitmap?) {
        val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap!!)

        InternetCheck(object:InternetCheck.Consumer{
            override fun accept(isConnected: Boolean?) {
                if(isConnected!!){
                    //use Cloud Detector
                    val options : FirebaseVisionCloudDetectorOptions = FirebaseVisionCloudDetectorOptions.Builder()
                        .setMaxResults(1) //Get highest result
                        .build()

                    val detector: FirebaseVisionCloudLabelDetector = FirebaseVision.getInstance().getVisionCloudLabelDetector(options)
                    val task : Task<List<FirebaseVisionCloudLabel>> = detector.detectInImage(image)
                    task
                        .addOnFailureListener{e -> Log.d("ERROR",e.message)}
                        .addOnSuccessListener{ result -> processResultFromCloud(result) }

                }
                else{
                    //use on Device
                    val options = FirebaseVisionLabelDetectorOptions.Builder()
                        .setConfidenceThreshold(0.8f) //Get highest result
                        .build()

                    val detector: FirebaseVisionLabelDetector = FirebaseVision.getInstance().getVisionLabelDetector(options)

                    detector.detectInImage(image)
                        .addOnFailureListener{e -> Log.d("ERROR",e.message)}
                        .addOnSuccessListener { result -> processResultFromDevice(result) }

                }
            }

        })
    }

    private fun processResultFromDevice(result: List<FirebaseVisionLabel>) {
        for(label:FirebaseVisionLabel in result)
            Toast.makeText(activity,"Device result: " +label.label,Toast.LENGTH_SHORT).show()
        waitingDialog.dismiss()
    }

    private fun processResultFromCloud(result: List<FirebaseVisionCloudLabel>) {
        //Toast.makeText(activity,"hahaha: ",Toast.LENGTH_SHORT).show()
        for(label:FirebaseVisionCloudLabel in result)
            Toast.makeText(activity,"Cloud result: " +label.label,Toast.LENGTH_SHORT).show()
        waitingDialog.dismiss()
    }


}


