package cn.com.dean.kotlin.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.com.dean.kotlin.R

/**
 * Created: tvt on 17/9/14 18:17
 */
class DiscoverFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.discover, container, false)
        return view
    }
}