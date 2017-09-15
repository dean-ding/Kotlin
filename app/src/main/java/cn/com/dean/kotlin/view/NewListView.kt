package cn.com.dean.kotlin.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView

/**
 * Created: tvt on 17/9/15 09:26
 */
class NewListView : ListView {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    private var mAdapter: NewsAdapter

    init {
        mAdapter = NewsAdapter(context)
        this.adapter = mAdapter
    }

    fun onReceiveData(data: Class<Int>) {
        mAdapter.updateData(data)
        mAdapter.notifyDataSetChanged()
    }

    inner class NewsAdapter(context: Context) : BaseAdapter() {

        private val mContext = context


        fun updateData(data: Class<Int>) {

            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            return convertView!!
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 10
        }

    }

}