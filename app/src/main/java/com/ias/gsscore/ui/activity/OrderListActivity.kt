package com.ias.gsscore.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.ias.gsscore.R
import com.ias.gsscore.ui.adapter.OrderListAdapter

class OrderListActivity : AppCompatActivity() {
    @BindView(R.id.rvOrderList)
    lateinit var rvOrderList: RecyclerView
    private var orderListAdapter: OrderListAdapter?=null
    private lateinit var linearLayoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        ButterKnife.bind(this)

        var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvOrderList.layoutManager = linearLayoutManager
        orderListAdapter = OrderListAdapter(this)
        rvOrderList.adapter = orderListAdapter
    }
}