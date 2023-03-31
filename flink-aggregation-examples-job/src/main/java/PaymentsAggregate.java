import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.Objects;

/**
 * * Pagos aprobados, rechazados, cancelados con threshold de monto
 * * Subtipo de rechazo
 * * Unique: device_id, doc, pan_profile_id, payer_id, geo, ip
 * * Reputation groups flags: device_ids sospechosos, device_ids longevos,
 * * Nuevos device_id, payers, docs, pan_profile_id, geo
 * * Pagos confiables: device_id, doc, pan_profile_id, payer_id, geo, ip
 * * Antiguedad
 * * Avg ticket
 * * Max ticket
 * * Proporcion de pagos CNP vs CP
 * * Por tipo de BIN gold, platinium, etc
 * * Payers Inhabilitados
 */
@DynamoDbBean
public class PaymentsAggregate {
    String merchantId;
    int approved;
    int declined;
    int canceled;
    int others;
    Double approvedAmount = 0.0;
    Double declinedAmount = 0.0;
    Double canceledAmount = 0.0;
    Double othersAmount = 0.0;
    Instant windowTimestamp;


    public Double getOthersAmount() {
        return othersAmount;
    }

    public void setOthersAmount(Double othersAmount) {
        this.othersAmount = othersAmount;
    }


    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getDeclined() {
        return declined;
    }

    public void setDeclined(int declined) {
        this.declined = declined;
    }

    public int getCanceled() {
        return canceled;
    }

    public void setCanceled(int canceled) {
        this.canceled = canceled;
    }

    public Double getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(Double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public Double getDeclinedAmount() {
        return declinedAmount;
    }

    public void setDeclinedAmount(Double declinedAmount) {
        this.declinedAmount = declinedAmount;
    }

    public Double getCanceledAmount() {
        return canceledAmount;
    }

    public void setCanceledAmount(Double canceledAmount) {
        this.canceledAmount = canceledAmount;
    }

    public void incrementApproved() {
        incrementApproved(1);
    }

    public void incrementApproved(int quantity) {
        this.setApproved(this.getApproved() + quantity);
    }

    public void incrementDeclined() {
        incrementDeclined(1);
    }

    public void incrementDeclined(int quantity) {
        this.setDeclined(this.getDeclined() + quantity);
    }


    public void incrementCanceled() {
        incrementCanceled(1);
    }

    public void incrementCanceled(int quantity) {
        this.setCanceled(this.getCanceled() + quantity);
    }


    public void incrementOthers() {
        incrementOthers(1);
    }

    public void incrementOthers(int quantity) {
        this.setOthers(this.getOthers() + quantity);
    }

    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }

    @DynamoDbPartitionKey
    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void incrementApprovedAmount(Double amount) {
        this.setApprovedAmount(this.getApprovedAmount() + (amount));
    }

    public void incrementDeclinedAmount(Double amount) {
        this.setDeclinedAmount(this.getDeclinedAmount() + (amount));
    }

    public void incrementCanceledAmount(Double amount) {
        this.setCanceledAmount(this.getCanceledAmount() + (amount));
    }

    public void incrementOthersAmount(Double amount) {
        this.setOthersAmount(this.getOthersAmount() + (amount));
    }

    @DynamoDbSortKey
    public Instant getWindowTimestamp() {
        return this.windowTimestamp;
    }

    public void setWindowTimestamp(final Instant windowTimestamp) {
        this.windowTimestamp = windowTimestamp;
    }

    @Override
    public String toString() {
        return "PaymentsAggregate{" +
                "timestamp=" + windowTimestamp +
                " merchantId=" + merchantId +
                ", approved=" + approved +
                ", declined=" + declined +
                ", canceled=" + canceled +
                ", others=" + others +
                ", approvedAmount=" + approvedAmount +
                ", declinedAmount=" + declinedAmount +
                ", canceledAmount=" + canceledAmount +
                ", othersAmount=" + othersAmount +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        final PaymentsAggregate that = (PaymentsAggregate) o;
        return this.approved == that.approved && this.declined == that.declined && this.canceled == that.canceled && this.others == that.others && this.merchantId.equals(that.merchantId) && (this.approvedAmount == null ? that.approvedAmount == null : this.approvedAmount.compareTo(that.approvedAmount) == 0) && (this.declinedAmount == null ? that.declinedAmount == null : this.declinedAmount.compareTo(that.declinedAmount) == 0) && (this.canceledAmount == null ? that.canceledAmount == null : this.canceledAmount.compareTo(that.canceledAmount) == 0) && (this.othersAmount == null ? that.othersAmount == null : this.othersAmount.compareTo(that.othersAmount) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.merchantId, this.approved, this.declined, this.canceled, this.others, this.approvedAmount, this.declinedAmount, this.canceledAmount, this.othersAmount);
    }
}