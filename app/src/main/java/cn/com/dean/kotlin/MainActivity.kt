package cn.com.dean.kotlin

import android.app.Activity
import android.os.Bundle
import android.view.View
import cn.com.dean.kotlin.view.TabView
import cn.com.dean.kotlin.view.TitleView

class MainActivity : Activity(), TabView.TabCallback {


    private lateinit var mTitleView: TitleView
    private lateinit var mNewTabView: TabView
    private lateinit var mMatchTabView: TabView
    private lateinit var mDiscoverTabView: TabView
    private lateinit var mMeTabView: TabView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTitleView = findViewById(R.id.title_View) as TitleView
        mNewTabView = findViewById(R.id.tab_news) as TabView
        mMatchTabView = findViewById(R.id.tab_match) as TabView
        mDiscoverTabView = findViewById(R.id.tab_discover) as TabView
        mMeTabView = findViewById(R.id.tab_me) as TabView

        mNewTabView.setCallback(this)
        mMatchTabView.setCallback(this)
        mDiscoverTabView.setCallback(this)
        mMeTabView.setCallback(this)

        mTitleView.OnClick(mTitleView.getChildAt(0))
        mNewTabView.isSelected = true
    }

    fun OnTitleClick(v: View) {
        mTitleView.OnClick(v)
    }

    override fun onTabClick(v: View?) {
        if (v != mNewTabView) {
            mNewTabView.isSelected = false
        }
        if (v != mMatchTabView) {
            mMatchTabView.isSelected = false
        }
        if (v != mDiscoverTabView) {
            mDiscoverTabView.isSelected = false
        }
        if (v != mMeTabView) {
            mMeTabView.isSelected = false
        }
    }

}
