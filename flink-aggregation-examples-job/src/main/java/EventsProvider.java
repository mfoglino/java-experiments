

import com.google.gson.Gson;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.util.Properties;

public class EventsProvider {

    private static DataStream<PaymentContextInformation> readPaymentContextEventsStream(StreamExecutionEnvironment env, String streamName) {

        Properties consumerConfig = new Properties();
        consumerConfig.put(org.apache.flink.streaming.connectors.kinesis.config.AWSConfigConstants.AWS_REGION, "us-west-2");
        consumerConfig.put(ConsumerConfigConstants.STREAM_INITIAL_POSITION, "TRIM_HORIZON"); // TRIM_HORIZON, LATEST

        FlinkKinesisConsumer<String> consumer = new FlinkKinesisConsumer<>(streamName, new SimpleStringSchema(), consumerConfig);
        DataStream<String> kinesisStreamSourceString = env.addSource(consumer);

        SingleOutputStreamOperator<PaymentContextInformation> parsedEventsStream = kinesisStreamSourceString.map(e -> new Gson().fromJson(e, PaymentContextInformation.class));

        return parsedEventsStream;
    }

    public DataStream<PaymentContextInformation> readFspAccertifyEvents(StreamExecutionEnvironment env, String streamName) {
        return readPaymentContextEventsStream(env, streamName);
    }
}