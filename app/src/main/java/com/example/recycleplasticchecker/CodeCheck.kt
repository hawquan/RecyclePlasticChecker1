package com.example.recycleplasticchecker


import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.example.recycleplasticchecker.Helper.InternetCheck
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.firebase.ml.vision.label. FirebaseVisionOnDeviceImageLabelerOptions
import com.wonderkiln.camerakit.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_code_check.*


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

        btn_FindSymbol.setOnClickListener{
            view.findNavController().navigate(R.id.action_codeCheck_to_codeCheck_2)
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
                    val options = FirebaseVisionCloudImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.8f)
                        .build()

                    val detector: FirebaseVisionImageLabeler = FirebaseVision.getInstance().getCloudImageLabeler(options)
                    val task : Task<List<FirebaseVisionImageLabel>> = detector.processImage(image)
                    task
                        .addOnFailureListener{e -> Log.d("ERROR",e.message)}
                        .addOnSuccessListener{ result -> processResultFromCloud(result) }

                }
                else{
                    //use on Device
                    val options = FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.8f) //Get highest result
                        .build()

                    val detector: FirebaseVisionImageLabeler = FirebaseVision.getInstance().getOnDeviceImageLabeler(options)

                    detector.processImage(image)
                        .addOnFailureListener{e -> Log.d("ERROR",e.message)}
                        .addOnSuccessListener { result -> processResultFromDevice(result) }

                }
            }

        })
    }

    private fun processResultFromDevice(result: List<FirebaseVisionImageLabel>) {
        for(label:FirebaseVisionImageLabel in result)
            Toast.makeText(activity,"Device result: " +label.text,Toast.LENGTH_SHORT).show()
        waitingDialog.dismiss()
    }

    private fun processResultFromCloud(result: List<FirebaseVisionImageLabel>) {
        for(label:FirebaseVisionImageLabel in result)
            Toast.makeText(activity,"Cloud result: " +label.text,Toast.LENGTH_SHORT).show()
        waitingDialog.dismiss()
    }


}






