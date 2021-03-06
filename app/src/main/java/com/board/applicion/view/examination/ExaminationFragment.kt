package com.board.applicion.view.examination

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.board.applicion.R
import com.board.applicion.app.App
import com.board.applicion.base.BaseFragment
import com.board.applicion.mode.DatabaseStore
import com.board.applicion.mode.SPConstant
import com.board.applicion.mode.databases.Substation
import com.board.applicion.view.examination.room.CabinetListActivity
import com.board.applicion.view.login.LoginActivity
import com.library.utils.SPHelper
import com.videogo.openapi.EZOpenSDK
import kotlinx.android.synthetic.main.fragment_examination.*

class ExaminationFragment : BaseFragment() {

    lateinit var adapter: SubListAdapter
    private val dataList = ArrayList<Substation>()

    companion object {
        fun getFragment(): ExaminationFragment {
            val fragment = ExaminationFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContentView(): Int {
        return R.layout.fragment_examination
    }

    private lateinit var subStore: DatabaseStore<Substation>

    override fun initData() {
        subStore = DatabaseStore(lifecycle, Substation::class.java)
    }

    override fun initView() {
        userText.text = "欢迎，${App.instance.getCurrentUser().realName}"
        adapter = SubListAdapter(activity, R.layout.item_sub, R.layout.item_room)
        adapter.setData(dataList)
        adapter.setItemListener {
            val intent = Intent(activity, CabinetListActivity::class.java)
            intent.putExtra("title", it.name)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
        expandableListView.setAdapter(adapter)
        subStore.getQueryData(subStore.getQueryBuilder().build()) {
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyDataSetChanged()
            if (it.isEmpty()) {
                noDataTv.visibility = View.VISIBLE
            } else {
                noDataTv.visibility = View.GONE
                for (i in 0 until expandableListView.count) {
                    expandableListView.expandGroup(i)
                }
            }
        }
        exitUser.setOnClickListener {
            MaterialDialog.Builder(activity!!)
                    .content("退出登录?")
                    .negativeText("取消")
                    .positiveText("确定")
                    .onPositive { dialog, _ ->
                        dialog.dismiss()
                        if (activity != null) {
                            EZOpenSDK.logout()
                            activity!!.finish()
                            SPHelper.remove(activity!!, SPConstant.SP_NAME, SPConstant.SP_CURRENT_USER)
                            activity!!.startActivity(Intent(activity!!, LoginActivity::class.java))
                        }
                    }
                    .build().show()
        }

    }
}