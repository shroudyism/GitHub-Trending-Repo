package com.example.shroudyism.trendingGitHub.dataModels

//Model class of trending repositories objects
data class TrendingListItems(var author : String, var name :  String, var avatar : String, var  url : String,
                             var description : String, var stars : Long, var forks : Long, var  currentPeriodStars : Long,
                             var builtBy : Array<User>) {

}


data class User(var ref : String , var avatar : String , var username : String){

}