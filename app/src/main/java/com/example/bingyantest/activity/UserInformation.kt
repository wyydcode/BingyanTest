package com.example.bingyantest.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.bingyantest.R
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.objects.MyObjects
import java.io.File

class UserInformation : AppCompatActivity(){
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
        val image = findViewById<ImageView>(R.id.user_image)
        image.setImageURI(Uri.parse(MyObjects.user.imageuri))
        image.setOnClickListener{
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent,fromAlbum)
        }
        val takePhotoBtn = findViewById<ImageView>(R.id.takephotobtn)
        takePhotoBtn.setOnClickListener {
            // 创建File对象，用于存储拍照后的图片
            outputImage = File(externalCacheDir, "output_image.jpg")
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "com.example.bingyantest.fileprovider", outputImage)
            } else {
                Uri.fromFile(outputImage)
            }
            // 启动相机程序
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, takePhoto)
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
        }
    }
    companion object {
        fun actionStart(context: Context, account: String) {
            val intent = Intent(context,UserInformation::class.java)
            context.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val image = findViewById<ImageView>(R.id.user_image)
        when(requestCode){
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    val bitmap = BitmapFactory.decodeStream(contentResolver.
                    openInputStream(imageUri))
                    image.setImageBitmap(rotateIfRequired(bitmap))
                }
            }
            fromAlbum->{
                if(resultCode == Activity.RESULT_OK && data != null){
                    data.data?.let {
                        uri ->
                        val bitmap = getBitmapFromUri(uri)
                        image.setImageBitmap(bitmap)
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
    }
    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }
    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height,
            matrix, true)
        bitmap.recycle() // 将不再需要的Bitmap对象回收
        return rotatedBitmap
    }
    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

}