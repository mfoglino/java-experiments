//import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
//import org.junit.Test;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
//import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
//import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
//import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
//import software.amazon.awssdk.services.dynamodb.model.*;
//
//import java.util.Iterator;
//import java.util.List;
//
//import static java.lang.System.out;
//
//public class DynamoDummyTest {
//    @Test
//    public void createTableTest() throws Exception {
//        DynamoDBProxyServer server = DynamoTestTools.createDynamoServer();
//        server.start();
//        DynamoDbClient client = DynamoTestTools.getDynamoDbClient();
//        DynamoDbEnhancedClient enhancedClient = DynamoTestTools.getDynamoDbEnhancedClient(client);
//
//        try {
//            //CreateTableResponse table = createTable(client);
//
//            enhancedCreateTable(enhancedClient);
//
//            insertDummyItem(client, enhancedClient);
//
//            listDummyPayments(enhancedClient);
//
////            out.println(table.tableDescription());
//            List<String> tables = client.listTables().tableNames();
//            out.println(tables);
//
//            server.stop();
//        } catch (DynamoDbException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//
//    }
//
//    private static void listDummyPayments(DynamoDbEnhancedClient enhancedClient) {
//        DynamoDbTable<PaymentDummy> paymentTable = enhancedClient.table("PaymentDummy", TableSchema.fromBean(PaymentDummy.class));
//        Iterator<PaymentDummy> results = paymentTable.scan().items().iterator();
//        while (results.hasNext()) {
//            PaymentDummy rec = results.next();
//            System.out.println("The record id is "+rec.getId());
//            System.out.println("The amount is " +rec.getAmount());
//        }
//    }
//
//    private static void insertDummyItem(DynamoDbClient client, DynamoDbEnhancedClient enhancedClient) {
//
//        String tableName = "PaymentDummy";
//        DynamoDbTable<PaymentDummy> paymentTable = enhancedClient.table(tableName, TableSchema.fromBean(PaymentDummy.class));
//        PaymentDummy p = new PaymentDummy();
//        p.setAmount(28.0);
//        p.setId("1");
//        PutItemEnhancedRequest enReq = PutItemEnhancedRequest.builder(PaymentDummy.class)
//                .item(p)
//                .build();
//
//        paymentTable.putItem(enReq);
//    }
//
//    private static void enhancedCreateTable(DynamoDbEnhancedClient enhancedClient) {
//        String tableName = "PaymentDummy";
//        DynamoDbTable<PaymentDummy> payment = enhancedClient.table(tableName, TableSchema.fromBean(PaymentDummy.class));
//        // Create the table
//        payment.createTable(builder -> builder
//                .provisionedThroughput(b -> b
//                        .readCapacityUnits(10L)
//                        .writeCapacityUnits(10L)
//                        .build())
//        );
//
//
//
////        System.out.println("Waiting for table creation...");
////
////        //AWS_DEFAULT_REGION=us-west-2
////        try (DynamoDbWaiter waiter = DynamoDbWaiter.create()) { // DynamoDbWaiter is Autocloseable
////            ResponseOrException<DescribeTableResponse> response = waiter
////                    .waitUntilTableExists(builder -> builder.tableName(tableName).build())
////                    .matched();
////            DescribeTableResponse tableDescription = response.response().get();
////                    //.orElseThrow(() -> new RuntimeException(tableName + " table was not created."));
////            // The actual error can be inspected in response.exception()
////            System.out.println(tableDescription.table().tableName() + " was created.");
////        }
//    }
//
//    @Deprecated
//    private static CreateTableResponse createTable(DynamoDbClient client) {
//        String key = "id";
//        CreateTableRequest request = CreateTableRequest.builder()
//                .attributeDefinitions(AttributeDefinition.builder()
//                        .attributeName(key)
//                        .attributeType(ScalarAttributeType.S)
//                        .build())
//                .keySchema(KeySchemaElement.builder()
//                        .attributeName(key)
//                        .keyType(KeyType.HASH)
//                        .build())
//                .provisionedThroughput(ProvisionedThroughput.builder()
//                        .readCapacityUnits(Long.valueOf(10))
//                        .writeCapacityUnits(Long.valueOf(10))
//                        .build())
//                .tableName("PaymentDummy")
//                .build();
//
//
//        CreateTableResponse table = client.createTable(request);
//        return table;
//    }
//
//}