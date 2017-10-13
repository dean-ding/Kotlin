package cn.com.dean.server.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: tvt on 17/10/10 15:56
 */
public class News
{
    private static class RecommendResponse
    {
        String title = "";
        String subTitle = "";
        List<String> images = new ArrayList<>();
        String url = "";
        long time = 0;
        int type = 0;
    }

    public static String GetRecommendData()
    {
        String url = "http://www.easyicon.net/api/resizeApi.php?id=1209120&size=128";
        List<RecommendResponse> data = new ArrayList<>();
        {
            RecommendResponse recommendResponse = new RecommendResponse();
            recommendResponse.title = "玫瑰绽放";
            recommendResponse.subTitle = "罗斯";
            recommendResponse.time = System.currentTimeMillis();
            recommendResponse.images.add(url);
            recommendResponse.url = "http://www.easyicon.net/api/resizeApi.php?id=1209120&size=128";
            recommendResponse.type = 0;
            data.add(recommendResponse);

            recommendResponse = new RecommendResponse();
            recommendResponse.title = "玫瑰绽放2";
            recommendResponse.subTitle = "罗斯";
            recommendResponse.time = System.currentTimeMillis();
            recommendResponse.images.add(url);
            recommendResponse.url = "http://www.easyicon.net/api/resizeApi.php?id=1207955&size=128";
            recommendResponse.type = 1;
            data.add(recommendResponse);
        }
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
