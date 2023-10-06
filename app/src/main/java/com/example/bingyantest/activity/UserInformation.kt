package com.example.bingyantest.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.bingyantest.R
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.objects.MyObjects
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.OutputStream

class UserInformation : BaseActivity(){
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PERMISSION_CAMERA = 2

    private lateinit var imageView: CircleImageView
    private lateinit var takePhotoBtn: ImageView
    val takePhoto = 1
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    val fromAlbum = 2
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_information)
        val name = findViewById<EditText>(R.id.user_name)
        val account = findViewById<EditText>(R.id.user_account)
        val email = findViewById<EditText>(R.id.user_email)
        imageView = findViewById<CircleImageView>(R.id.user_image)
        imageView.setImageURI(Uri.parse(MyObjects.user.imageuri))
        imageView.setOnClickListener{
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent,fromAlbum)
        }
        takePhotoBtn = findViewById<ImageView>(R.id.takephotobtn)
        takePhotoBtn.setOnClickListener {
            // 创建File对象，用于存储拍照后的图片
            capturePhoto()
        }
        name.setText(MyObjects.user.name)
        account.setText(MyObjects.userAccount)
        email.setText(MyObjects.user.email)
        val logout = findViewById<Button>(R.id.logout)
        val changeInformation = findViewById<Button>(R.id.change_information)
        changeInformation.setOnClickListener{
            MyObjects.user.email = email.text.toString()
            MyObjects.user.name = name.text.toString()
            val dbHelper = MyDatabaseHelper(this,"Users",1)
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("email",email.text.toString())
                put("name",name.text.toString())
            }
            db.update("Users",values,"account = ?", arrayOf("${MyObjects.userAccount}"))
        }
        logout.setOnClickListener{
            //退出登录
            ActivityCollector.finishAll()
            MyObjects.reset()
            LoginActivity.actionStart(this)
        }
    }


    private fun capturePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION_CAMERA
            )
        } else {
            launchCamera()
        }
    }

    private fun launchCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView.setImageURI(imageUri)
            saveImageToGallery()
            MyObjects.user.imageuri = imageUri.toString()
            val dbHelper = MyDatabaseHelper(this,"Users",1)
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("imageuri",imageUri.toString())
            }
            db.update("Users",values,"account = ?", arrayOf("${MyObjects.userAccount}"))
            Toast.makeText(this, "Saved to gallery!", Toast.LENGTH_SHORT).show()
        }
        if(requestCode==fromAlbum){
            if(resultCode == Activity.RESULT_OK && data != null){
                data.data?.let {
                        uri ->
                    val bitmap = getBitmapFromUri(uri)
                    imageView.setImageBitmap(bitmap)
                    MyObjects.user.imageuri = uri.toString()
                    val dbHelper = MyDatabaseHelper(this,"Users",1)
                    val db = dbHelper.writableDatabase
                    val values = ContentValues().apply {
                        put("imageuri",uri.toString())
                    }
                    db.update("Users",values,"account = ?", arrayOf("${MyObjects.userAccount}"))
                }
            }
        }
    }

    private fun saveImageToGallery() {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            var outStream: OutputStream? = null
            outStream = contentResolver.openOutputStream(imageUri!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            }
        }
    }
    companion object {
        fun actionStart(context: Context, account: String) {
            val intent = Intent(context,UserInformation::class.java)
            context.startActivity(intent)
        }
    }
}