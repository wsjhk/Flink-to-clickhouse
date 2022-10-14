package kafka_flink_clickhouse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;


public class ClickHouseSqlSink extends RichSinkFunction<String> implements CheckpointedFunction {
    private PreparedStatement ps;
    private Connection connection;
    private int i;
    private static final int count = 100000;
    private final String _ck_host, _db, _username, _password;
    private final String[] meta_columns = {"_time","zone","cluster","log_format","log_type","instance"};

    ClickHouseSqlSink(String ck_host, String db, String username, String password){
        this._ck_host = ck_host;
        this._db = db;
        this._username = username;
        this._password = password;
    }

    public boolean checkTable(String t_name) throws Exception {
        boolean rs = false;
        String checkSql = "show tables like '" + t_name + "';";
//        System.out.println(checkSql);
        if (connection.createStatement().executeQuery(checkSql).next()) {
            rs = true;
        }

        return rs;
    }

    public String[] queryColumns(String t_name) throws Exception {
        String querySql = "select * from " + t_name + " limit 1";
//        System.out.println(querySql);
        ResultSet rs = connection.createStatement().executeQuery(querySql);
        ResultSetMetaData rss = rs.getMetaData();
        int columnCount = rss.getColumnCount();
        String[] orgTabCols = new String[columnCount];
        for (int i = 1; i <= columnCount; ++i) {
            orgTabCols[i-1] = rss.getColumnName(i);
//            System.out.println(orgTabCols[i-1]);
        }
        return orgTabCols;
    }

    public void createTableandView(String t_name, String[] columns) throws Exception {
        String createtableSql = "create table if not exists " + t_name + " \n" +
                "(\n" +
                "    `_time` DateTime64(3) CODEC(DoubleDelta, LZ4),\n" +
                "    `zone` LowCardinality(String),\n" +
                "    `cluster` LowCardinality(String),\n" +
                "    `log_format` Int8,\n" +
                "    `log_type` LowCardinality(String) CODEC(ZSTD(1)),\n" +
                "    `instance` LowCardinality(String) CODEC(ZSTD(1)),\n" +
                "    `string.keys` Array(String) CODEC(ZSTD(1)),\n" +
                "    `string.values`  Array(String) CODEC(ZSTD(1))\n" +
//                "    PROJECTION p_clusters_usually (SELECT cluster, count(), min(_time), max(_time), groupUniqArrayArray(string.keys)GROUP BY cluster)\n" +
                ")\n" +
                "ENGINE = MergeTree\n" +
                "PARTITION BY toYYYYMMDD(_time)\n" +
                "ORDER BY _time\n" +
                "TTL toDateTime(_time) + toIntervalDay(3) DELETE;";
//        System.out.println(createtableSql);
        StringBuilder subsql= new StringBuilder();
        for (String column : columns) {
//            System.out.println(column);
            if (!Arrays.asList(meta_columns).contains(column)) {
                subsql.append("string.values[indexOf(string.keys,'").append(column).append("')] as ").append(column.replace("@","")).append(",");
            }
        }
        String createviewSql = "CREATE MATERIALIZED VIEW " + t_name + "_view \n" +
                "ENGINE = MergeTree\n" +
                "ORDER BY _time\n" +
                "TTL toDateTime(_time) + toIntervalDay(180) DELETE\n" +
                "AS SELECT\n" +
                "  _time,\n" +
                "  zone,\n" +
                "  cluster,\n" +
                "  log_format,\n" +
                "  log_type,\n" +
                "  instance,\n" +
                subsql.deleteCharAt(subsql.length() - 1) + "\n" +
                "FROM "+ _db + "." + t_name + ";";
//        System.out.println(createviewSql);
        connection.createStatement().executeUpdate(createtableSql);
        connection.createStatement().executeUpdate(createviewSql);
    }

    public void updatedView(String t_name, String[] orgin_columns, String[] add_columns) throws Exception {
        StringBuilder subsql= new StringBuilder();
        for(String orgin_column : orgin_columns){
            if (!Arrays.asList(meta_columns).contains(orgin_column)) {
                subsql.append("string.values[indexOf(string.keys,'").append(orgin_column).append("')] as ").append(orgin_column).append(",");
            }
        }
        for(String add_column : add_columns){
            if (!Arrays.asList(meta_columns).contains(add_column)) {
                connection.createStatement().executeUpdate("alter table `.inner." + t_name + "_view` add column " + add_column + " String;");
                subsql.append("string.values[indexOf(string.keys,'").append(add_column).append("')] as ").append(add_column).append(",");
            }
        }
        String updateviewSql = "ATTACH MATERIALIZED VIEW " + t_name + "_view \n" +
                "ENGINE = MergeTree\n" +
                "ORDER BY _time\n" +
                "TTL toDateTime(_time) + toIntervalDay(180) DELETE\n" +
                "AS SELECT\n" +
                "  _time,\n" +
                "  zone,\n" +
                "  cluster,\n" +
                "  log_format,\n" +
                "  log_type,\n" +
                "  instance,\n" +
                subsql.deleteCharAt(subsql.length() - 1) + "\n" +
                "FROM "+ _db + "." + t_name + ";";
        System.out.println(updateviewSql);
        connection.createStatement().executeUpdate("detach table " + t_name + "_view;");
        connection.createStatement().executeUpdate(updateviewSql);
    }

