package kafka_flink_clickhouse;

import java.util.Arrays;
import java.util.Date;


public class sls_k8s_audit_log {
    private String tag_pod_name_;
    private String tag_container_name_;
    private Object annotations;
    private String tag_pod_uid_;
    private String tag_container_ip_;
    private String apiVersion;
    private String __pack_meta__;
    private String tag_namespace_;
    private String tag__pack_id__;
    private String auditID;
    private Date requestReceivedTimestamp;
    private Integer __time__;
    private Object objectRef;
    private String __topic__;
    private String level;
    private String kind;
    private String __source__;
    private String verb;
    private String tag__user_defined_id__;
    private String userAgent;
    private String requestURI;
    private Object responseStatus;
    private Date stageTimestamp;
    private Object[] sourceIPs;
    private String tag__hostname__;
    private String stage;
    private String tag_audit;
    private String tag_node_ip_;
    private String tag_image_name_;
    private Object user;
    private String tag__path__;
    private String tag_node_name_;

    public sls_k8s_audit_log(String tag_pod_name_, String tag_container_name_, Object annotations, String tag_pod_uid_,
                             String tag_container_ip_, String apiVersion, String __pack_meta__, String tag_namespace_,
                             String tag__pack_id__, String auditID, Date requestReceivedTimestamp, Integer __time__,
                             Object objectRef, String __topic__, String level, String kind, String __source__, String verb,
                             String tag__user_defined_id__, String userAgent, String requestURI, Object responseStatus,
                             Date stageTimestamp, Object[] sourceIPs, String tag__hostname__, String stage, String tag_audit,
                             String tag_node_ip_, String tag_image_name_, Object user, String tag__path__, String tag_node_name_) {
        this.tag_pod_name_ = tag_pod_name_;
        this.tag_container_name_ = tag_container_name_;
        this.annotations = annotations;
        this.tag_pod_uid_ = tag_pod_uid_;
        this.tag_container_ip_ = tag_container_ip_;
        this.apiVersion = apiVersion;
        this.__pack_meta__ = __pack_meta__;
        this.tag_namespace_ = tag_namespace_;
        this.tag__pack_id__ = tag__pack_id__;
        this.auditID = auditID;
        this.requestReceivedTimestamp = requestReceivedTimestamp;
        this.__time__ = __time__;
        this.objectRef = objectRef;
        this.__topic__ = __topic__;
        this.level = level;
        this.kind = kind;
        this.__source__ = __source__;
        this.verb = verb;
        this.tag__user_defined_id__ = tag__user_defined_id__;
        this.userAgent = userAgent;
        this.requestURI = requestURI;
        this.responseStatus = responseStatus;
        this.stageTimestamp = stageTimestamp;
        this.sourceIPs = sourceIPs;
        this.tag__hostname__ = tag__hostname__;
        this.stage = stage;
        this.tag_audit = tag_audit;
        this.tag_node_ip_ = tag_node_ip_;
        this.tag_image_name_ = tag_image_name_;
        this.user = user;
        this.tag__path__ = tag__path__;
        this.tag_node_name_ = tag_node_name_;
    }

    public String getTag_pod_name_() {
        return tag_pod_name_;
    }

    public void setTag_pod_name_(String tag_pod_name_) {
        this.tag_pod_name_ = tag_pod_name_;
    }

    public String getTag_container_name_() {
        return tag_container_name_;
    }

    public void setTag_container_name_(String tag_container_name_) {
        this.tag_container_name_ = tag_container_name_;
    }

    public String getAnnotations() {
        return annotations.toString();
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    public String getTag_pod_uid_() {
        return tag_pod_uid_;
    }

    public void setTag_pod_uid_(String tag_pod_uid_) {
        this.tag_pod_uid_ = tag_pod_uid_;
    }

    public String getTag_container_ip_() {
        return tag_container_ip_;
    }

    public void setTag_container_ip_(String tag_container_ip_) {
        this.tag_container_ip_ = tag_container_ip_;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String get__pack_meta__() {
        return __pack_meta__;
    }

    public void set__pack_meta__(String __pack_meta__) {
        this.__pack_meta__ = __pack_meta__;
    }

    public String getTag_namespace_() {
        return tag_namespace_;
    }

    public void setTag_namespace_(String tag_namespace_) {
        this.tag_namespace_ = tag_namespace_;
    }

    public String getTag__pack_id__() {
        return tag__pack_id__;
    }

    public void setTag__pack_id__(String tag__pack_id__) {
        this.tag__pack_id__ = tag__pack_id__;
    }

    public String getAuditID() {
        return auditID;
    }

    public void setAuditID(String auditID) {
        this.auditID = auditID;
    }

    public Date getRequestReceivedTimestamp() {
        return requestReceivedTimestamp;
    }

    public void setRequestReceivedTimestamp(Date requestReceivedTimestamp) {
        this.requestReceivedTimestamp = requestReceivedTimestamp;
    }

    public Integer get__time__() {
        return __time__;
    }

    public void set__time__(Integer __time__) {
        this.__time__ = __time__;
    }

    public String getObjectRef() {
        return objectRef.toString();
    }

    public void setObjectRef(String objectRef) {
        this.objectRef = objectRef;
    }

    public String get__topic__() {
        return __topic__;
    }

    public void set__topic__(String __topic__) {
        this.__topic__ = __topic__;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String get__source__() {
        return __source__;
    }

    public void set__source__(String __source__) {
        this.__source__ = __source__;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getTag__user_defined_id__() {
        return tag__user_defined_id__;
    }

    public void setTag__user_defined_id__(String tag__user_defined_id__) {
        this.tag__user_defined_id__ = tag__user_defined_id__;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getResponseStatus() {
        return responseStatus.toString();
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Date getStageTimestamp() {
        return stageTimestamp;
    }

    public void setStageTimestamp(Date stageTimestamp) {
        this.stageTimestamp = stageTimestamp;
    }

    public Object[] getSourceIPs() {
        return Arrays.stream(sourceIPs).toArray();
    }

    public void setSourceIPs(Object[] sourceIPs) {
        this.sourceIPs = sourceIPs;
    }

    public String getTag__hostname__() {
        return tag__hostname__;
    }

    public void setTag__hostname__(String tag__hostname__) {
        this.tag__hostname__ = tag__hostname__;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getTag_audit() {
        return tag_audit;
    }

    public void setTag_audit(String tag_audit) {
        this.tag_audit = tag_audit;
    }

    public String getTag_node_ip_() {
        return tag_node_ip_;
    }

    public void setTag_node_ip_(String tag_node_ip_) {
        this.tag_node_ip_ = tag_node_ip_;
    }

    public String getTag_image_name_() {
        return tag_image_name_;
    }

    public void setTag_image_name_(String tag_image_name_) {
        this.tag_image_name_ = tag_image_name_;
    }

    public String getUser() {
        return user.toString();
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTag__path__() {
        return tag__path__;
    }

    public void setTag__path__(String tag__path__) {
        this.tag__path__ = tag__path__;
    }

    public String getTag_node_name_() {
        return tag_node_name_;
    }

    public void setTag_node_name_(String tag_node_name_) {
        this.tag_node_name_ = tag_node_name_;
    }
}
