package com.example.thegoodfoodadmin

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_orders.*

class Orders : AppCompatActivity() {
    lateinit var db: DatabaseReference
    lateinit var list: ArrayList<orderUserData>
    lateinit var adapter: OrdersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        setSupportActionBar(ordertoolbar)
        db = FirebaseDatabase.getInstance().reference
        list = ArrayList()
        adapter = OrdersAdapter(applicationContext, list, this)
        allorders.layoutManager = LinearLayoutManager(applicationContext)
        allorders.adapter = adapter
        db.child("Admin").child("Orders").addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    list.clear()
                    if (p0.exists() and p0.hasChildren()) {
                        for (ds in p0.children) {
                            Log.d("data", ds.key.toString())
                            db.child("users")
                                .child(ds.key.toString())
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onDataChange(userdata: DataSnapshot) {
                                        db.child("Admin")
                                            .child("Orders")
                                            .child(ds.key.toString())
                                            .addValueEventListener(object : ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {}
                                                override fun onDataChange(orderdata: DataSnapshot) {
                                                    if (userdata.exists()) {
                                                        var c = 1
                                                        val d = orderUserData()
                                                        d.email =
                                                            userdata.child("email").value.toString()
                                                        d.phonenumber =
                                                            userdata.child("phone").value.toString()
                                                        d.userid = ds.key.toString()
                                                        for (data in orderdata.children) {
                                                            val order = orderData()
                                                            order.ordername = "order$c"
                                                            order.orderprice =
                                                                data.child("price").value.toString()
                                                            order.orderadd =
                                                                data.child("add").value.toString()
                                                            order.ordertime =
                                                                data.child("del").value.toString()
                                                            order.ordercount =
                                                                data.child("items").value.toString()
                                                            order.orderitems =
                                                                data.child("foodlist").value as ArrayList<String>
                                                            order.orderid = data.key.toString()
                                                            order.userref = ds.key.toString()
                                                            if (data.hasChild("status")) {
                                                                order.status =
                                                                    data.child("status").value.toString()
                                                            }
                                                            d.orders.add(order)
                                                            c += 1
                                                        }
                                                        list.add(d)
                                                        adapter.update(list)
                                                    }
                                                }
                                            })
                                    }
                                })
                        }
                    }
                }

            }
        )
        searchbarorder.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchinDb(newText!!)
                return true
            }

        })
    }

    fun searchinDb(query: String) {
        var temp: ArrayList<orderUserData> = ArrayList()
        adapter.update(temp)
        if (query.isNotEmpty()) {
            list.forEach {

                if (it.name.toLowerCase().contains(query.toLowerCase())) {
                    temp.add(it)
                }
                if (it.email.contains(query.toLowerCase())) {
                    temp.add(it)
                }
                if (it.phonenumber.toLowerCase().contains(query.toLowerCase())) {
                    temp.add(it)
                }
                adapter.update(temp)
            }

        } else {
            temp.clear()
            adapter.update(list)
        }

    }
}
