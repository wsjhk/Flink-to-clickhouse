package kafka_flink_clickhouse;

import java.sql.Connection;
import java.sql.SQLException;
import com.alibaba.druid.pool.DruidDataSource;


public class ClickHouseUtils {
    private static DruidDataSource dataSource;
    public static Connection getConnection(String ck_host, String db, String username, String password) throws SQLException {
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
        dataSource.setUrl("jdbc:clickhouse://" + ck_host + "/" + db);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        //设置初始化连接数，最大连接数，最小闲置数
        dataSource.setInitialSize(10);
        dataSource.setMaxActive(50);
        dataSource.setMinIdle(5);
        //返回连接
        return dataSource.getConnection();

//        private static Connection connection = null;
//        try {
//            Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
//            String url = "jdbc:clickhouse://clickhouse.xpu.xiaopeng.local:80/kafka";
//            String user = "clickhouse";
//            String password = "Xpu#Clickhouse@@2022";
//            connection = DriverManager.getConnection(url, user, password);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return connection;
    }
}

