# Flink-to-clickhouse
use flink consumer kafka log msg and write to clickhouse

# Run Job
./flink run -c kafka_flink_clickhouse.Flink_Kafka_Source /root/Flink-Log-System-Job.jar --kafka kafka:9092 --topic sls-nginx-ingress-k8s-log --ck_host clickhouse:8123 --db kafka --username clickhouse --password xxx
