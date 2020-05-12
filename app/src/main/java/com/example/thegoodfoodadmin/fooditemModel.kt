package com.example.thegoodfoodadmin

class fooditemModel(var imgurl:String,var name:String,var price:String,var desc:String,var type:String,var cat:String) {
    var foodid:String=""
    fun setid(id:String){
        this.foodid=id
    }
}