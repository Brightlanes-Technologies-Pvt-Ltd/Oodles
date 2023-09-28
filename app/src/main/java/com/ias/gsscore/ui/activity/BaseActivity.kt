package com.ias.gsscore.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.ias.gsscore.R
import com.ias.gsscore.utils.Preferences


open class BaseActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var fulllayout: RelativeLayout
        var layoutResourceID = 0
    }

    override fun setContentView(layoutResID: Int) {
        layoutResourceID = layoutResID
        fulllayout = layoutInflater.inflate(R.layout.activity_base, null) as RelativeLayout
        val activityContainer = fulllayout.findViewById(R.id.content_frame) as FrameLayout
        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(fulllayout)

        //setSupportActionBar(toolbar)


        findViewById<LinearLayout>(R.id.navClick).setOnClickListener {
            setDrawer()
        }
        findViewById<LinearLayout>(R.id.ivBack).setOnClickListener {
            finish()
        }
        findViewById<LinearLayout>(R.id.notification).setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("whereFrom","cart")
            startActivity(intent)
        }

        findViewById<TextView>(R.id.orderlayout).setOnClickListener {
            startActivity(Intent(this, OrderListActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.settingLayout).setOnClickListener {
            startActivity(Intent(this, ProfileSettingActivity::class.java))

        }


        findViewById<TextView>(R.id.tvLogout).setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut()

            Preferences.getInstance(this).isLogin = false
            Preferences.getInstance(this).userId = ""
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


    }

    private fun setDrawer() {
        if (fulllayout.findViewById<DrawerLayout>(R.id.drawer_Layout)!!
                .isDrawerOpen(GravityCompat.START)
        ) {
            fulllayout.findViewById<DrawerLayout>(R.id.drawer_Layout)!!
                .closeDrawer(GravityCompat.START)
        } else {
            fulllayout.findViewById<DrawerLayout>(R.id.drawer_Layout)!!
                .openDrawer(GravityCompat.START)
        }
    }

    fun setToolBar(title: String) {
        fulllayout.findViewById<LinearLayout>(R.id.cart).visibility = View.GONE
        fulllayout.findViewById<ImageView>(R.id.nav_logo).visibility = View.GONE
        fulllayout.findViewById<LinearLayout>(R.id.navClick).visibility = View.GONE
        fulllayout.findViewById<LinearLayout>(R.id.ivBack).visibility = View.VISIBLE
        fulllayout.findViewById<TextView>(R.id.title).text = title
    }

    fun hideFooter(boolean: Boolean) {
        if (boolean)
            fulllayout.findViewById<LinearLayout>(R.id.layoutFooter).visibility = View.GONE
        else
            fulllayout.findViewById<LinearLayout>(R.id.layoutFooter).visibility = View.VISIBLE
    }


}