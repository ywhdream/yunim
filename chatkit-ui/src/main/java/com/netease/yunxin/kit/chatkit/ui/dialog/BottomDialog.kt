package com.netease.yunxin.kit.chatkit.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.adapter.BottomDialogAdapter
import com.netease.yunxin.kit.chatkit.ui.constant.Constant.AUDIO
import com.netease.yunxin.kit.chatkit.ui.constant.Constant.CANCEL
import com.netease.yunxin.kit.chatkit.ui.constant.Constant.VIDEO
import com.netease.yunxin.kit.chatkit.ui.model.BottomDialogBean
import com.netease.yunxin.kit.chatkit.ui.view.input.ActionConstants.ACTION_TYPE_AUDIO_CALL_ACTION
import com.netease.yunxin.kit.chatkit.ui.view.input.ActionConstants.ACTION_TYPE_VIDEO_CALL_ACTION


/**
 * @Author ywh

 * @Date 2023/11/1

 * @类的作用:
 */
class BottomDialog(private val context: Context) :
    BottomSheetDialogFragment() {
    private val mBottomDialogAdapter: BottomDialogAdapter by lazy {
        BottomDialogAdapter(
            context, mutableListOf()
        )
    }
    private var bottomDialogListener: BottomDialogListener? = null

    private val root: View by lazy {
        LayoutInflater.from(activity).inflate(R.layout.dialog_bottom, null)
    }

    interface BottomDialogListener {
        fun onBottomDialog(bottomDialogBean: BottomDialogBean)
    }

    fun setBottomDialogListener(listener: BottomDialogListener) {
        this.bottomDialogListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null) return super.onCreateDialog(savedInstanceState)
        val dialog = Dialog(
            requireActivity(), R.style.BottomSheetStyle
        ).apply {
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(root)
            setCanceledOnTouchOutside(true)
            //设置dialog在底部显示
            window?.setGravity(Gravity.BOTTOM)
            //设置dialog宽度占满屏幕，高度包含内容
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            //因为我的dialog背景图片是圆弧型，不设置背景透明的话圆弧处显示黑色
            window?.setBackgroundDrawableResource(android.R.color.transparent)

        }
        initView()
        return dialog
    }

    private fun initView() {

        val recyclerView = root.findViewById<RecyclerView>(R.id.share_Recyclerview)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mBottomDialogAdapter
        mBottomDialogAdapter.addAllData(setBottomDialog())
        mBottomDialogAdapter.addItemClickListener { _, bottomDialogBean ->
            bottomDialogListener?.onBottomDialog(bottomDialogBean)
            dismiss()

        }


    }

    private fun setBottomDialog(): MutableList<BottomDialogBean> {
        val bottomDialog: MutableList<BottomDialogBean> = mutableListOf()

        bottomDialog.add(
            BottomDialogBean(
                AUDIO,
                getString(R.string.chat_message_audio_call_action)
            )
        )
        bottomDialog.add(
            BottomDialogBean(
                VIDEO,
                getString(R.string.chat_message_video_call_action)
            )
        )
        bottomDialog.add(BottomDialogBean(CANCEL, getString(R.string.cancel)))


        return bottomDialog
    }
}