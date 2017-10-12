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
        String name = "";
        String defaultVersionName = "1.1";
        long time = 0;
    }

    public static String GetRecommendData()
    {
        List<RecommendResponse> data = new ArrayList<>();
        {
            RecommendResponse recommendResponse = new RecommendResponse();
            recommendResponse.name = "US112";
            recommendResponse.defaultVersionName = "1.2.1";
            recommendResponse.time = 201710111658L;
            data.add(recommendResponse);

            recommendResponse = new RecommendResponse();
            recommendResponse.name = "CA23";
            recommendResponse.defaultVersionName = "1.2.0";
            recommendResponse.time = 201710111658L;
            data.add(recommendResponse);
        }
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
