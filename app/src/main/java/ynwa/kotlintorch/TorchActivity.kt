package ynwa.kotlintorch

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_torch.*

class TorchActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private lateinit var mCameraId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torch)

        /*
         * Check if your device has Flash camera feature
         * when flash not available you notify user and finish the activity / kill the application
         */
        val isFlashAvailable: Boolean = applicationContext
                .packageManager
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        if (!isFlashAvailable) {
            showFlashNotAvailable()
        }

        /*
         * as a single device may have multiple camera, here we are returning the list of camera
         * using camera manager and we access the first we are seeing (which is mainly the main camera)
         */
        cameraManager = applicationContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            mCameraId = cameraManager.cameraIdList[0]
        } catch (exception: CameraAccessException) {
            exception.printStackTrace()
        }

        /*
         * use toggle button to change state of the CAMERA FLASH (ON/OFF)
         *
         * (and in Kotlin is suggested to use underscore _ for the unused variable parameter as
         * so that the name u should be using u can use it in another place is same function
         * that's why I am passing underscore in place of view parameter as I am not needing to use
         * view parameter)
         */
        toggleBtn.setOnCheckedChangeListener({ _, isChecked ->
            changeFlashState(isChecked)
        })
    }

    /*
     * Turning flash on using CameraManager and boolean state of toggle button
     * which passed as parameter (toggle state) to this function.
     */
    private fun changeFlashState(checked: Boolean) {
        try {
            cameraManager.setTorchMode(mCameraId, checked)
        } catch (exception: CameraAccessException) {
            exception.printStackTrace()
        }
    }

    /*
     * function that will display alert dialog that tells
     * user that his device has no flash functionality
     */

    private fun showFlashNotAvailable() {
        val alert = AlertDialog.Builder(this).create()
        alert.setTitle("Oops!")
        alert.setMessage("Flash not available in this device...")
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", { dialog, _ ->
            dialog.dismiss()
            finish()
        })
        alert.show()
    }
}
