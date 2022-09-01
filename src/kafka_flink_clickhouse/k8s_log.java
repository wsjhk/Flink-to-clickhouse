package kafka_flink_clickhouse;

import java.util.Date;

public class k8s_log {
    private Date time;
    private String kubernetes_container_name;
    private String kubernetes_host;
    private String kubernetes_namespace_name;
    private String kubernetes_pod_name;
    private String message;
    private String stream;
    private String tag;

    public k8s_log(String stream, Date time, String message, String tag, String kubernetes_container_name,
                   String kubernetes_namespace_name, String kubernetes_pod_name,
                   String kubernetes_host) {
        this.time = time;
        this.kubernetes_container_name = kubernetes_container_name;
        this.kubernetes_host = kubernetes_host;
        this.kubernetes_namespace_name = kubernetes_namespace_name;
        this.kubernetes_pod_name = kubernetes_pod_name;
        this.message = message;
        this.stream = stream;
        this.tag = tag;
    }

    public Date getTimestamp() {
        return time;
    }

    public void setTimestamp(Date time) {
        this.time = time;
    }

    public String getKubernetes_container_name() {
        return kubernetes_container_name;
    }

    public void setKubernetes_container_name(String kubernetes_container_name) {
        this.kubernetes_container_name = kubernetes_container_name;
    }

    public String getKubernetes_host() {
        return kubernetes_host;
    }

    public void setKubernetes_host(String kubernetes_host) {
        this.kubernetes_host = kubernetes_host;
    }

    public String getKubernetes_namespace_name() {
        return kubernetes_namespace_name;
    }

    public void setKubernetes_namespace_name(String kubernetes_namespace_name) {
        this.kubernetes_namespace_name = kubernetes_namespace_name;
    }

    public String getKubernetes_pod_name() {
        return kubernetes_pod_name;
    }

    public void setKubernetes_pod_name(String kubernetes_pod_name) {
        this.kubernetes_pod_name = kubernetes_pod_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
