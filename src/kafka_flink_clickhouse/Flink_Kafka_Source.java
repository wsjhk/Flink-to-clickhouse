package kafka_flink_clickhouse;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class Flink_Kafka_Source {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String kafka = parameterTool.get("kafka");
        List<String> topics = new ArrayList<>(Arrays.asList(parameterTool.get("topics").split(",")));
        String ck_host = parameterTool.get("ck_host");
        String db = parameterTool.get("db");
        String username = parameterTool.get("username");
        String password = parameterTool.get("password");

        final StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        streamEnv.getCheckpointConfig().setMaxConcurrentCheckpoints(5);
        streamEnv.getCheckpointConfig().setCheckpointInterval(1000);
        // 为了保证数据的一致性，我们开启Flink的checkpoint一致性检查点机制，保证容错
        streamEnv.enableCheckpointing(30000);
//        streamEnv.setParallelism(1);

        // TODO 将kafka作为数据源
        // 生产环境中，一般就是用flink消费kafka中的数据，完成实时数据计算。（离线一般用spark）
        // Flink自带专门的kafka数据源对象
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka);
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka_flink_to_ck");
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>(
                topics,
                new SimpleStringSchema(), // 这里可以使用自定义序列化获取kafka的topic，partition，offset等元数据信息和消息的详细内容
                prop
        );
        // 开启kafka-offset检查点状态保存机制.一旦这个检查点开启，那么之前配置的 auto.commit.enable = true的配置就会自动失效
        kafkaSource.setCommitOffsetsOnCheckpoints(true);
        // 将kafka数据源绑定到flink中
        DataStreamSource<String> kafkaDS = streamEnv.addSource(kafkaSource);

        // TODO Transform 转换算子 => 封装逻辑方法
        SingleOutputStreamOperator<String> transformDS = kafkaDS.map(new RichMapFunction<String, String>() {
            @Override
            public void open(Configuration parameters) throws Exception {
                System.out.println("open...");
            }
            @Override
            public String map(String transform_str) throws Exception {
                // 扩展点：数据处理和转换等操作
                return transform_str;
            }
            @Override
            public void close() throws Exception {
                System.out.println("close...");
            }
        });

        // add Sink
        transformDS.addSink(new ClickHouseSqlSink(ck_host, db, username, password));
//        transformDS.print();

        streamEnv.execute("Flink Log-System Job: " + topics);
    }
}
