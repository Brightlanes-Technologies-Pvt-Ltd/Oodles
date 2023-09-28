package com.ias.gsscore.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.room.Room
import com.ias.gsscore.db.OodlesDB
import com.ias.gsscore.network.response.courses.Filter

class SingletonClass : Application() {
    var supportFragmentManager: FragmentManager?=null
    var containerFragmentManager: FragmentManager?=null
    var navController: NavController?=null
    var parentList: ArrayList<Filter> = arrayListOf()
    private var db: OodlesDB?=null
     var cartCount: TextView?=null
    var cartCountLayout: LinearLayout?=null
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var instance:SingletonClass
        private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getSupportManager() : FragmentManager {
        return supportFragmentManager!!
    }

    fun setSupportManager(supportFragmentManager: FragmentManager){
        this.supportFragmentManager = supportFragmentManager
    }

    fun setContainerManager(containerFragmentManager: FragmentManager){
        this.containerFragmentManager = containerFragmentManager
    }


    fun getCustomNavController() : NavController {
        return navController!!
    }

    fun setCustomNavController(navController: NavController){
        this.navController = navController
    }

    fun getFilterList() : ArrayList<Filter> {
        return parentList
    }

    fun setFilterList(parentList:ArrayList<Filter>){
        this.parentList = parentList
    }


    fun setCart(cartCount:TextView,cartCountLayout:LinearLayout){
        this.cartCount = cartCount
        this.cartCountLayout = cartCountLayout

    }

    fun dbInstance(context: Context): OodlesDB{
        if (db==null){
            db = Room.databaseBuilder(
                context,
                OodlesDB::class.java, "Oodles"
            ).build()
        }

        return db as OodlesDB
    }
}