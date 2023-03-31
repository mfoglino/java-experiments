import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class EventsProviderDummy extends EventsProvider {

    public DataStream<PaymentContextInformation> readFspAccertifyEvents(StreamExecutionEnvironment env, String streamName) {

        PaymentContextInformation pci = new PaymentContextInformation();
        pci.setMerchantId("merchant1");

        Payment p = new Payment();
        p.setAmount(11.1);
        p.setStatus(1);
        p.setCreatedAt("2023-02-24 16:57:05");
        p.setCreatedAtTimezone("America/Mexico_City");
        pci.setPayment(p);

        DataStreamSource<PaymentContextInformation> stream = env.fromElements(pci);
        return stream;
    }
}
