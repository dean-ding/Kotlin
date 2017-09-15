package cn.com.dean.kotlin.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.com.dean.kotlin.R
import cn.com.dean.kotlin.view.TitleView

/**
 * Created: tvt on 17/9/14 18:17
 */
class MatchFragment : Fragment() {

    private lateinit var mTitleView: TitleView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.match, container, false)

        mTitleView = view.findViewById(R.id.match_title_view) as TitleView
        mTitleView.OnClick(mTitleView.getChildAt(1))

        return view
    }

}