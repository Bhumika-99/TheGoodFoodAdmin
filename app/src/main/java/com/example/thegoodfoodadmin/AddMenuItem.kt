package com.example.thegoodfoodadmin

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_menu_item.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class AddMenuItem : AppCompatActivity() {
    var image_selected_code: Int = 101
    var imagesrc = ""
    var inputStream: InputStream? =null
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_menu_item)
        setSupportActionBar(addtoolbar)
        db= FirebaseDatabase.getInstance().reference
        st= FirebaseStorage.getInstance().reference
        editbtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, image_selected_code)
        }
        uploaditem.setOnClickListener {
            validateMenuItem()
        }

    }
    private fun validateMenuItem() {
        var name=itemname.text.toString().trim()
        var desc=itemdesc.text.toString().trim()
        var price=itemprice.text.toString().trim()
        var type=itemtype.text.toString().trim()
        var cat=itemcat.text.toString().trim()
        if(name.isNotEmpty() and desc.isNotEmpty() and  price.isNotEmpty() and type.isNotEmpty() and cat.isNotEmpty()){
            if(inputStream!=null){
                var a= AlertDialog.Builder(this)
                    .setTitle("Add Food Item")
                    .setMessage(" Are You Sure You Want To Add This Item")
                    .setPositiveButton("Yes"
                    ) { dialog, which ->
                        uploadMenuItem(name,desc,price,type,cat)
                        progcircle.visibility= View.VISIBLE
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
            else{
                Toast.makeText(applicationContext,"Please Select Food Item Picture",Toast.LENGTH_SHORT).show()

            }
        }
        else{
            Toast.makeText(applicationContext,"Please Enter Valid Details ",Toast.LENGTH_SHORT).show()
        }
    }
    private fun uploadMenuItem(name: String, desc: String, price: String, type: String,cat:String) {
        if(inputStream!=null){
            //compress the image
//                var bitmap=BitmapFactory.decodeStream(inputStream)
            var bitmap=(itemimage.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var stref=st.child("MenuItem").child("${name}.jpeg")
            stref.putBytes(data)
                .addOnSuccessListener{
                    stref.downloadUrl.addOnSuccessListener {
                        Log.d("url",it.toString())
                        var hashMap= hashMapOf(
                            "url" to it.toString(),
                            "name" to name,
                            "desc" to desc,
                            "price" to price,
                            "type" to type,
                            "cat" to cat
                        )
                        db.child("Admin")
                            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                            .child(UUID.randomUUID().toString())
                            .setValue(hashMap)
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext,"Menu Item Added Succefully",Toast.LENGTH_SHORT).show()
                                progcircle.visibility=View.GONE
                            }
                    }
                }


        }
    }

    fun getPath(uri: Uri?): String? {
        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(uri, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == image_selected_code) {
                try {
                    var uri = data?.data
                    inputStream = applicationContext.contentResolver.openInputStream(data?.data!!)!!
                    var bit = BitmapFactory.decodeStream(inputStream)
                    itemimage.setImageBitmap(bit)
                } catch (e: Exception) {
                    Log.d("exe", e.toString())
                }

            }
        }
    }

}
