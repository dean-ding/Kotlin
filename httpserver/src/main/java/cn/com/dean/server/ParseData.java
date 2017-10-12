package cn.com.dean.server;

import java.io.File;

import cn.com.dean.server.data.News;

/**
 * Created: tvt on 17/10/10 15:26
 */
public class ParseData
{
    public synchronized static String parseData(String type)
    {
        if (type.equals(File.separator + NetType.NEWS_TYPE + "&" + NetType.NEWS_TYPE_ITEM.TYPE_NEWS_RECOMMEND))
        {
            return News.GetRecommendData();
        }

        return "";
    }
}
