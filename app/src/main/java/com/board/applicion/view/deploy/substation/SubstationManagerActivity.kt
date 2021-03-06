package com.board.applicion.view.deploy.substation

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.board.applicion.R
import com.board.applicion.mode.databases.Substation
import com.board.applicion.mode.databases.Substation_
import com.board.applicion.view.deploy.BaseEditActivity
import io.objectbox.query.QueryBuilder

class SubstationManagerActivity : BaseEditActivity<Substation>() {

    private lateinit var adapter: Adapter

    override fun setAdapter() {
        adapter = Adapter(data, editData, this)
        getRecycleView().adapter = adapter
    }

    override fun modeChange() {
        adapter.isEdit = isEditMode
    }

    override fun getDataClass(): Class<Substation> {
        return Substation::class.java
    }

    override fun toSearchIntent(): Intent? {
        return Intent(this,SubstationSearchActivity::class.java)
    }

    override fun getAddIntent(): Intent {
        return Intent(this, SubstationAddActivity::class.java)
    }

    override fun getToolBarTitle(): String? {
        return "变电站管理"
    }

    /**
     * 查询变电站
     */
    override fun getQueryBuild(): QueryBuilder<Substation> {
        return databaseStore.getBox().query().equal(Substation_.status, 0)
    }

    private class Adapter(private val dataList: ArrayList<Substation>, val editList: ArrayList<Boolean>, private val content: Context)
        : RecyclerView.Adapter<ViewHolder>() {

        var isEdit: Boolean = false

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(content).inflate(R.layout.item_substation_list, parent, false)
            val editStateImage = view.findViewById<ImageView>(R.id.editStateImage)
            val substationName = view.findViewById<TextView>(R.id.substationName)
            val substationLevel = view.findViewById<TextView>(R.id.substationLevel)
            val substationDes = view.findViewById<TextView>(R.id.substationDes)
            val editLayout = view.findViewById<LinearLayout>(R.id.editLayout)
            return ViewHolder(view, editStateImage, substationName, substationLevel, substationDes, editLayout)
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
            holder.substationName.text = dataList[position].name
            holder.substationLevel.text = dataList[position].voltageRank
            holder.substationDes.text = dataList[position].desc
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
                }else{
                    val intent = Intent(content,SubstationAddActivity::class.java)
                    intent.putExtra("ID",dataList[position].id)
                    content.startActivity(intent)
                }
            }
        }
    }

    private class ViewHolder(itemView: View, val imageView: ImageView
                             , val substationName: TextView
                             , val substationLevel: TextView
                             , val substationDes: TextView, val editLayout: LinearLayout)
        : RecyclerView.ViewHolder(itemView)
}