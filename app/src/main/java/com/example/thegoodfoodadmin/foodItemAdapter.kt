package com.example.thegoodfoodadmin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior

class foodItemAdapter(
    var foodlist: ArrayList<fooditemModel>,
    var context: Context,
    var sheetview: View,
    var sheetBehavior: BottomSheetBehavior<RelativeLayout>
) :
    RecyclerView.Adapter<itemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.fooditemscard, parent, false)
        return itemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foodlist.size

    }

    fun update(fl: ArrayList<fooditemModel>) {
        this.foodlist = fl
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        var item = foodlist[position]

        var bmimg = sheetview!!.findViewById(R.id.bmimg) as ImageView
        var name = sheetview!!.findViewById(R.id.foodname) as TextView
        var desc = sheetview!!.findViewById(R.id.fooddesc) as TextView
        var price = sheetview!!.findViewById(R.id.foodprice) as TextView
        var quantity = sheetview!!.findViewById(R.id.foodquantity) as TextView
        var category = sheetview!!.findViewById(R.id.foodcategory) as TextView
        var bmbtn = sheetview!!.findViewById(R.id.bmbtn) as Button
        holder.name.setText(foodlist[position].name)
        holder.price.setText(foodlist[position].price)
        Glide.with(context).load(foodlist[position].imgurl).into(holder.imgitem)

        holder.showmore.setOnClickListener {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                Log.d("slidekey",item.name)
                name.text = "Name: ${item.name}"
                desc.text = "Description: ${item.desc}"
                price.text = "Price: ${item.price}"
                quantity.text = "Quantity:${item.cat}"
                category.text = "Category: ${item.type}"
                bmbtn.setOnClickListener {
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
                Glide.with(context).load(item.imgurl).into(bmimg)
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
//        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
    }
}

class itemViewHolder(var itemview: View) : RecyclerView.ViewHolder(itemview) {
    var name = itemview.findViewById(R.id.recitemname) as TextView
    var price = itemview.findViewById(R.id.recitemprice) as TextView
    var imgitem = itemview.findViewById(R.id.recitemimage) as ImageView
    var showmore = itemView.findViewById(R.id.showmore) as CardView


}