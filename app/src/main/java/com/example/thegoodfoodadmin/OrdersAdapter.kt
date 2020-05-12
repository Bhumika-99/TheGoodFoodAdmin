package com.example.thegoodfoodadmin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class OrdersAdapter(
    var context: Context,
    var list: ArrayList<orderUserData>,
    var instnace: Orders
) :
    RecyclerView.Adapter<ordersHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ordersHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.users_order_row, parent, false)
        return ordersHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<orderUserData>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ordersHolder, position: Int) {
        holder.email.setText(list[position].email)
        holder.phone.setText(list[position].phonenumber)
        holder.ordercard.setOnClickListener {
            var i = Intent(context, OrderDetailsView::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.putExtra("data", list[position])
            context.startActivity(i)
        }
        holder.cancel.setOnClickListener {
            var a = AlertDialog.Builder(instnace)
                .setTitle("Deliverd")
                .setMessage("This order Will be removed from current orders")
                .setPositiveButton(
                    "Yes"
                ) { dialog, which ->
                    removeorder(list[position])
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
                .create()
                .show()
        }
    }

    private fun removeorder(orderUserData: orderUserData) {
        val db = FirebaseDatabase.getInstance().reference
        db.child("Admin").child("Orders").child(orderUserData.userid).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Order removed", Toast.LENGTH_SHORT).show()
            }
    }

}

class ordersHolder(var itemview: View) : RecyclerView.ViewHolder(itemview) {
    var email = itemView.findViewById<TextView>(R.id.orderemail)
    var phone = itemView.findViewById<TextView>(R.id.orderphone)
    var ordercard = itemView.findViewById<CardView>(R.id.ordercard)
    var cancel = itemView.findViewById<ImageView>(R.id.cancel)

}