    public String[] arraySubtract(String[] new_columns, String[] org_columns) {
        ArrayList<String> list = new ArrayList<String>();
        //选出属于数组new_columns但不属于数组org_columns的元素
        for (String new_column : new_columns) {
            boolean bContained = false;
            for (String org_column : org_columns) {
                if (Objects.equals(new_column, org_column)) {
                    bContained = true;
                    break;
                }
            }
            if (!bContained) {
                list.add(new_column);
            }
        }

        String[] add_columns = new String[list.size()];
        for(int i = 0; i < list.size(); ++i) {
            add_columns[i] = list.get(i);
            System.out.println(add_columns[i]);
        }
        return add_columns;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        //获取CK数据库连接
        connection = ClickHouseUtils.getConnection(this._ck_host, this._db, this._username, this._password);
        connection.setAutoCommit(false);
//        String sql = "insert into gz_local_xpu_k8s_cluster_k8s_log_1 values(?,?,?,?,?,?,?,?)";
//        ps = connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception {
        super.close();
        //关闭并释放资源
        if (connection != null) {
            connection.close();
        }
        if (ps != null) {
            ps.close();
        }

    }

    //  只在数据到达sink算子时，才会被触发
    @Override
    public void invoke(String value, Context context) throws Exception {
//        Gson gson = new Gson();
        value = value.replace("__tag__:","tag");
        JsonObject jsonObject = new JsonParser().parse(value).getAsJsonObject();
        JsonObject k8s_log_sub_json = jsonObject.getAsJsonObject("kubernetes");
        boolean k8s_audit_log_sub_json = jsonObject.has("objectRef");
        boolean k8s_ingress_log_sub_json = jsonObject.has("upstream_addr");

        if (k8s_log_sub_json != null){
            for (Map.Entry<String, JsonElement> stringJsonElementEntry : k8s_log_sub_json.entrySet()) {
                String tmp_key = "kubernetes_" + ((Map.Entry<?, ?>) stringJsonElementEntry).getKey();
                jsonObject.add(tmp_key, (JsonElement) ((Map.Entry<?, ?>) stringJsonElementEntry).getValue());
            }
            jsonObject.remove("kubernetes");
        } else if (k8s_audit_log_sub_json){
            System.out.println("k8s_audit_log");
        } else if (k8s_ingress_log_sub_json){
            System.out.println("k8s_ingress_log");
        } else {
            System.out.println("other logs");
        }

        String table_name = (jsonObject.get("zone").toString() + "_" + jsonObject.get("cluster").toString() + "_" +
                jsonObject.get("log_type").toString() + "_" + jsonObject.get("log_format").toString()).replace("-","_").replace("\"", "");
        String sql = "insert into " + table_name + " values(?,?,?,?,?,?,?,?)";
        ps = connection.prepareStatement(sql); // 是否会导致过多连接和资源不释放而堆积，导致未知问题

        Set<Map.Entry<String, JsonElement>> keySet = jsonObject.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = keySet.iterator();
        int index = 0;
        String[] arraykey = new String[keySet.size()], arrayvalue = new String[keySet.size()];
        while(iterator.hasNext()) {
            Map.Entry<String, JsonElement> k_v = iterator.next();
            arraykey[index] = k_v.getKey().replace("@","");
            arrayvalue[index] = String.valueOf(k_v.getValue());
//            System.out.println(arraykey[index]);
//            System.out.println(arrayvalue[index]);
            index++;
        }
//        System.out.println(table_name);
        if (!checkTable(table_name)){
            System.out.println("create table and view");
            createTableandView(table_name, arraykey);
        }

        String[] orgin_columns = queryColumns(table_name + "_view");
        String[] add_columns = arraySubtract(arraykey, orgin_columns);
        System.out.println("add columns: " + Arrays.toString(add_columns));
        if ( add_columns.length != 0 ) {
            updatedView(table_name, orgin_columns, add_columns);
        }

        if (i <= count) {
            ps.setTimestamp(1, new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(jsonObject.get("_time").toString().replace("\"","")).getTime()));
            ps.setString(2, String.valueOf(jsonObject.get("zone")));
            ps.setString(3, String.valueOf(jsonObject.get("cluster")));
            ps.setString(4, String.valueOf(jsonObject.get("log_format")));
            ps.setString(5, String.valueOf(jsonObject.get("log_type")));
            ps.setString(6, String.valueOf(jsonObject.get("instance")));
            ps.setArray(7, connection.createArrayOf("Array(String)", arraykey));
            ps.setArray(8, connection.createArrayOf("Array(String)", arrayvalue));
            ps.addBatch();
            i++;
        }

        //攒够10w条数据写入CK。
        if (i >= count) {
            ps.executeBatch();
            connection.commit();
            ps.clearBatch();
            System.out.printf("To execute Batch for %d records ...%n", i);
            i = 0;
        }
    }

    // 定时器功能：
    // 由于executeBatch写入ck操作是在invoke()方法中触发的，而invoke方法只有在数据到达sink算子时才触发。
    // 所以在数据比较稀疏时，数据的时效性取决于两条数据到达的时间间隔。为了解决这个问题，采用在自定义Sink类中继承CheckpointedFunction接口，
    // 实现其中的snapshotState方法。checkpointedFunction是实现operator state的核心方法，其中定义了两个方法：snapshotState和initialState
    // snapshotState在checkpoint的时候会被调用，用于快照状态，通常用于flush、commit、synchronize外部系统
    // initializeState在从状态中恢复时会被调用。
    // 计划在每次checkpoint的时候，写入ck一次。设置checkpoint的间隔为30s，所以每30s会刷新数据到ck。
    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
        ps.executeBatch();
        connection.commit();
        ps.clearBatch();
        i = 0;
        System.out.println("Time to execute Batch for 30s timer done ...");
    }

    @Override
    public void initializeState(FunctionInitializationContext functionInitializationContext) throws Exception {

    }
}