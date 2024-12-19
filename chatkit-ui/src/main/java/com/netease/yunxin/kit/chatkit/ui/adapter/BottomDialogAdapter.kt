package com.netease.yunxin.kit.chatkit.ui.adapter

import android.content.Context
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.model.BottomDialogBean
import com.netease.yunxin.kit.chatkit.ui.util.ViewHolder


/**
 * @Author ywh

 * @Date 2023/11/1

 * @类的作用:
 */
class BottomDialogAdapter(context: Context, data: List<BottomDialogBean>) :
    CommonAdapter<BottomDialogBean>(context, R.layout.bottom_dialog_item, data) {
    private var callback: ((Int, BottomDialogBean) -> Unit)? = null

    fun addItemClickListener(callback: (Int, BottomDialogBean) -> Unit) {
        this.callback = callback
    }

    override fun convert(holder: ViewHolder?, item: BottomDialogBean?, position: Int) {

        item?.run {
            holder?.apply {
                getView<AppCompatTextView>(R.id.bottom_dialog_name).text = item.name
                getView<AppCompatTextView>(R.id.bottom_dialog_name).setOnClickListener {
                    callback?.invoke(position, item)
                }
            }
        }

    }
}