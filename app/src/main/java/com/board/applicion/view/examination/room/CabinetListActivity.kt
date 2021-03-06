package com.board.applicion.view.examination.room

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.board.applicion.R
import com.board.applicion.base.BaseActivity
import com.board.applicion.mode.DatabaseStore
import com.board.applicion.mode.databases.Cabinet
import com.board.applicion.mode.databases.Cabinet_
import com.board.applicion.view.search.CabinetHistoryActivity
import kotlinx.android.synthetic.main.activity_cabinet_list.*

class CabinetListActivity : BaseActivity() {

    var dataList = ArrayList<Cabinet>()
    private var isShowHistory = false
    private var roomId = -1L

    override fun initView(savedInstanceState: Bundle?) {
        recycleView.layoutManager = LinearLayoutManager(this)
        val headerView = LayoutInflater.from(this).inflate(R.layout.layout_search_all, null)
        recycleView.addHeaderView(headerView)
        headerView.setOnClickListener {
            //to search
            val intent = Intent(this,CabinetSearchActivity::class.java)
            intent.putExtra("showHistory",isShowHistory)
            intent.putExtra("id",roomId)
            startActivity(intent)
        }
        isShowHistory = intent.getBooleanExtra("showHistory", false)
        recycleView.adapter = Adapter(dataList, isShowHistory, this)
    }

    override fun initData() {
        roomId = intent.getLongExtra("id", -1)
        val cabinetStore = DatabaseStore(lifecycle, Cabinet::class.java)
        cabinetStore.getQueryData(cabinetStore.getQueryBuilder().equal(Cabinet_.mcrId, roomId).build()) {
            dataList.clear()
            dataList.addAll(it)
            if (dataList.isEmpty()) {
                noDataTv.visibility = View.VISIBLE
            } else {
                noDataTv.visibility = View.GONE
            }
            recycleView.adapter?.notifyDataSetChanged()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_cabinet_list
    }

    override fun getToolBarTitle(): String? {
        return intent.getStringExtra("title")
    }


    private class Adapter(private val dataList: ArrayList<Cabinet>, private val isShowHistory: Boolean, private val content: Context)
        : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(content).inflate(R.layout.item_cabinet, parent, false)
            val name = view.findViewById<TextView>(R.id.name)
            return ViewHolder(view, name)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = dataList[position].name
            holder.itemView.setOnClickListener {
                if (content is Activity) {
                    if (isShowHistory) {
                        val intent = Intent(content, CabinetHistoryActivity::class.java)
                        intent.putExtra("cabinetId", dataList[position].id)
                        content.startActivity(intent)
                    } else {
                        val intent = Intent(content, SwitchBoardActivity::class.java)
                        intent.putExtra("title", dataList[position].name)
                        intent.putExtra("id", dataList[position].id)
                        content.startActivity(intent)
                    }
                }
            }
        }
    }

    private class ViewHolder(itemView: View, val name: TextView)
        : RecyclerView.ViewHolder(itemView)

}