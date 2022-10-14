# Flink-to-clickhouse
use flink consumer kafka log msg and write to clickhouse

## 实现功能：
	1)自动根据固定日志字段动态建表和对应的物化视图

	2)动态更新物化视图表结构适配日志的动态字段

	3)支持批量写和定时写

	4)支持消费多个topic

	5)clickvisual支持物化视图的查询


# Run Job
./flink run -c kafka_flink_clickhouse.Flink_Kafka_Source /root/Flink-Log-System-Job.jar --kafka kafka:9092 --topics sls-nginx-ingress-k8s-log,sls-k8s-audit-k8s-log,flinktock --ck_host clickhouse:8123 --db kafka --username clickhouse --password xxx

