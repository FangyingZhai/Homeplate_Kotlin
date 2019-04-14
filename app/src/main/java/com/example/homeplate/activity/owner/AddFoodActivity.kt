package com.example.homeplate.activity.owner

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.homeplate.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_food.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddFoodActivity : AppCompatActivity(), View.OnClickListener {
    var currentPhotoPath: String = ""
    var REQUEST_IMAGE_CAPTURE: Int = 1
    var REQUEST_LOAD_IMAGE: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        cameraButton.setOnClickListener(this)
        viewPhotoButton.setOnClickListener(this)

        submit.setOnClickListener {
            try {
                val name = dish_name.text.toString().replace("\\d".toRegex(), "").trim()
                val value = price.text.toString()
                if(name.isEmpty() || value.isEmpty()) {
                    Toast.makeText(it.context, "Please enter valid input", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(name.toIntOrNull() != null) {
                    Toast.makeText(it.context, "Please enter valid name", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // Pseudocode
                // firebase cloud storage -> add image to fire
                //  successlistener:
                //      // maybe manipulate data...
                //      firestore.update to update object with image uriString returned from firebase cloud storage add

                //send data back to main activity.
                setResult(
                    Activity.RESULT_OK, Intent()
                        .putExtra("name", name)
                        .putExtra("value", value.toInt()))

                finish()
            }
            catch (e : Exception) {
                Toast.makeText(it.context, "Please enter valid input", Toast.LENGTH_SHORT).show()
            }
        }

        val returnUri = Uri.parse(intent.extras.getString("uri"))

        val bitmapImage = MediaStore.Images.Media.getBitmap(this.contentResolver, returnUri)
        img.setImageBitmap(bitmapImage)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.cameraButton -> {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager).also {
                        val photoFile : File? = try {
                            createImageFile()
                        } catch (ex: IOException) {
                            Log.d("ERROR:", "Could not get photo file")
                            null
                        }

                        photoFile?.also {
                            val photoURI = FileProvider.getUriForFile(
                                this,
                                "com.example.cse438.imageprocessor.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }

                }
            }
            R.id.viewPhotoButton -> {
                val i = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

                startActivityForResult(i, REQUEST_LOAD_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic()
        }

        if(requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK) {
            val returnUri = data?.data

            Picasso.with(this).load(returnUri).into(img)

        }

    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_$timeStamp",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

}
