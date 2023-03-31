import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FraudPreventionFeaturesJobTest {
    @Test
    public void testFeatureJobTransformations() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EventsProvider ep = new EventsProviderDummy();

        FeatureGeneratorConfig config = new FeatureGeneratorConfig();
        config.aggregation_window_size = Time.minutes(1);
        config.aggregation_window_slide = Time.seconds(30);

        // For version v1.15
        //Sink<PaymentsAggregate> dummyAggregationsSink = new PrintSink<PaymentsAggregate>();
        Sink<PaymentsAggregate> dummyAggregationsSink = null;

        SingleOutputStreamOperator<PaymentsAggregate> paymentsInfo = new PaymentEventTransformer(env, ep, config, dummyAggregationsSink).compute();

        CollectSink.values.clear();
        paymentsInfo.print();
        paymentsInfo.addSink(new CollectSink());

        env.execute("Feature job test");

        List<PaymentsAggregate> aMerchantFeatures = CollectSink.values.stream().filter(pa -> pa.getMerchantId().equals("merchant1")).collect(Collectors.toList());


//        assertThat(aMerchantFeatures, contains(
//                hasProperty("approvedAmount", is(BigDecimal.valueOf(10.0))),  // Window 0
//                hasProperty("approvedAmount", is(BigDecimal.valueOf(110.0))), // Window 1
//                hasProperty("approvedAmount", is(BigDecimal.valueOf(100.0))), // Window 2
//                hasProperty("othersAmount", is(BigDecimal.valueOf(300.0))), // Window 3
//                hasProperty("declinedAmount", is(BigDecimal.valueOf(400.0))) // Window 4
//        ));
    }

    private static class CollectSink implements SinkFunction<PaymentsAggregate> {
        public static final List<PaymentsAggregate> values = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void invoke(PaymentsAggregate value, SinkFunction.Context context) throws Exception {
            values.add(value);
        }
    }
}