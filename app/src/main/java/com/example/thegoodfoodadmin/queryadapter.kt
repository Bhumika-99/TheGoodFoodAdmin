package com.example.thegoodfoodadmin

import android.content.Context
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class queryadapter(
    var userList: ArrayList<query>,
    val context: Context
) : RecyclerView.Adapter<queryadapter.ViewHolder>() {
    var isexpnaded = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val qtext = itemView.findViewById(R.id.qtext1) as TextView
        val qemail = itemView.findViewById(R.id.qemail) as TextView
        val qname = itemView.findViewById(R.id.qname) as TextView
        val qcard = itemView.findViewById<CardView>(R.id.qcard)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.querycard, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        TransitionManager.beginDelayedTransition(holder.qcard)
        val user: query = userList[p1]
        holder.qtext.text = "FeedBack: ${user.qtext}"
        holder.qemail.text = "Email: ${user.email}"
        holder.qname.text = "Name: ${user.name}"
        holder.qcard.setOnClickListener {
            if (holder.qtext.visibility == View.GONE) {
                holder.qtext.visibility = View.VISIBLE
            } else {
                holder.qtext.visibility = View.GONE

            }
        }
    }
}