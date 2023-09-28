package com.ias.gsscore.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivitySplashBinding
import com.ias.gsscore.utils.Preferences


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivSplash.setImageDrawable(resources.getDrawable(R.drawable.splash))

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                if (Preferences.getInstance(this).isLogin){
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            finish()
        }, 3000)
        }
    }
