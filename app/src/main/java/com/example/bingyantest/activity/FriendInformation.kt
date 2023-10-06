package com.example.bingyantest.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bingyantest.R
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.objects.MyObjects
import de.hdodenhof.circleimageview.CircleImageView

class FriendInformation :BaseActivity(){
    companion object {
        fun actionStart(context: Context, name: String, account: String, email: String, imageuri: String) {
            val intent = Intent(context, FriendInformation::class.java)
            intent.putExtra("name", name)
            intent.putExtra("account", account)
            intent.putExtra("email",email)
            intent.putExtra("imageuri",imageuri)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.friend_information)
        val name = findViewById<TextView>(R.id.fname)
        val account = findViewById<TextView>(R.id.faccount)
        val email = findViewById<TextView>(R.id.femail)
        val image = findViewById<CircleImageView>(R.id.fimage)
        val change = findViewById<Button>(R.id.change)
        name.text = intent.getStringExtra("name")
        account.text = intent.getStringExtra("account")
        email.text = intent.getStringExtra(("email"))
        image.setImageURI(Uri.parse(intent.getStringExtra("imageuri")))
        val groupSet = findViewById<Button>(R.id.group_set)
        groupSet.setOnClickListener {
            GroupSet.actionStart(this, intent.getStringExtra("name")!!, intent.getStringExtra("account")!!, intent.getStringExtra("email")!!,intent.getStringExtra("imageuri")!!)
        }




        //此处有错误
        //val resourceId = intent.getIntExtra("imageuri",0)//此处有错误
        //image.setImageResource(resourceId)                                      //此处有错误
        //此处有错误




        if(MyObjects.isFriend(account.text.toString())){//如果已经是好友
            change.setBackgroundColor(Color.RED)
            change.setText("删除好友")
            change.setOnClickListener{
                val dbHelper = MyDatabaseHelper(this,"Users",1)
                AlertDialog.Builder(this).apply {
                    setTitle("确定要删除这个好友吗")
                    setMessage("删除之后，你们将失去所有聊天记录，且无法像之前那样自由发送信息")
                    setCancelable(false)
                    setPositiveButton("OK") { dialog, which ->//删除
                        MyObjects.remove(account.text.toString())
                        val db = dbHelper.writableDatabase
                        val values = ContentValues().apply {
                            put("sender",MyObjects.userAccount)
                            put("receiver",account.text.toString())
                        }
                        db.insertWithOnConflict("DeleteInformation",null,values, SQLiteDatabase.CONFLICT_IGNORE)

                        MyObjects.friendslist.value?.remove(MyObjects.query(account.toString()))

                        Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Cancel") { dialog, which ->//不删除
                    }
                    show()
                }
            }
        }
        else{//如果不是好友
            change.setOnClickListener{
                val dbHelper = MyDatabaseHelper(this,"Users",1)
                Toast.makeText(this,"好友申请已发送",Toast.LENGTH_SHORT).show()
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("sender",MyObjects.userAccount)
                    put("receiver",account.text.toString())
                }
                db.insertWithOnConflict("UnsolvedApplys",null,values,SQLiteDatabase.CONFLICT_IGNORE)
            }
            groupSet.visibility = View.GONE
        }
    }
}