package com.example.thegoodfoodadmin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_order_details_view.*

class OrderDetailsView : AppCompatActivity() {
    lateinit var list: ArrayList<orderData>
    lateinit var adapter: OrderDetailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details_view)
        setSupportActionBar(detailstoolbar)
        list = ArrayList()
        adapter = OrderDetailsAdapter(applicationContext, list)
        val data = intent.extras?.get("data") as orderUserData
        list.addAll(data.orders)
        detailrec.layoutManager=LinearLayoutManager(applicationContext)
        detailrec.adapter=adapter
        adapter.update(list)

    }
}
