package com.example.thegoodfoodadmin

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class OrderDetailsAdapter(var context: Context, var list: ArrayList<orderData>) :
    RecyclerView.Adapter<DetailsHodler>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsHodler {
        var view = LayoutInflater.from(context).inflate(R.layout.orderdetails, parent, false)
        return DetailsHodler(view)
    }

    fun update(list: ArrayList<orderData>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DetailsHodler, position: Int) {
        holder.bind(list[position], context)
    }

}

class DetailsHodler(var itemview: View) : RecyclerView.ViewHolder(itemview) {
    var flag = 0
    var ordername: TextView = itemview.findViewById<TextView>(R.id.ordername)
    var ordertime: TextView = itemview.findViewById<TextView>(R.id.ordertime)
    var orderprice: TextView = itemview.findViewById<TextView>(R.id.orderprice)
    var orderadd: TextView = itemview.findViewById<TextView>(R.id.orderadd)
    var orderpeople: TextView = itemview.findViewById<TextView>(R.id.orderpeople)
    var ordercount: TextView = itemview.findViewById<TextView>(R.id.ordercount)
    var orderitems: Button = itemview.findViewById<Button>(R.id.orderitems)
    var ordercard: CardView = itemview.findViewById<CardView>(R.id.ordercard2)
    var status: ImageView = itemview.findViewById<ImageView>(R.id.status)
    fun bind(orderData: orderData, context: Context) {
        ordername.text = orderData.ordername
        ordertime.text = orderData.ordertime
        if (orderData.status == "1") {
            status.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
        }
        status.setOnClickListener {
            status.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
            val db = FirebaseDatabase.getInstance().reference
            db.child("Admin")
                .child("Orders")
                .child(orderData.userref)
                .child(orderData.orderid)
                .updateChildren(hashMapOf("status" to "1") as Map<String, String>)
                .addOnSuccessListener {
                    Toast.makeText(context, "Order Status change to Processing", Toast.LENGTH_SHORT)
                        .show()
                }
        }
        TransitionManager.beginDelayedTransition(ordercard);
        ordercard.setOnClickListener {
            if (flag == 0) {
                orderprice.text = orderData.orderprice
                orderadd.text = orderData.orderadd
                orderpeople.text = orderData.orderpeople
                ordercount.text = orderData.ordercount
                orderprice.text = orderData.orderprice
                ordercount.text = orderData.ordercount
                orderprice.visibility = View.VISIBLE
                orderadd.visibility = View.VISIBLE
                orderpeople.visibility = View.VISIBLE
                ordercount.visibility = View.VISIBLE
                orderprice.visibility = View.VISIBLE
                orderitems.visibility = View.VISIBLE
                orderitems.setOnClickListener {
                    val i = Intent(context, Displayitem::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    i.putExtra("datastring", orderData.orderitems)
                    context.startActivity(i)
                }
                flag = 1
            } else {
                orderprice.visibility = View.GONE
                orderadd.visibility = View.GONE
                orderpeople.visibility = View.GONE
                ordercount.visibility = View.GONE
                orderprice.visibility = View.GONE
                orderitems.visibility = View.GONE
                flag = 0
            }

        }
    }
}