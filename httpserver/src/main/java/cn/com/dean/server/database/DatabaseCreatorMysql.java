package cn.com.dean.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created: tvt on 17/10/14 09:41
 */
public class DatabaseCreatorMysql
{
    private final static String USER_NAME = "root";
    private final static String USER_PASSWORD = "dinghugui";

    private void createDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");

        //一开始必须填一个已经存在的数据库
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8";
        Connection conn = DriverManager.getConnection(url, USER_NAME, USER_PASSWORD);
        Statement stat = conn.createStatement();
        //创建数据库hello
        stat.executeUpdate("create database News");

        //打开创建的数据库
        stat.close();
        conn.close();
        url = "jdbc:mysql://localhost:3306/News?useUnicode=true&characterEncoding=utf-8";
        conn = DriverManager.getConnection(url, USER_NAME, USER_PASSWORD);
        stat = conn.createStatement();
        //创建表test
        stat.executeUpdate("create table test(id int, name varchar(80))");
        //添加数据
        stat.executeUpdate("insert into test values(1, '张三')");
        stat.executeUpdate("insert into test values(2, '李四')");

        //查询数据
        ResultSet result = stat.executeQuery("select * from test");
        while (result.next())
        {
            System.out.println(result.getInt("id") + " " + result.getString("name"));
        }
        //关闭数据库
        result.close();
        stat.close();
        conn.close();
    }

    public int insert() throws SQLException
    {
        int i = 0;
        String sql = "insert into (表名)(列名1,列明2) values(?,?)";
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8";
        Connection conn = DriverManager.getConnection(url, USER_NAME, USER_PASSWORD);
        try
        {
            PreparedStatement preStmt = conn.prepareStatement(sql);
            preStmt.setString(1, "1111");
            preStmt.setString(2, "2222");//或者：preStmt.setInt(1,值);
            i = preStmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return i;//返回影响的行数，1为执行成功
    }
}
