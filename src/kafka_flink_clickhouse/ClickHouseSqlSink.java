package kafka_flink_clickhouse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;


public class ClickHouseSqlSink extends RichSinkFunction<String> implements CheckpointedFunction {
    private PreparedStatement ps;
    private Connection connection;
    private int i, j, k;
    private static final int count = 100000;
    private String _topic, _ck_host, _db, _username, _password;
//    private ArrayList<String> _topics;

    ClickHouseSqlSink(String topic, String ck_host, String db, String username, String password){
        this._topic = topic;
        this._ck_host = ck_host;
        this._db = db;
        this._username = username;
        this._password = password;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        //获取CK数据库连接
        connection = ClickHouseUtils.getConnection(this._ck_host, this._db, this._username, this._password);

        String sql;
        if (this._topic.contains("sls-k8s-audit")){
            sql = "insert into " + this._topic.replace("-","_") +"_distributed (tag_pod_name_,tag_container_name_,annotations," +
                    "tag_pod_uid_,tag_container_ip_,apiVersion,__pack_meta__,tag_namespace_,tag__pack_id__,auditID," +
                    "requestReceivedTimestamp,__time__,objectRef,__topic__,level,kind,__source__,verb,tag__user_defined_id__," +
                    "userAgent,requestURI,responseStatus,stageTimestamp,sourceIPs,tag__hostname__,stage,tag_audit,tag_node_ip_," +
                    "tag_image_name_,user,tag__path__,tag_node_name_) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        } else if (this._topic.contains("sls-nginx-ingress")) {
            sql = "insert into " + this._topic.replace("-","_") +"_distributed (_time_,upstream_addr,body_bytes_sent,http_user_agent,remote_user," +
                    "req_id,upstream_status,request_time,_container_ip_,host,client_ip,_pod_name_,_image_name_,_container_name_,method," +
                    "_namespace_,upstream_response_length,_source_,version,x_forward_for,url,request_length,http_referer,_pod_uid_," +
                    "proxy_upstream_name,upstream_response_time,time,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        } else {
            sql = "insert into " + this._topic.replace("-","_") +"_distributed (timestamp,kubernetes_container_name,kubernetes_host," +
                    "kubernetes_namespace_name,kubernetes_pod_name,message,stream,tag) values (?,?,?,?,?,?,?,?);";
        }

        ps = connection.prepareStatement(sql);
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

    // 只在数据到达sink算子时，才会被触发
    @Override
    public void invoke(String value, Context context) throws Exception {
        Gson gson = new Gson();
        value = value.replace("__tag__:","tag");
        JsonObject jsonObject = new JsonParser().parse(value).getAsJsonObject();
        JsonObject k8s_log_sub_json = jsonObject.getAsJsonObject("kubernetes");
        boolean k8s_audit_log_sub_json = jsonObject.has("objectRef");
        boolean k8s_ingress_log_sub_json = jsonObject.has("upstream_addr");

        if (k8s_log_sub_json != null){
            System.out.println("===k8s_log===");
            if (i <= count) {
                for (Map.Entry<String, JsonElement> stringJsonElementEntry : k8s_log_sub_json.entrySet()) {
                    String tmp_key = "kubernetes_" + ((Map.Entry<?, ?>) stringJsonElementEntry).getKey();
                    jsonObject.add(tmp_key, (JsonElement) ((Map.Entry<?, ?>) stringJsonElementEntry).getValue());
                }
                jsonObject.remove("kubernetes");
                k8s_log k8slogdata = gson.fromJson(jsonObject.toString(), k8s_log.class);
//                k8s_log k8slogdata = JSONObject.parseObject(jsonObject.toString(), k8s_log.class);
                ps.setTimestamp(1, new java.sql.Timestamp(k8slogdata.getTimestamp().getTime()));
                ps.setString(2, k8slogdata.getKubernetes_container_name());
                ps.setString(3, k8slogdata.getKubernetes_host());
                ps.setString(4, k8slogdata.getKubernetes_namespace_name());
                ps.setString(5, k8slogdata.getKubernetes_pod_name());
                ps.setString(6, k8slogdata.getMessage());
                ps.setString(7, k8slogdata.getStream());
                ps.setString(8, k8slogdata.getTag());
                ps.addBatch();
                i++;
            }
        } else if (k8s_audit_log_sub_json){
            System.out.println("---k8s_audit_log---");
            if (j <= count) {
                sls_k8s_audit_log k8sAuditLoglogdata = gson.fromJson(jsonObject.toString(), sls_k8s_audit_log.class);
//                sls_k8s_audit_log k8sAuditLoglogdata = JSONObject.parseObject(jsonObject.toString(), sls_k8s_audit_log.class);
                ps.setString(1, k8sAuditLoglogdata.getTag_pod_name_());
                ps.setString(2, k8sAuditLoglogdata.getTag_container_name_());
                ps.setString(3, k8sAuditLoglogdata.getAnnotations());
                ps.setString(4, k8sAuditLoglogdata.getTag_pod_uid_());
                ps.setString(5, k8sAuditLoglogdata.getTag_container_ip_());
                ps.setString(6, k8sAuditLoglogdata.getApiVersion());
                ps.setString(7, k8sAuditLoglogdata.get__pack_meta__());
                ps.setString(8, k8sAuditLoglogdata.getTag_namespace_());
                ps.setString(9, k8sAuditLoglogdata.getTag__pack_id__());
                ps.setString(10, k8sAuditLoglogdata.getAuditID());
                ps.setTimestamp(11, new java.sql.Timestamp(k8sAuditLoglogdata.getRequestReceivedTimestamp().getTime()));
                ps.setInt(12, k8sAuditLoglogdata.get__time__());
                ps.setString(13, k8sAuditLoglogdata.getObjectRef());
                ps.setString(14, k8sAuditLoglogdata.get__topic__());
                ps.setString(15, k8sAuditLoglogdata.getLevel());
                ps.setString(16, k8sAuditLoglogdata.getKind());
                ps.setString(17, k8sAuditLoglogdata.get__source__());
                ps.setString(18, k8sAuditLoglogdata.getVerb());
                ps.setString(19, k8sAuditLoglogdata.getTag__user_defined_id__());
                ps.setString(20, k8sAuditLoglogdata.getUserAgent());
                ps.setString(21, k8sAuditLoglogdata.getRequestURI());
                ps.setString(22, k8sAuditLoglogdata.getResponseStatus());
                ps.setTimestamp(23, new java.sql.Timestamp(k8sAuditLoglogdata.getStageTimestamp().getTime()));
                ps.setArray(24, connection.createArrayOf("Array(String)", k8sAuditLoglogdata.getSourceIPs()));
                ps.setString(25, k8sAuditLoglogdata.getTag__hostname__());
                ps.setString(26, k8sAuditLoglogdata.getStage());
                ps.setString(27, k8sAuditLoglogdata.getTag_audit());
                ps.setString(28, k8sAuditLoglogdata.getTag_node_ip_());
                ps.setString(29, k8sAuditLoglogdata.getTag_image_name_());
                ps.setString(30, k8sAuditLoglogdata.getUser());
                ps.setString(31, k8sAuditLoglogdata.getTag__path__());
                ps.setString(32, k8sAuditLoglogdata.getTag_node_name_());
                ps.addBatch();
                j++;
            }
        } else if (k8s_ingress_log_sub_json){
            System.out.println("+++k8s_ingress_log+++");
            if (k <= count) {
                sls_nginx_ingress_k8s_log k8singresslogdata = gson.fromJson(jsonObject.toString(), sls_nginx_ingress_k8s_log.class);
//                sls_nginx_ingress_k8s_log k8singresslogdata = JSONObject.parseObject(jsonObject.toString(), sls_nginx_ingress_k8s_log.class);
                ps.setTimestamp(1, new java.sql.Timestamp(k8singresslogdata.get_time_().getTime()));
                ps.setString(2, k8singresslogdata.getUpstream_addr());
                ps.setInt(3, k8singresslogdata.getBody_bytes_sent());
                ps.setString(4, k8singresslogdata.getHttp_user_agent());
                ps.setString(5, k8singresslogdata.getRemote_user());
                ps.setString(6, k8singresslogdata.getReq_id());
                ps.setInt(7, k8singresslogdata.getUpstream_status());
                ps.setFloat(8, k8singresslogdata.getRequest_time());
                ps.setString(9, k8singresslogdata.get_container_ip_());
                ps.setString(10, k8singresslogdata.getHost());
                ps.setString(11, k8singresslogdata.getClient_ip());
                ps.setString(12, k8singresslogdata.get_pod_name_());
                ps.setString(13, k8singresslogdata.get_image_name_());
                ps.setString(14, k8singresslogdata.get_container_name_());
                ps.setString(15, k8singresslogdata.getMethod());
                ps.setString(16, k8singresslogdata.get_namespace_());
                ps.setInt(17, k8singresslogdata.getUpstream_response_length());
                ps.setString(18, k8singresslogdata.get_source_());
                ps.setString(19, k8singresslogdata.getVersion());
                ps.setString(20, k8singresslogdata.getX_forward_for());
                ps.setString(21, k8singresslogdata.getUrl());
                ps.setInt(22, k8singresslogdata.getRequest_length());
                ps.setString(23, k8singresslogdata.getHttp_referer());
                ps.setString(24, k8singresslogdata.get_pod_uid_());
                ps.setString(25, k8singresslogdata.getProxy_upstream_name());
                ps.setFloat(26, k8singresslogdata.getUpstream_response_time());
                ps.setString(27, k8singresslogdata.getTime());
                ps.setInt(28, k8singresslogdata.getStatus());
                ps.addBatch();
                k++;
            }
        } else {
            System.out.println("日志内容或者格式不符合CK表格式，请检查！\n" + value);
        }

        //攒够10w条数据写入CK。
        if (i >= count) {
            ps.executeBatch();
            System.out.println(String.format("k8s_log to execute Batch for %d records ...", i));
            i = 0;
        }
        if (j >= count) {
            ps.executeBatch();
            System.out.println(String.format("k8s_audit_log to execute Batch for %d records ...", j));
            j = 0;
        }
        if (k >= count) {
            ps.executeBatch();
            System.out.println(String.format("k8s_ingress_log to execute Batch for %d records ...", k));
            k = 0;
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
        i = 0;
        j = 0;
        k = 0;
        System.out.println("Time to execute Batch for 30s timer done ...");
    }

    @Override
    public void initializeState(FunctionInitializationContext functionInitializationContext) throws Exception {

    }
}