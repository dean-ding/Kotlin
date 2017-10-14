package cn.com.dean.server.data;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: tvt on 17/10/10 15:56
 */
public enum News
{
    INSTANCE;

    private static class RecommendResponse
    {
        String title = "";
        String subTitle = "";
        List<String> images = new ArrayList<>();
        String url = "";
        long time = 0;
        int type = 0;
    }

    public String GetRecommendData(String database, String table)
    {
        return GetData(database, table);
        //        String url = "http://www.easyicon.net/api/resizeApi.php?id=1209120&size=128";
        //        List<RecommendResponse> data = new ArrayList<>();
        //        {
        //            RecommendResponse recommendResponse = new RecommendResponse();
        //            recommendResponse.title = "玫瑰绽放";
        //            recommendResponse.subTitle = "罗斯";
        //            recommendResponse.time = System.currentTimeMillis();
        //            recommendResponse.images.add(url);
        //            recommendResponse.url = "http://www.easyicon.net/api/resizeApi.php?id=1209120&size=128";
        //            recommendResponse.type = 0;
        //            data.add(recommendResponse);
        //
        //            recommendResponse = new RecommendResponse();
        //            recommendResponse.title = "玫瑰绽放2";
        //            recommendResponse.subTitle = "罗斯";
        //            recommendResponse.time = System.currentTimeMillis();
        //            recommendResponse.images.add(url);
        //            recommendResponse.url = "http://www.easyicon.net/api/resizeApi.php?id=1207955&size=128";
        //            recommendResponse.type = 1;
        //            data.add(recommendResponse);
        //        }
        //        Gson gson = new Gson();
        //        return gson.toJson(data);
    }

    private String GetData(String database, String table)
    {
        // 驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数据库名scutcs
        String url = "jdbc:mysql://127.0.0.1:3306/" + database;
        // MySQL配置时的用户名
        String user = "root";
        // MySQL配置时的密码
        String password = "dinghugui";
        try
        {
            // 加载驱动程序
            Class.forName(driver);
            // 连续数据库
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("conn.isClosed() " + conn.isClosed());
            if (!conn.isClosed())
            {
                System.out.println("Succeeded connecting to the Database!");
            }
            // statement用来执行SQL语句
            Statement statement = conn.createStatement();
            // 要执行的SQL语句
            String sql = "select * from " + table;
            // 结果集
            ResultSet rs = statement.executeQuery(sql);
            List<RecommendResponse> data = new ArrayList<>();
            while (rs.next())
            {
                RecommendResponse recommendResponse = new RecommendResponse();
                recommendResponse.title = rs.getString("title");
                recommendResponse.subTitle = rs.getString("subTitle");
                recommendResponse.time = rs.getDate("time").getTime() + rs.getTime("time").getTime();
                recommendResponse.images.add(url);
                recommendResponse.url = rs.getString("images");
                recommendResponse.type = rs.getInt("type");
                data.add(recommendResponse);
            }
            rs.close();
            conn.close();
            return new Gson().toJson(data);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
