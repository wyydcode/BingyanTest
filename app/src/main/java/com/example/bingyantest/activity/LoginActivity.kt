package com.example.bingyantest.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.bingyantest.MainActivity
import com.example.bingyantest.R
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.datasave.UsersDatabaseHelper
import com.example.bingyantest.fragment.NoResultFragment
import com.example.bingyantest.fragment.RecycleViewFragment
import com.example.bingyantest.objects.Friend
import com.example.bingyantest.objects.MyObjects

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
                val intent = Intent(this, MainActivity::class.java)
                //初始化信息
                MyObjects.appContext = this
                MyObjects.user = MyObjects.refreshUser(account)
                MyObjects.appContext = this
                MyObjects.userAccount = account
                MyObjects.friendslist.value = ArrayList()
                MyObjects.add(Friend("friend", "friend", "friend", "friend"))
                MyObjects.newFriendList.add(
                    Friend(
                        "newFriend",
                        "newFriend",
                        "newFriend",
                        "newFriend"
                    )
                )
                MyObjects.initialize(this)
                MyObjects.load()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this, "account or password is invalid",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        register.setOnClickListener {
            RegisterActivity.actionStart(this)
        }
    }

    fun isLoginSucess(context: Context, inputAccount: String, inputPassword: String): Boolean {
        val dbHelper = UsersDatabaseHelper(context, "Users", 1)
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


    companion object {
        fun actionStart(context: Context, account: String) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}