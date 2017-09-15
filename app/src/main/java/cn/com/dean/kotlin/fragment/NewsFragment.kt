package cn.com.dean.kotlin.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.com.dean.kotlin.R
import cn.com.dean.kotlin.view.TitleView


/**
 * Created: tvt on 17/9/13 14:11
 */
class NewsFragment : Fragment() {

    private lateinit var mTitleView: TitleView
    private lateinit var mContentView: ViewPager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.news, container, false)

        mTitleView = view.findViewById(R.id.news_title_View) as TitleView
        mTitleView.OnClick(mTitleView.getChildAt(0))
        for (i in 0 until mTitleView.childCount) {
            mTitleView.getChildAt(i).setOnClickListener(TextClick())
        }
        mContentView = view.findViewById(R.id.news_content) as ViewPager

        return view
    }

    inner class TextClick : View.OnClickListener {
        override fun onClick(v: View?) {
            mTitleView.OnClick(v)
        }
    }
}