package com.example.thegoodfoodadmin

import java.io.Serializable

class orderData : Serializable {
    var ordername = ""
    var ordertime = ""
    var orderprice = ""
    var ordercount = ""
    var orderadd = ""
    var orderpeople = ""
    var orderitems = ArrayList<String>()
    var status=""
    var orderid=""
    var userref=""
}

class orderUserData : Serializable {
    var email = ""
    var name = ""
    var phonenumber = ""
    var orders = ArrayList<orderData>()
    var userid=""
}