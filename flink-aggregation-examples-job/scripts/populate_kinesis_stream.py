import boto3
import json
import random
import time
from datetime import datetime, timedelta

merchants = ["10001", "10001", "10002", "10003"]
status_codes = [0, 1, 2, 3, -3]


def generate_payment_context_row(merchantId, transaction_date_str, amount, status_code):
    # transaction_date "2023-02-24 16:57:05"
    # amount 123.45
    # 0 / 1 / 2 / 3 /-3
    message = {"scoreDataV1": "insert another one", "payment": {"paymentId": "001",
                                                                "transactionId": "15d9fd31-2526-4ff9-b50c-a9d183c904ba",
                                                                "transactionDate": transaction_date_str,
                                                                "timeZone": "America/Mexico_City",
                                                                "paymentType": "CARD",
                                                                "paymentSubType": "EMV_SIGNATURE",
                                                                "transactionAmount": amount,
                                                                "tipAmount": 0.00, "statusCode": status_code,
                                                                "statusMessage": "Cancelled. Cardholder initiated.",
                                                                "reversalInitiator": "UI", "latitude": "20.3924815",
                                                                "longitude": "-101.1812874", "psp": "PROSA",
                                                                "orderId": "XXXXXXXXXXXXX",
                                                                "pspTransactionId": "598575",
                                                                "affiliation": "7927515", "subAffiliation": "1498091",
                                                                "userId": "2698cc1a-e932-4628-9df8-ec000a81fe9d",
                                                                "userEmail": "arsaarteaga@outlook.com",
                                                                "transactionLocalDate": "2023-03-15 10:19:28",
                                                                "SOPDays": 0,
                                                                "readerHasPinPad": 1,
                                                                "card": {"pan": "c0a76ad2-0e20-4a85-8781-12fcb888e995",
                                                                         "bin": "XXXXXX",
                                                                         "cardHolderName": "MAS DESPENSA",
                                                                         "expiration": "1023", "type": "Debit",
                                                                         "issuer": "SI VALE",
                                                                         "brand": "CR", "brandCode": "CR",
                                                                         "last4": "0206",
                                                                         "country": "MX"}},
               "merchant": {"merchantId": merchantId, "merchantName": "Super ARSA Arteaga",
                            "accountStatus": "ACTIVATED",
                            "urlWebPage": "", "registrationDate": "2021-06-15 21:20:57", "address1": "Arteaga 51",
                            "address2": "", "colony": "Valle de Santiago Centro", "municipality": "Valle de Santiago",
                            "state": "Guanajuato", "postalCode": "38400",
                            "industryId": "afa02c9a-58ae-11ea-82b4-0242ac130003",
                            "industryParentId": "afa02bd2-58ae-11ea-82b4-0242ac130003",
                            "industryName": "Minisúper y ultramarinos",
                            "adminUserId": "2698cc1a-e932-4628-9df8-ec000a81fe9d",
                            "adminFullName": "Juan de los Palotes", "adminEmail": "arsaarteaga@dummmy.com",
                            "adminPhone": "4566510298", "financeEmails": [], "bankAccountNumber": "XXXXXXXXXXXXX",
                            "bankName": "BBVA Bancomer, S.A.", "bankAccountOwnerName": "Rogelio Arredondo Sandoval",
                            "notificationEmail": "johndoe@outlook.com",
                            "geo": {"latitudeXPay": "20.3924815", "longitudeXPay": "-101.1812874"}, "isFast": False,
                            "rfc": "AESR830410J69"},
               "device": {"id": "2bb4ff883be41342", "name": "XGD", "vendorId": "69aaaa9d-e10a-4d0b-8f7d-b3891909f71e",
                          "os": "Android", "version": "7.1.2", "modelNumber": "N5_V7C.D1.01.405",
                          "kernel": "Linux - 3.10.49-g202dc76", "wifiName": "\"INFINITUM09C0\"",
                          "connectionType": "wifi", "signalStrength": "1048576", "modelName": "N5"},
               "application": {"bundleId": "com.payclip.clip", "version": "8.2.1", "invocation": "App",
                               "buildNumber": 780},
               "reader": {"deviceId": "N500W4N9810", "manufacturer": "NEXGO", "hardwareVersion": "N5",
                          "firmwareVersion": "v1.3.4", "sdkVersion": "3.01.001",
                          "bootLoaderVersion": "3.10.49-g202dc76", "isCharging": "false", "batteryPercentage": 0,
                          "usbConnected": "false", "typeCode": "F", "typeCodeName": "Fluorine"},
               "aggregated": {"localDatetimeofTransaction": "",
                              "domesticAcceptedTPVPercentageLastMonth": -7236.635211930727,
                              "domesticAcceptedTPVPercentageLifetime": -72.36635211930727,
                              "merchantAcceptedTPVLifetime": 10657722, "merchantAcceptedTPVLifetimeMean": 399.15067,
                              "merchantAcceptedTransactionCountLifetime": 26701.0,
                              "merchantDeclinedTPVLifetime": 1206968.2,
                              "merchantDeclinedTransactionCountLifetime": 2379.0}, "additionalInformation": {}}
    return message


def insert_message_in_stream(message):
    response = kinesis_client.put_record(
        StreamName=stream_name,
        Data=json.dumps(message),
        PartitionKey="test_partion_1")
    print("Record has been put.")
    print(response)


if _name_ == '_main_':
    session = boto3.Session(profile_name="sandbox-fraudbusters")
    s3 = session.client("s3")
    kinesis_client = session.client('kinesis')

    # print(s3.list_buckets())
    stream_name = "sandbox-fraud-payments-context-stream"

    # response = kinesis_client.describe_stream(StreamName="fraud-detection-payment-context-events-demo")
    # details = response['StreamDescription']
    # print(details)

    last_timestamp = datetime.now() - timedelta(days=20)

    for i in range(1, 1000):
        merchantId = random.choice(merchants)
        seconds = random.random() * 3
        last_timestamp = last_timestamp + timedelta(seconds=seconds)
        transaction_date = last_timestamp.strftime("%Y-%m-%d %H:%M:%S")
        amount = random.randint(100, 1000)
        status = random.choice(status_codes)

        message = generate_payment_context_row(merchantId, transaction_date, amount, status)
        time.sleep
        # print(merchantId, transaction_date, amount, status)

        print("Record: " + json.dumps(message))
        insert_message_in_stream(message)
        time.sleep(0.3)