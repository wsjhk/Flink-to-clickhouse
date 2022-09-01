package kafka_flink_clickhouse;

import java.util.Date;


public class sls_nginx_ingress_k8s_log {
    private Date _time_;
    private String upstream_addr;
    private Integer body_bytes_sent;
    private String http_user_agent;
    private String remote_user;
    private String req_id;
    private Integer upstream_status;
    private Float request_time;
    private String _container_ip_;
    private String host;
    private String client_ip;
    private String _pod_name_;
    private String _image_name_;
    private String _container_name_;
    private String method;
    private String _namespace_;
    private Integer upstream_response_length;
    private String _source_;
    private String version;
    private String x_forward_for;
    private String url;
    private Integer request_length;
    private String http_referer;
    private String _pod_uid_;
    private String proxy_upstream_name;
    private Float upstream_response_time;
    private String time;
    private Integer status;

    public sls_nginx_ingress_k8s_log(Date _time_, String upstream_addr, Integer body_bytes_sent, String http_user_agent, String remote_user, String req_id, Integer upstream_status, Float request_time, String _container_ip_, String host, String client_ip, String _pod_name_, String _image_name_, String _container_name_, String method, String _namespace_, Integer upstream_response_length, String _source_, String version, String x_forward_for, String url, Integer request_length, String http_referer, String _pod_uid_, String proxy_upstream_name, Float upstream_response_time, String time, Integer status) {
        this._time_ = _time_;
        this.upstream_addr = upstream_addr;
        this.body_bytes_sent = body_bytes_sent;
        this.http_user_agent = http_user_agent;
        this.remote_user = remote_user;
        this.req_id = req_id;
        this.upstream_status = upstream_status;
        this.request_time = request_time;
        this._container_ip_ = _container_ip_;
        this.host = host;
        this.client_ip = client_ip;
        this._pod_name_ = _pod_name_;
        this._image_name_ = _image_name_;
        this._container_name_ = _container_name_;
        this.method = method;
        this._namespace_ = _namespace_;
        this.upstream_response_length = upstream_response_length;
        this._source_ = _source_;
        this.version = version;
        this.x_forward_for = x_forward_for;
        this.url = url;
        this.request_length = request_length;
        this.http_referer = http_referer;
        this._pod_uid_ = _pod_uid_;
        this.proxy_upstream_name = proxy_upstream_name;
        this.upstream_response_time = upstream_response_time;
        this.time = time;
        this.status = status;
    }

    public Date get_time_() {
        return _time_;
    }

    public void set_time_(Date _time_) {
        this._time_ = _time_;
    }

    public String getUpstream_addr() {
        return upstream_addr;
    }

    public void setUpstream_addr(String upstream_addr) {
        this.upstream_addr = upstream_addr;
    }

    public Integer getBody_bytes_sent() {
        return body_bytes_sent;
    }

    public void setBody_bytes_sent(Integer body_bytes_sent) {
        this.body_bytes_sent = body_bytes_sent;
    }

    public String getHttp_user_agent() {
        return http_user_agent;
    }

    public void setHttp_user_agent(String http_user_agent) {
        this.http_user_agent = http_user_agent;
    }

    public String getRemote_user() {
        return remote_user;
    }

    public void setRemote_user(String remote_user) {
        this.remote_user = remote_user;
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public Integer getUpstream_status() {
        return upstream_status;
    }

    public void setUpstream_status(Integer upstream_status) {
        this.upstream_status = upstream_status;
    }

    public Float getRequest_time() {
        return request_time;
    }

    public void setRequest_time(Float request_time) {
        this.request_time = request_time;
    }

    public String get_container_ip_() {
        return _container_ip_;
    }

    public void set_container_ip_(String _container_ip_) {
        this._container_ip_ = _container_ip_;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String get_pod_name_() {
        return _pod_name_;
    }

    public void set_pod_name_(String _pod_name_) {
        this._pod_name_ = _pod_name_;
    }

    public String get_image_name_() {
        return _image_name_;
    }

    public void set_image_name_(String _image_name_) {
        this._image_name_ = _image_name_;
    }

    public String get_container_name_() {
        return _container_name_;
    }

    public void set_container_name_(String _container_name_) {
        this._container_name_ = _container_name_;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String get_namespace_() {
        return _namespace_;
    }

    public void set_namespace_(String _namespace_) {
        this._namespace_ = _namespace_;
    }

    public Integer getUpstream_response_length() {
        return upstream_response_length;
    }

    public void setUpstream_response_length(Integer upstream_response_length) {
        this.upstream_response_length = upstream_response_length;
    }

    public String get_source_() {
        return _source_;
    }

    public void set_source_(String _source_) {
        this._source_ = _source_;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getX_forward_for() {
        return x_forward_for;
    }

    public void setX_forward_for(String x_forward_for) {
        this.x_forward_for = x_forward_for;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRequest_length() {
        return request_length;
    }

    public void setRequest_length(Integer request_length) {
        this.request_length = request_length;
    }

    public String getHttp_referer() {
        return http_referer;
    }

    public void setHttp_referer(String http_referer) {
        this.http_referer = http_referer;
    }

    public String get_pod_uid_() {
        return _pod_uid_;
    }

    public void set_pod_uid_(String _pod_uid_) {
        this._pod_uid_ = _pod_uid_;
    }

    public String getProxy_upstream_name() {
        return proxy_upstream_name;
    }

    public void setProxy_upstream_name(String proxy_upstream_name) {
        this.proxy_upstream_name = proxy_upstream_name;
    }

    public Float getUpstream_response_time() {
        return upstream_response_time;
    }

    public void setUpstream_response_time(Float upstream_response_time) {
        this.upstream_response_time = upstream_response_time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
