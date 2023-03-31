import org.apache.flink.streaming.api.windowing.time.Time;

public class FeatureGeneratorConfig {

    Time aggregation_window_size = Time.seconds(30);
    Time aggregation_window_slide = Time.seconds(10);

    String payment_context_stream_name = "kinesis_stream";
}
