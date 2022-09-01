package kafka_flink_clickhouse;


public class DynamicSchemaRecord<T> {
    private T value;

    public DynamicSchemaRecord(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }
}
