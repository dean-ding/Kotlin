package cn.com.dean.kotlin

import android.app.Activity
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.View
import cn.com.dean.kotlin.fragment.DiscoverFragment
import cn.com.dean.kotlin.fragment.MatchFragment
import cn.com.dean.kotlin.fragment.MeFragment
import cn.com.dean.kotlin.fragment.NewsFragment
import cn.com.dean.kotlin.view.TabView
import com.safframework.log.L

class MainActivity : Activity(), TabView.TabCallback {

    internal object FRAGMENT {
        val NEWS: Int = 0x01
        val MATCH: Int = 0x02
        val DISCOVER: Int = 0x03
        val ME: Int = 0x04
    }

    private lateinit var mNewTabView: TabView
    private lateinit var mMatchTabView: TabView
    private lateinit var mDiscoverTabView: TabView
    private lateinit var mMeTabView: TabView

    private lateinit var mNewsFragment: NewsFragment
    private lateinit var mMatchFragment: MatchFragment
    private lateinit var mDiscoverFragment: DiscoverFragment
    private lateinit var mMeFragment: MeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNewsFragment = NewsFragment()
        mMatchFragment = MatchFragment()
        mDiscoverFragment = DiscoverFragment()
        mMeFragment = MeFragment()

        mNewTabView = findViewById(R.id.tab_news) as TabView
        mNewTabView.id = FRAGMENT.NEWS
        mMatchTabView = findViewById(R.id.tab_match) as TabView
        mMatchTabView.id = FRAGMENT.MATCH
        mDiscoverTabView = findViewById(R.id.tab_discover) as TabView
        mDiscoverTabView.id = FRAGMENT.DISCOVER
        mMeTabView = findViewById(R.id.tab_me) as TabView
        mMeTabView.id = FRAGMENT.ME

        mNewTabView.setCallback(this)
        mMatchTabView.setCallback(this)
        mDiscoverTabView.setCallback(this)
        mMeTabView.setCallback(this)

        mNewTabView.isSelected = true
        addFragment(FRAGMENT.NEWS)
    }

    override fun onTabClick(v: View) {
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
        addFragment(v.id)
    }

    private fun addFragment(index: Int) {
        L.i(String.format("current thread id is %s",Thread.currentThread().name))
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        when (index) {
            FRAGMENT.NEWS -> {
                fragmentTransaction.replace(R.id.fragments, mNewsFragment)
            }
            FRAGMENT.MATCH -> {
                fragmentTransaction.replace(R.id.fragments, mMatchFragment)
            }
            FRAGMENT.DISCOVER -> {
                fragmentTransaction.replace(R.id.fragments, mDiscoverFragment)
            }
            FRAGMENT.ME -> {
                fragmentTransaction.replace(R.id.fragments, mMeFragment)
            }
        }
        fragmentTransaction.commit()
    }

}
