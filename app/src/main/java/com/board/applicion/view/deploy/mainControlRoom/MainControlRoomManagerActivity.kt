package com.board.applicion.view.deploy.mainControlRoom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.board.applicion.R
import com.board.applicion.app.App
import com.board.applicion.mode.*
import com.board.applicion.mode.databases.MainControlRoom
import com.board.applicion.mode.databases.MainControlRoom_
import com.board.applicion.mode.databases.Substation
import com.board.applicion.view.deploy.BaseEditActivity
import io.objectbox.query.QueryBuilder

class MainControlRoomManagerActivity : BaseEditActivity<MainControlRoom>() {

    private lateinit var adapter: Adapter

    override fun setAdapter() {
        adapter = Adapter(data, editData, this)
        getRecycleView().adapter = adapter
    }

    override fun modeChange() {
        adapter.isEdit = isEditMode
    }

    override fun getDataClass(): Class<MainControlRoom> {
        return MainControlRoom::class.java
    }

    override fun toSearchIntent(): Intent? {
        return Intent(this,MainControlRoomSearchActivity::class.java)
    }

    override fun getAddIntent(): Intent {
        return Intent(this, MainControlRoomAddActivity::class.java)
    }

    override fun getQueryBuild(): QueryBuilder<MainControlRoom> {
        return databaseStore.getBox().query().equal(MainControlRoom_.status, 0)
    }

    override fun getToolBarTitle(): String? {
        return "主控室管理"
    }

    private class Adapter(private val dataList: ArrayList<MainControlRoom>, val editList: ArrayList<Boolean>, private val content: Context)
        : RecyclerView.Adapter<ViewHolder>() {

        var isEdit: Boolean = false

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(content).inflate(R.layout.item_main_control_room_list, parent, false)
            val editStateImage = view.findViewById<ImageView>(R.id.editStateImage)
            val mainRoomName = view.findViewById<TextView>(R.id.substationName)
            val substationName = view.findViewById<TextView>(R.id.substationLevel)
            val des = view.findViewById<TextView>(R.id.substationDes)
            val editLayout = view.findViewById<LinearLayout>(R.id.editLayout)
            return ViewHolder(view, editStateImage, mainRoomName, substationName, des, editLayout)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (isEdit) {
                holder.editLayout.visibility = View.VISIBLE
            } else {
                holder.editLayout.visibility = View.GONE
            }
            holder.text1.text = dataList[position].substationToOne.target.name
            holder.text2.text = dataList[position].name
            holder.text3.text = dataList[position].desc
            if (editList[position]) {
                holder.imageView.setImageDrawable(content.resources.getDrawable(R.drawable.radio_on))
            } else {
                holder.imageView.setImageDrawable(content.resources.getDrawable(R.drawable.radio_off))
            }
            holder.itemView.setOnClickListener {
                if (isEdit) {
                    editList[position] = !editList[position]
                    if (editList[position]) {
                        holder.imageView.setImageDrawable(content.resources.getDrawable(R.drawable.radio_on))
                    } else {
                        holder.imageView.setImageDrawable(content.resources.getDrawable(R.drawable.radio_off))
                    }
                } else {
                    val intent = Intent(content, MainControlRoomAddActivity::class.java)
                    intent.putExtra("ID", dataList[position].id)
                    content.startActivity(intent)
                }
            }
        }
    }

    private class ViewHolder(itemView: View, val imageView: ImageView
                             , val text1: TextView
                             , val text2: TextView
                             , val text3: TextView, val editLayout: LinearLayout)
        : RecyclerView.ViewHolder(itemView)
}