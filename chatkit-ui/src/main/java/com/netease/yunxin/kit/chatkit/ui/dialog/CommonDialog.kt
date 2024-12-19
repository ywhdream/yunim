package com.netease.yunxin.kit.chatkit.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.util.DialogUtils

class CommonDialog {
    class Builder(private val mContext: Context) {
        private var mDialog: Dialog? = null
        private var mViewHolder: ViewHolder? = null
        private lateinit var mView: View
        private var hasPos = false
        private var hasNeg = false

        init {
            initView()
        }

        fun setTitle(title: CharSequence?): Builder {
            mViewHolder?.tvTitle?.text = title
            return this
        }

        fun setTitle(title: CharSequence?, color: Int): Builder {
            mViewHolder?.tvTitle?.visibility = View.VISIBLE
            mViewHolder?.tvTitle?.text = title
            mViewHolder?.tvTitle?.setTextColor(ContextCompat.getColor(mContext, color))
            return this
        }

        fun setTitle(resid: Int): Builder {
            mViewHolder?.tvTitle?.setText(resid)
            return this
        }

        fun setTitle(resid: Int, color: Int): Builder {
            mViewHolder?.tvTitle?.setText(resid)
            mViewHolder?.tvTitle?.setTextColor(ContextCompat.getColor(mContext, color))
            return this
        }

        fun setMessage(title: CharSequence?): Builder {
            mViewHolder?.tvMessage?.text = title
            return this
        }

        fun setMessage(title: CharSequence?, color: Int): Builder {
            mViewHolder?.tvMessage?.text = title
            mViewHolder?.tvMessage?.setTextColor(ContextCompat.getColor(mContext, color))
            return this
        }

        fun setMessage(resid: Int): Builder {
            mViewHolder?.tvMessage?.setText(resid)
            return this
        }

        fun setMessageNew(title: CharSequence?): Builder {
            mViewHolder?.tvMessageNew?.text = title
            mViewHolder?.tvMessageNew?.visibility = View.VISIBLE
            mViewHolder?.tvMessage?.visibility = View.GONE
            mViewHolder?.tvTitle?.visibility = View.GONE
            return this
        }

        fun setMessage(resid: Int, color: Int): Builder {
            mViewHolder?.tvMessage?.setText(resid)
            mViewHolder?.tvMessage?.setTextColor(ContextCompat.getColor(mContext, color))
            return this
        }

        fun setPositiveButton(text: CharSequence?, listener: View.OnClickListener?): Builder {
            mViewHolder?.tvPositiveButton?.visibility = View.VISIBLE
            hasPos = true
            mViewHolder?.tvPositiveButton?.text = text
            mViewHolder?.tvPositiveButton?.setOnClickListener { view: View? ->
                mDialog?.dismiss()
                listener?.onClick(view)
            }
            return this
        }

        fun setPositiveButton(
            text: CharSequence?,
            listener: View.OnClickListener?,
            color: Int
        ): Builder {
            return setPositiveButton(text, listener, color, true)
        }

        fun setPositiveButton(
            text: CharSequence?,
            listener: View.OnClickListener?,
            color: Int,
            isDismiss: Boolean
        ): Builder {
            mViewHolder?.tvPositiveButton?.visibility = View.VISIBLE
            hasPos = true
            mViewHolder?.tvPositiveButton?.text = text
            mViewHolder?.tvPositiveButton?.setTextColor(ContextCompat.getColor(mContext, color))
            mViewHolder?.tvPositiveButton?.setOnClickListener { view: View? ->
                if (isDismiss) {
                    mDialog!!.dismiss()
                }
                listener?.onClick(view)
            }
            return this
        }

        fun setNegativeButton(text: CharSequence?, listener: View.OnClickListener?): Builder {
            mViewHolder?.tvNegativeButton?.visibility = View.VISIBLE
            hasNeg = true
            mViewHolder?.tvNegativeButton?.text = text
            mViewHolder?.tvNegativeButton?.setOnClickListener { view ->
                mDialog?.dismiss()
                listener?.onClick(view)
            }
            return this
        }

        fun setNegativeButton(
            text: CharSequence?,
            listener: View.OnClickListener?,
            color: Int
        ): Builder {
            return setNegativeButton(text, listener, color, true)
        }

        fun setNegativeButton(
            text: CharSequence?,
            listener: View.OnClickListener?,
            color: Int,
            isDismiss: Boolean
        ): Builder {
            mViewHolder?.tvNegativeButton?.visibility = View.VISIBLE
            hasNeg = true
            mViewHolder?.tvNegativeButton?.text = text
            mViewHolder?.tvNegativeButton?.setTextColor(ContextCompat.getColor(mContext, color))
            mViewHolder?.tvNegativeButton?.setOnClickListener { view ->
                if (isDismiss) {
                    mDialog!!.dismiss()
                }
                listener?.onClick(view)
            }
            return this
        }

        fun setCancelable(flag: Boolean): Builder {
            mDialog?.setCancelable(flag)
            return this
        }

        fun setCanceledOnTouchOutside(flag: Boolean): Builder {
            mDialog?.setCanceledOnTouchOutside(flag)
            return this
        }

        fun create(): Dialog? {
            return mDialog
        }

        fun show() {
            if (mDialog != null) {
                if (hasPos || hasNeg) {
                    //    mViewHolder.line1.setVisibility(View.VISIBLE);
                }
//                if (hasPos && hasNeg) {
//                    mViewHolder?.line2?.visibility = View.VISIBLE
//                }
                mDialog?.show()
            }
        }

        fun dismiss() {
            if (mDialog != null) {
                mDialog?.dismiss()
            }
        }


        private fun initView() {
            mDialog = Dialog(mContext, DialogUtils.style)
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_easy_dialog, null)
            mViewHolder = ViewHolder(mView)
            mDialog?.setContentView(mView)
            val dm = DisplayMetrics()
            val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(dm)
            val lp = mDialog?.window?.attributes
            //lp.width = (int) (dm.widthPixels * 0.8);
            lp?.width = (dm.widthPixels * 0.7).toInt()
            mDialog?.window?.attributes = lp
        }

        internal inner class ViewHolder(view: View) {
            var tvTitle: TextView
            var tvMessage: TextView
            var tvMessageNew: TextView
            var tvPositiveButton: TextView
            var tvNegativeButton: TextView
            var vgLayout: ConstraintLayout
            var line1: View

            init {
                tvTitle = view.findViewById(R.id.dialog_title)
                tvMessage = view.findViewById(R.id.dialog_message)
                tvMessageNew = view.findViewById(R.id.dialog_message_new)
                tvPositiveButton = view.findViewById(R.id.dialog_positive)
                tvNegativeButton = view.findViewById(R.id.dialog_negative)
                vgLayout = view.findViewById(R.id.dialog_layout)
                line1 = view.findViewById(R.id.dialog_line1)
            }
        }
    }
}