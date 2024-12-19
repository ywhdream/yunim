//package com.netease.yunxin.kit.chatkit.ui.view
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.FrameLayout
//import androidx.fragment.app.FragmentActivity
//import androidx.fragment.app.FragmentManager
//import com.google.android.material.tabs.TabLayout
//import com.netease.yunxin.kit.chatkit.ui.R
//import com.netease.yunxin.kit.chatkit.ui.model.ViewPagerModel
//
//
//class InputBottomPanel : FrameLayout {
//    private var actionList: List<BaseAction>? = null
//    private var childFragmentManager: FragmentManager? = null
//    private var fragmentList: List<ViewPagerModel>? = null
//
//    interface InputBottomPanelListener {
//        fun onShowPanel()
//
//        fun onHidePanel()
//
//        fun getFragmentList(): List<ViewPagerModel>
//
//        fun onTabReselected(tab: TabLayout.Tab?, fragmentList: List<ViewPagerModel>?)
//
//        fun onTabSelected(tab: TabLayout.Tab?, fragmentList: List<ViewPagerModel>?)
//
//        fun onPanelClick(item: String)
//    }
//
//    constructor(context: Context) : super(context)
//
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
//
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
//        context,
//        attrs,
//        defStyleAttr
//    )
//
//    private var mListener: InputBottomPanelListener? = null
//
//    fun setActionList(actionList: List<BaseAction>) {
//        this.actionList = actionList
//        initViews()
//    }
//
//    private fun initViews() {
//        LayoutInflater.from(context).inflate(R.layout.view_im_bottom_action, this)
//        try {
////            val fragmentManager = childFragmentManager ?: (context as FragmentActivity).supportFragmentManager
////            val adapter = TSViewPagerAdapter(fragmentManager)
////            vp_nim_action.adapter = adapter
////            fragmentList = mListener?.getFragmentList()
////            adapter.bindData(fragmentList?.map { it.fragment })
////            tab_nim_action.setupWithViewPager(vp_nim_action)
////            vp_nim_action.offscreenPageLimit = fragmentList?.size ?: 0
////            for (i in 0 until tab_nim_action.tabCount) {
////                fragmentList?.get(i)?.iconDrawable?.let {
////                    tab_nim_action.getTabAt(i)?.setIcon(it)
////                }
////            }
//
//
////            val adapter = ActionsPagerAdapter(vp_nim_action, actionList, mListener)
////            vp_nim_action.adapter = adapter
//
//            vp_nim_action.visibility = View.GONE
//            tab_nim_action.visibility = View.GONE
//
//            tab_nim_action.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//                override fun onTabReselected(tab: TabLayout.Tab?) {
//                    if (isValidTabSelect(tab, fragmentList)) {
//                        mListener?.onTabReselected(tab, fragmentList)
//                    }
//                }
//
//                override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//                }
//
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    if (isValidTabSelect(tab, fragmentList)) {
//                        mListener?.onTabSelected(tab, fragmentList)
//                    }
//                }
//            })
//        } catch (ex: Exception) {
//            BugfenderUtils.d("InputBottomPanel_InitViewFailed", ex)
//            ex.printStackTrace()
//        }
//    }
//
//    fun updateFragment(list: List<ViewPagerModel>?) {
//        if(list == null){
//            fragmentList = null
//            val adapter = ActionsPagerAdapter(vp_nim_action, actionList, mListener)
//            vp_nim_action.adapter = adapter
//            tab_nim_action.setupWithViewPager(vp_nim_action)
//            tab_nim_action.visibility = View.VISIBLE
//
//        } else{
//            val fragmentManager = childFragmentManager ?: (context as FragmentActivity).supportFragmentManager
//            val adapter = TSViewPagerAdapter(fragmentManager)
//            vp_nim_action.adapter = adapter
//            fragmentList = list
//            adapter.bindData(fragmentList?.map { it.fragment })
//            vp_nim_action.offscreenPageLimit = fragmentList?.size ?: 0
//            vp_nim_action.adapter = adapter
//            tab_nim_action.visibility = View.GONE
//        }
//    }
//
//
//    fun setChildFragmentManager(childFragmentManager: FragmentManager) {
//        this.childFragmentManager = childFragmentManager
//    }
//
//    fun setInputBottomPanelListener(listener: InputBottomPanelListener) {
//        mListener = listener
//    }
//
//    fun setPanelVisibility(isShow: Boolean) {
//        if (isShow) {
//            vp_nim_action?.visibility = View.VISIBLE
//        } else {
//            vp_nim_action?.visibility = View.GONE
//            tab_nim_action.visibility = View.GONE
//            resetIcon()
//        }
//    }
//
//    fun isPanelVisible(): Boolean {
//        return vp_nim_action?.visibility == View.VISIBLE
//    }
//
//    fun isValidTabSelect(tab: TabLayout.Tab?, fragmentList: List<ViewPagerModel>?): Boolean {
//        if (fragmentList == null || tab == null) {
//            return false
//        }
//
//        if (tab.position > fragmentList.size) {
//            return false
//        }
//
//        return true
//    }
//
//    private fun resetIcon() {
//        fragmentList?.let { itFragmentList ->
//            val size = itFragmentList.size.minus(1)
//            if (itFragmentList[size].tag?.equals(InputPanel.TAG_BOTTOM_PANEL_MORE) == true) {
//                tab_nim_action.getTabAt(size)?.setIcon(R.drawable.icons_addmoment_black2)
//            }
//        }
//    }
//}