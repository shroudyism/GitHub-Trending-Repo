package com.example.shroudyism.gojekassignment


data class ListItems(var author : String , var name :  String ,var avatar : String ,var  url : String ,
                var description : String , var stars : Long , var forks : Long ,var  currentPeriodStars : Long ,
                var builtBy : Array<User>) {

}

