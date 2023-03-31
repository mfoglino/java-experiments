import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


public class PaymentEventTransformer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventTransformer.class);
    private StreamExecutionEnvironment flinkEnv;
    private EventsProvider eventsProvider;
    private FeatureGeneratorConfig config;
    private Sink<PaymentsAggregate> enrichedEventsSink;

    public PaymentEventTransformer(StreamExecutionEnvironment env, EventsProvider eventsProvider, FeatureGeneratorConfig featureGeneratorConfig, Sink<PaymentsAggregate> aggregationsSink) throws Exception {

        this.flinkEnv = env;
        this.eventsProvider = eventsProvider;
        this.config = featureGeneratorConfig;
        this.enrichedEventsSink = aggregationsSink;
    }

    private static WatermarkStrategy<PaymentContextInformation> getWatermarkStrategy() {

        return WatermarkStrategy.<PaymentContextInformation>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                .withTimestampAssigner((TimestampAssignerSupplier<PaymentContextInformation>) context -> (TimestampAssigner<PaymentContextInformation>) (element, recordTimestamp) -> {
                    return element.getCreatedAtInstant().toEpochMilli();
                });
    }

    public SingleOutputStreamOperator<PaymentsAggregate> compute() throws Exception {
        logger.info("Starting aggregations compute...");


        //Read the input stream, assigning timestamps and creating watermarks.
        DataStream<PaymentContextInformation> paymentsStream = eventsProvider.readFspAccertifyEvents(flinkEnv, this.config.payment_context_stream_name);

        paymentsStream.print();
        logger.info("About to do aggregations...");
        SingleOutputStreamOperator<PaymentsAggregate> merchantAggregatesStream =
                paymentsStream.assignTimestampsAndWatermarks(getWatermarkStrategy()).keyBy((PaymentContextInformation p) -> p.getMerchantId())
                        .window(SlidingEventTimeWindows.of(config.aggregation_window_size, config.aggregation_window_slide))
                        .aggregate(new PaymentsAggregateFunction());

        //merchantAggregatesStream.sinkTo(this.enrichedEventsSink);
        return merchantAggregatesStream;
    }
}