//import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
//import org.apache.flink.connector.aws.config.AWSConfigConstants;
//import org.apache.flink.connector.base.sink.writer.ElementConverter;
//import org.apache.flink.connector.dynamodb.sink.DynamoDbBeanElementConverter;
//import org.apache.flink.connector.dynamodb.sink.DynamoDbSink;
//import org.apache.flink.connector.dynamodb.sink.DynamoDbWriteRequest;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
//import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
//import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
//import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
//
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.util.List;
//import java.util.Properties;
//import java.util.stream.Collectors;
//
//import static java.util.Collections.singletonList;
//import static org.junit.Assert.assertEquals;
//
//public class FlinkToDynamoExampleTest {
//    public static DynamoDBProxyServer server;
//
//    @BeforeClass
//    public static void setupDynamo() throws Exception {
//        server = DynamoTestTools.createDynamoServer();
//        server.start();
//    }
//
//    @AfterClass
//    public static void teardownDynamo() throws Exception {
//        server.stop();
//    }
//
//    private static DynamoDbSink<EnrichedPaymentEvent> getEnrichedPaymentEventDynamoDbSink() {
//        Properties sinkProperties = new Properties();
//        String port = DynamoTestTools.port;
//        sinkProperties.put(AWSConfigConstants.AWS_REGION, "us-west-2");
//        sinkProperties.put(AWSConfigConstants.AWS_ACCESS_KEY_ID, "dummy");
//        sinkProperties.put(AWSConfigConstants.AWS_SECRET_ACCESS_KEY, "dummy");
//        sinkProperties.put(AWSConfigConstants.AWS_ENDPOINT, "http://localhost:" + port);
//        ElementConverter<EnrichedPaymentEvent, DynamoDbWriteRequest> elementConverter = new DynamoDbBeanElementConverter(EnrichedPaymentEvent.class);
//
//        DynamoDbSink<EnrichedPaymentEvent> dynamoDbSink =
//                DynamoDbSink.<EnrichedPaymentEvent>builder()
//                        .setDynamoDbProperties(sinkProperties)              // Required
//                        .setTableName("EnrichedPaymentEvent")                  // Required
//                        .setElementConverter(elementConverter)              // Required
//                        .setOverwriteByPartitionKeys(singletonList("merchantId"))  // Optional
//                        .setFailOnError(false)                              // Optional
//                        .setMaxBatchSize(25)                                // Optional
//                        .setMaxInFlightRequests(50)                         // Optional
//                        .setMaxBufferedRequests(10_000)                     // Optional
//                        .setMaxTimeInBufferMS(5000)                         // Optional
//                        .build();
//        return dynamoDbSink;
//    }
//
//    private static void insertEnrichedEvent(DynamoDbEnhancedClient enhancedClient, EnrichedPaymentEvent enrichedPaymentEvent) {
//        DynamoDbTable<EnrichedPaymentEvent> paymentTable = enhancedClient.table("EnrichedPaymentEvent", TableSchema.fromBean(EnrichedPaymentEvent.class));
//        PutItemEnhancedRequest enReq = PutItemEnhancedRequest.builder(EnrichedPaymentEvent.class)
//                .item(enrichedPaymentEvent)
//                .build();
//
//        paymentTable.putItem(enReq);
//    }
//
//    private static void enhancedCreateTable(DynamoDbEnhancedClient enhancedClient) {
//        String tableName = "EnrichedPaymentEvent";
//
//        DynamoDbTable<EnrichedPaymentEvent> payment = enhancedClient.table(tableName, TableSchema.fromBean(EnrichedPaymentEvent.class));
//        // Create the table
//        payment.createTable(builder -> builder
//                .provisionedThroughput(b -> b
//                        .readCapacityUnits(10L)
//                        .writeCapacityUnits(10L)
//                        .build())
//        );
//
////        System.out.println("Waiting for table creation...");
////
////        try (DynamoDbWaiter waiter = DynamoDbWaiter.create()) { // DynamoDbWaiter is Autocloseable
////            ResponseOrException<DescribeTableResponse> response = waiter
////                    .waitUntilTableExists(builder -> builder.tableName(tableName).build())
////                    .matched();
////            DescribeTableResponse tableDescription = response.response().get();
////
////                    //.orElseThrow(
////                    //() -> new RuntimeException(tableName + " table was not created."));
////            // The actual error can be inspected in response.exception()
////            System.out.println(tableDescription.table().tableName() + " was created.");
////        }
////        catch (Exception e){
////            System.out.println(e.getMessage());
////        }
//    }
//
//
//    private static EnrichedPaymentEvent getDummyEnrichedPayment() {
//        EnrichedPaymentEvent ep = new EnrichedPaymentEvent();
//        ep.setPaymentId("paymentId");
//        ep.setTransactionId("transactionId");
//        ep.setMerchantId("merchant.111");
//        ep.setMerchantToken("merchantToken");
//        ep.setUserId("userId");
//        ep.setUserEmail("userEmail");
//        ep.setApiUserId("apiUserId");
//        ep.setApiKey("apiKey");
//        ep.setPaymentType("paymentType");
//        ep.setPaymentSubType("paymentSubType");
//        ep.setReceiptNo("receiptNo");
//        ep.setMerchantInvoiceId("merchantInvoiceId");
//        ep.setAmount(111);
//        ep.setBaseAmount(112);
//        ep.setTip(0.11);
//        ep.setCurrency("currency");
//        ep.setTerm("term");
//        ep.setPsp("psp");
//        ep.setStatusCode(1);
//        ep.setStatusMsg("statusMsg");
//        ep.setLatitude(45.0);
//        ep.setLongitude(55.0);
//        ep.setDeviceId("deviceId");
//        ep.setHwManufacturer("hwManufacturer");
//        ep.setHwHasPinpad(true);
//        ep.setKsn("ksn");
//        ep.setLast4("last4");
//        ep.setCartId("cartId");
//        ep.setExpDate("expDate");
//        ep.setBrand("brand");
//        ep.setCardCountryCode("cardCountryCode");
//        ep.setCapability("capability");
//
//        LocalDate localDate = LocalDate.parse("2020-04-07");
//        LocalDateTime localDateTime = localDate.atStartOfDay();
//        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
//
//        ep.setCreatedAt(instant);
//        ep.setVersion("version");
//
//        return ep;
//    }
//
//    private static List<EnrichedPaymentEvent> listDummyPayments(DynamoDbEnhancedClient enhancedClient) {
//        DynamoDbTable<EnrichedPaymentEvent> paymentTable = enhancedClient.table("EnrichedPaymentEvent", TableSchema.fromBean(EnrichedPaymentEvent.class));
//        List<EnrichedPaymentEvent> results = paymentTable.scan().items().stream().collect(Collectors.toList());
//        return results;
//    }
//
//    @Test
//    public void testSinkToDynamo() throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        // crear tabla EventEnriched dummy
//        DynamoDbClient client = DynamoTestTools.getDynamoDbClient();
//        DynamoDbEnhancedClient enhancedClient = DynamoTestTools.getDynamoDbEnhancedClient(client);
//        enhancedCreateTable(enhancedClient);
//
//        // Probar de salvar el Evento en Dynamo
//        EnrichedPaymentEvent dummyEvent = getDummyEnrichedPayment();
//        //insertEnrichedEvent(enhancedClient, dummyEvent);
//
//        // hacer un stream de EventEnriched Dummy
//        DataStreamSource<EnrichedPaymentEvent> elements = env.fromElements(dummyEvent);
//
//        // Connectar Dynamo a Flink
//        DynamoDbSink<EnrichedPaymentEvent> dynamoDbSink = getEnrichedPaymentEventDynamoDbSink();
//        //System.out.println(client.listTables());
//
//        // Sink the results events to Dynamo
//        elements.sinkTo(dynamoDbSink);
//
//        env.execute();
//
//        List<EnrichedPaymentEvent> r = listDummyPayments(enhancedClient);
//        assertEquals("merchant.111", r.get(0).getMerchantId());
//    }