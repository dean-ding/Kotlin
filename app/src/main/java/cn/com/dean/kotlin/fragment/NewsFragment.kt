package cn.com.dean.kotlin.fragment

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import cn.com.dean.kotlin.R
import cn.com.dean.kotlin.helper.GlobalUnit
import cn.com.dean.kotlin.resp.RespBase
import cn.com.dean.kotlin.thread.PoolThreadCallback
import cn.com.dean.kotlin.view.ListViewAdapter
import cn.com.dean.kotlin.view.TitleView
import cn.com.dean.server.NetType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.safframework.log.L
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created: tvt on 17/9/13 14:11
 */
class NewsFragment : Fragment(), PoolThreadCallback {

    private lateinit var mTitleView: TitleView
    private lateinit var mPagerAdapter: MyPagerAdapter
    private lateinit var mContentView: ViewPager
    private lateinit var mHandler: Handler
    private lateinit var mViewInPager: MutableList<View>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.news, container, false)

        mTitleView = view.findViewById(R.id.news_title_View) as TitleView
        mTitleView.OnClick(mTitleView.getChildAt(0))
        for (i in 0 until mTitleView.childCount) {
            val child = mTitleView.getChildAt(i)
            child.id = i
            child.setOnClickListener(TextClick())
        }

        mViewInPager = ArrayList()
        for (i in 0 until mTitleView.GetCount()) {
            val recommendAdapter = ListViewAdapter(this.activity)
            val recommendView = inflater.inflate(R.layout.news_view_pager_sub_view, null) as ListView
            recommendView.adapter = recommendAdapter
            mViewInPager.add(recommendView)
        }
        mPagerAdapter = MyPagerAdapter(mViewInPager)
        mContentView = view.findViewById(R.id.news_content) as ViewPager
        mContentView.adapter = mPagerAdapter
        mContentView.currentItem = 0
        mContentView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mTitleView.OnClick(mTitleView.getChildAt(position))
            }

        })

        mHandler = object : Handler() {
            override fun handleMessage(msg: Message?) {

            }
        }
        L.d("mViewInPager.size = " + mViewInPager.size)

        loadData(NetType.NEWS_TYPE, NetType.NEWS_TYPE_ITEM.TYPE_NEWS_RECOMMEND)
        return view
    }

    inner class TextClick : View.OnClickListener {
        override fun onClick(v: View?) {
            val id = v!!.id
            mTitleView.OnClick(v)
            mContentView.setCurrentItem(id, true)
        }
    }

    private fun loadData(type: String, subType: String) {
        val url = GlobalUnit.HTTP_URL_BASE + File.separator + type + "&" + subType

        val okHttpClient = OkHttpClient()
        okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS)
        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS)
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS)

        val request: Request = Request.Builder().url(url).addHeader("Charset", "UTF-8").build()
        val call = okHttpClient.newCall(request)
        call.enqueue(OkHttpCallback())
    }

    inner class OkHttpCallback : Callback {
        override fun onResponse(response: Response) {
            if (!response.isSuccessful) {
                return
            }
            val result = response.body().string()
            val data: List<RecommendResponse> = Gson().fromJson(result, object : TypeToken<List<RecommendResponse>>() {}.type)

            mHandler.post {
                val listView: ListView = mViewInPager[mContentView.currentItem] as ListView
                val adapter: ListViewAdapter = listView.adapter as ListViewAdapter
                adapter.setData(data)
            }
        }

        override fun onFailure(request: Request?, e: IOException?) {
            L.d("onFailure-->" + e.toString())
        }
    }

    override fun onPoolThreadRet(id: Int, code: Int, respBase: RespBase?) {
        if (respBase == null) {
            return
        }
    }

    public class RecommendResponse {
        var title: String = ""
        var subTitle: String = ""
        var images: List<String> = ArrayList()
        var url: String = ""
        var time: Long = 0
        var type: Int = 0

        override fun toString(): String = "name->$title,defaultVersionName->$subTitle,time->$time,url->$url,type->$type"
    }

    public class MyPagerAdapter : PagerAdapter {
        /*
         * 使用 pagerAdapter  注意几点：
         * 重写方法的时候是 含有 ViewGroup的方法 :
         * 适合用在 使用 layout 布局实现
         * instantiateItem(ViewGroup container, int position)
         * destroyItem(ViewGroup container, int position, Object object)
         *
         */
        private var views: MutableList<View> = ArrayList()

        constructor(views: List<View>) {
            this.views.addAll(views)
        }

        /**
         * 返回 页卡 的数量
         */
        override fun getCount(): Int = views.size

        /**
         *  判断 是 view 是否来自对象
         */
        override fun isViewFromObject(arg0: View, arg1: Any): Boolean = arg0 == arg1

        override fun getItemPosition(`object`: Any?): Int {
            return super.getItemPosition(`object`)
        }

        /**
         * 实例化 一个 页卡
         */
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            L.d("instantiateItem position -->" + position)
            // 添加一个 页卡
            container.addView(views[position])
            return views[position]
        }

        /**
         * 销毁 一个 页卡
         */
        override fun destroyItem(container: ViewGroup, position: Int, Object: Any) {
            // 删除
            L.d("destroyItem position -->" + position)
            container.removeView(views[position])
        }
    }
}