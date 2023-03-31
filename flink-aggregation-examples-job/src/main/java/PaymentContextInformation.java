// This class is meant for contain all the information needed in order to create metrics counters about
// the payment, user, merchant, device, etc.
// The origins of this data is produced by fsp-accertify in the payload class FspAccertifyPayload.

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentContextInformation {
    private Payment payment;
    private String merchantId;

    private static Instant parseDate(String strDate, String strTimeZone) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime ld = LocalDateTime.parse(strDate, dateTimeFormatter);
        ZoneId zoneId = ZoneId.of(strTimeZone);
        ZonedDateTime zonedDateTime = ld.atZone(zoneId);
        return zonedDateTime.toInstant();
    }

    public Instant getCreatedAtInstant() {
        return parseDate(this.getPayment().getCreatedAt(), this.getPayment().getCreatedAtTimezone());
    }

    @Override
    public String toString() {
        return "PaymentContextInformation{" +
                "payment=" + payment +
                ", merchantId='" + merchantId + '\'' +
                '}';
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}