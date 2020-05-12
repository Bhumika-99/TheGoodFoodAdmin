package com.example.thegoodfoodadmin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_displayitem.*

class Displayitem : AppCompatActivity() {
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    lateinit var foodlist: ArrayList<fooditemModel>
    lateinit var foodItemAdapter: foodItemAdapter
    var datalist = ArrayList<String>()
    var dataflag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displayitem)
        setSupportActionBar(hometoolbar)
        if (intent.hasExtra("datastring")) {
            datalist = intent.extras?.get("datastring") as ArrayList<String>
            dataflag = 1
        }
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        foodlist = ArrayList()
        var bottom_sheet = findViewById<RelativeLayout>(R.id.bottom_sheet)
        var sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                var bmimg = bottomSheet.findViewById(R.id.bmimg) as ImageView
                bmimg.minimumHeight = (200 * slideOffset * resources.displayMetrics.density).toInt()
                bmimg.minimumWidth = (200 * slideOffset * resources.displayMetrics.density).toInt()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

        })
//
        foodItemAdapter = foodItemAdapter(foodlist, applicationContext, bottom_sheet, sheetBehavior)
//        fooditemrec.layoutManager = LinearLayoutManager(this)
        fooditemrec.layoutManager = GridLayoutManager(applicationContext, 2)
        fooditemrec.adapter = foodItemAdapter
        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchinDb(newText!!)
                return true
            }

        })
        if (dataflag == 0) {
            db.child("Admin").child("XAk5RJc3YROwvdEvFZ3RDfCfXoD2")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Log.d("data", "got")
                        for (ds in p0.children) {
                            var fooditemModel = fooditemModel(
                                ds.child("url").value.toString(),
                                ds.child("name").value.toString(),
                                ds.child("price").value.toString(),
                                ds.child("desc").value.toString(),
                                ds.child("type").value.toString(),
                                ds.child("cat").value.toString()
                            )
                            fooditemModel.setid(ds.key.toString())
                            foodlist.add(fooditemModel)
                        }
                        foodItemAdapter.update(foodlist)
                        foodItemAdapter.notifyDataSetChanged()
                    }

                })
        } else {
            datalist.forEach {
                db.child("Admin").child("XAk5RJc3YROwvdEvFZ3RDfCfXoD2").child(it)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(ds: DataSnapshot) {
                            Log.d("data", "got")
                            var fooditemModel = fooditemModel(
                                ds.child("url").value.toString(),
                                ds.child("name").value.toString(),
                                ds.child("price").value.toString(),
                                ds.child("desc").value.toString(),
                                ds.child("type").value.toString(),
                                ds.child("cat").value.toString()
                            )
                            fooditemModel.setid(ds.key.toString())
                            foodlist.add(fooditemModel)
                            foodItemAdapter.update(foodlist)
                        }

                    })
            }
        }
    }

    fun searchinDb(query: String) {
        var temp: ArrayList<fooditemModel> = ArrayList()
        foodItemAdapter.update(temp)
        if (query.isNotEmpty()) {
            foodlist.forEach {

                if (it.name.toLowerCase().contains(query.toLowerCase())) {
                    temp.add(it)
                }
                if (it.price.contains(query.toLowerCase())) {
                    temp.add(it)
                }
                if (it.type.toLowerCase().contains(query.toLowerCase())) {
                    temp.add(it)
                }
                foodItemAdapter.update(temp)
            }

        } else {
            temp.clear()
            foodItemAdapter.update(foodlist)
        }
    }
}
