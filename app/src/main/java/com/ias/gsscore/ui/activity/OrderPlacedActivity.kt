package com.ias.gsscore.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.ias.gsscore.R
import com.ias.gsscore.ui.adapter.CartDeliveryItemAdapter

class OrderPlacedActivity : AppCompatActivity() {

    @BindView(R.id.rv_item_list)
    lateinit var rv_item_list: RecyclerView

    private var cartDeliverItemAdapter: CartDeliveryItemAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)
        ButterKnife.bind(this)

        var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_item_list.layoutManager = linearLayoutManager
        cartDeliverItemAdapter = CartDeliveryItemAdapter()
        rv_item_list.adapter = cartDeliverItemAdapter

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}