package com.example.bingyantest.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bingyantest.MainActivity
import com.example.bingyantest.R
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.fragment.NoResultFragment
import com.example.bingyantest.fragment.RecycleViewFragment
import com.example.bingyantest.objects.Friend
import com.example.bingyantest.objects.Group
import com.example.bingyantest.objects.MyObjects
import com.example.bingyantest.objects.Users

class LoginActivity : BaseActivity() {

    private val PERMISSION_REQUEST_CODE = 1001
    private val PICK_IMAGE_REQUEST_CODE = 1002
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val isRemember = prefs.getBoolean("remember_password", false)
        val accountEdit = findViewById<EditText>(R.id.accountEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        val rememberPass = findViewById<CheckBox>(R.id.rememberPass)
        val login = findViewById<Button>(R.id.login)
        val register = findViewById<Button>(R.id.register)
        if (isRemember) {
            // 将账号和密码都设置到文本框中
            val account = prefs.getString("account", "")
            val password = prefs.getString("password", "")
            accountEdit.setText(account)
            passwordEdit.setText(password)
            rememberPass.isChecked = true
        }

        login.setOnClickListener {
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            // 如果账号是admin且密码是123456，就认为登录成功
            if (isLoginSucess(this, account, password)) {
                val editor = prefs.edit()
                if (rememberPass.isChecked) { // 检查复选框是否被选中
                    editor.putBoolean("remember_password", true)
                    editor.putString("account", account)
                    editor.putString("password", password)
                } else {
                    editor.clear()
                }
                editor.apply()
                //初始化信息
                MyObjects.initialize(this)
                MyObjects.user = MyObjects.refreshUser(account)
                MyObjects.userAccount = account
                MyObjects.friendslist.value = ArrayList()
                //MyObjects.add(Friend("friend", "friend", "friend", "friend"))
                MyObjects.load()
                //MyObjects.groupList.add(Group("联系人", MyObjects.friendslist.value!!))
                /*val db = MyObjects.dbHelper.writableDatabase
                var cursor = db.query("UnsolvedApplys", arrayOf("sender"),"receiver = ?",
                    arrayOf("$account"),null,null,null)
                val newFriendAccount = ArrayList<String>()
                if (cursor.moveToFirst()) {
                    do {
                        // 遍历Cursor对象，取出数据并打印
                        newFriendAccount.add(cursor.getString(cursor.getColumnIndexOrThrow("sender")))
                    } while (cursor.moveToNext())
                }
                cursor.close()
                MyObjects.usersList.forEach{
                    if(it.account in newFriendAccount ){
                        MyObjects.newFriendList.add(Friend(it.name,it.account,it.imageuri,it.email))
                    }
                }*/



                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this, "账号或密码错误",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        register.setOnClickListener {
            RegisterActivity.actionStart(this)
        }
    }

    fun isLoginSucess(context: Context, inputAccount: String, inputPassword: String): Boolean {
        val dbHelper = MyDatabaseHelper(context, "Users", 1)
        val db = dbHelper.writableDatabase
        val cursor = db.query("Users", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val account = cursor.getString(cursor.getColumnIndexOrThrow("account"))
                if (account != inputAccount) continue
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                if (password != inputPassword) {
                    return false
                }
                return true
            } while (cursor.moveToNext())
        } else {
            return false
        }
        cursor.close()
        return false
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功
                } else {
                    // 授权失败
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
                    }
                    Toast.makeText(this, "需要读取外部存储权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}