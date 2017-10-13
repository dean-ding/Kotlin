package cn.com.dean.kotlin.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cn.com.dean.kotlin.R
import cn.com.dean.kotlin.fragment.NewsFragment
import com.squareup.picasso.Picasso

/**
 * Created: tvt on 17/10/13 11:10
 */
class ListViewAdapter(context: Context) : BaseAdapter() {

    private var mContext: Context = context
    private var mData: MutableList<NewsFragment.RecommendResponse> = ArrayList()

    fun setData(data: List<NewsFragment.RecommendResponse>) {
        mData.clear()
        mData.addAll(data)

        this.notifyDataSetChanged()
    }

    private class ViewHolder {
        lateinit var titleView: TextView
        lateinit var keywordView: TextView
        lateinit var imageView: ImageView
        lateinit var lineView: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.news_listview, null)
            holder = ViewHolder()
            holder.titleView = view.findViewById(R.id.list_view_title) as TextView
            holder.keywordView = view.findViewById(R.id.list_view_keyword) as TextView
            holder.imageView = view.findViewById(R.id.list_view_image) as ImageView
            holder.lineView = view.findViewById(R.id.list_view_line) as TextView
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val recommend: NewsFragment.RecommendResponse = mData[position]
        holder.titleView.text = recommend.title
        holder.keywordView.text = recommend.subTitle
        Picasso.with(mContext).load(recommend.url).placeholder(R.drawable.ic_launcher).into(holder.imageView)

        if (position == mData.size - 1) {
            holder.lineView.visibility = View.INVISIBLE
        }
        return view
    }

    override fun getItem(position: Int): Any = mData[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = mData.size

}