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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_food.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddFoodActivity : AppCompatActivity(), View.OnClickListener {
    var currentPhotoPath: String = ""
    var REQUEST_IMAGE_CAPTURE: Int = 1
    var REQUEST_LOAD_IMAGE: Int = 2

    private lateinit var auth: FirebaseAuth
    private lateinit var photoUri: Uri
    private lateinit var photoRef: StorageReference
    private lateinit var db: FirebaseFirestore
    private lateinit var email: String
    private lateinit var menuList: ArrayList<String>
    private var photoSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        //firebase setup
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        email = auth.currentUser!!.email!!
        //get menu list
        getMenu()
        //button setup
        cameraButton.setOnClickListener(this)
        viewPhotoButton.setOnClickListener(this)
        //submit button
        submit.setOnClickListener {
//            val name = dish_name.text.toString().replace("\\d".toRegex(), "").trim()
            val name = dish_name.text.toString()
            val value = price.text.toString()
            //check for empty values
            if(name.isEmpty() || value.isEmpty()) {
                Toast.makeText(this, "Please enter valid input", Toast.LENGTH_SHORT).show()
            }
            else if(name.toIntOrNull() != null) {
                Toast.makeText(this, "Please enter valid name", Toast.LENGTH_SHORT).show()
            }
            else if(!photoSelected) {
                Toast.makeText(this, "Please select photo", Toast.LENGTH_SHORT).show()
            }
            //check if dish name already exists
            else if(inMenu(name)) {
                Toast.makeText(this, "Dish of selected name already exists", Toast.LENGTH_SHORT).show()
            }
            //firebase cloud storage -> add image to fire
            else {
                val storageRef = FirebaseStorage.getInstance().getReference()
                photoRef = storageRef.child("/photos/"+email+"/"+name)
                photoRef.putFile(photoUri).addOnSuccessListener {
                    Toast.makeText(this, "Photo Uploaded.", Toast.LENGTH_SHORT).show()
                    var dish = HashMap<String, Any>()
                    dish.put("name", name)
                    dish.put("price", value)
                    db.collection("owners").document(email).collection("menu").document(name).set(dish)
                    finish()
                }
            }
        }
        //cancel button
        cancel.setOnClickListener {
            finish()
        }

        //val returnUri = Uri.parse(intent.extras.getString("uri"))

        //val bitmapImage = MediaStore.Images.Media.getBitmap(this.contentResolver, returnUri)
        //img.setImageBitmap(bitmapImage)
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
                                "com.example.homeplate",
                                it
                            )
                            photoUri = photoURI
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }

                }
            }
            R.id.viewPhotoButton -> {
                val i = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                )

                startActivityForResult(i, REQUEST_LOAD_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Picasso.with(this).load(photoUri).into(img)
            photoSelected = true
        }

        if(requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK) {
            photoUri = data?.data!!
            Picasso.with(this).load(photoUri).into(img)
            photoSelected = true
        }

    }
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_$timeStamp",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
    private fun getMenu() {
        menuList = ArrayList<String>()
        db.collection("owners").document(email).collection("menu")
            .get().addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val name = document.id.toString()
                        menuList.add(name)
                    }
                }
                else {
                    Log.d("DEBUG", "COULDN'T GET MENU LIST")
                }
        }
    }
    private fun inMenu(name: String) : Boolean {
        for (dish in menuList) {
            if (name == dish) {
                return true
            }
        }
        return false
    }
}
