package cn.com.dean.kotlin.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.com.dean.kotlin.R
import cn.com.dean.kotlin.helper.GlobalUnit
import cn.com.dean.kotlin.resp.RespBase
import cn.com.dean.kotlin.thread.PoolThreadCallback
import cn.com.dean.kotlin.view.TitleView
import cn.com.dean.server.NetType
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
    private lateinit var mContentView: ViewPager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.news, container, false)

        mTitleView = view.findViewById(R.id.news_title_View) as TitleView
        mTitleView.OnClick(mTitleView.getChildAt(0))
        for (i in 0 until mTitleView.childCount) {
            mTitleView.getChildAt(i).setOnClickListener(TextClick())
        }
        mContentView = view.findViewById(R.id.news_content) as ViewPager

        loadData(NetType.NEWS_TYPE, NetType.NEWS_TYPE_ITEM.TYPE_NEWS_RECOMMEND)
        return view
    }

    inner class TextClick : View.OnClickListener {
        override fun onClick(v: View?) {
            mTitleView.OnClick(v)
        }
    }

    private fun loadData(type: String, subType: String) {
        val url = GlobalUnit.HTTP_URL_BASE + File.separator + type + "&" + subType

        val okHttpClient = OkHttpClient()
        okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS)
        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS)
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS)

        val request: Request = Request.Builder().url(url).addHeader("Connection", "close").build()
        val call = okHttpClient.newCall(request)
        call.enqueue(OkHttpCallback())
    }

    class OkHttpCallback : Callback {
        override fun onResponse(response: Response) {
            L.d("onResponse-->" + response.body().string())
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
}