import org.apache.flink.api.common.functions.AggregateFunction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

/**
 * Pagos aprobados, rechazados, cancelados con threshold de monto
 * Subtipo de rechazo
 * Unique: device_id, doc, pan_profile_id, payer_id, geo, ip
 * Nuevos device_id, payers, docs, pan_profile_id, geo
 * Antiguedad
 * Avg ticket
 * Max ticket
 * Proporcion de pagos CNP vs CP
 * Por tipo de BIN gold, platinium, etc
 * Payers Inhabilitados
 * Reputation groups flags: device_ids sospechosos, device_ids longevos -> Requiere de una tabla auxiliar o hardcoding
 * Pagos confiables: device_id, doc, pan_profile_id, payer_id, geo, ip -> Requiere de una tabla auxiliar
 * Generalizar - Key or Combined Key
 */
public class PaymentsAggregateFunction implements AggregateFunction<PaymentContextInformation, PaymentsAggregate, PaymentsAggregate> {
    @Override
    public PaymentsAggregate createAccumulator() {
        return new PaymentsAggregate();
    }

    @Override
    public PaymentsAggregate add(PaymentContextInformation payment, PaymentsAggregate accumulator) {
        accumulator.setMerchantId(payment.getMerchantId());
        incrementTransactions(accumulator, PaymentStatus.valueOf(payment.getPayment().getStatus()));
        incrementAmounts(payment, accumulator);
        setTimestamp(payment, accumulator);
        return accumulator;
    }

    private void setTimestamp(PaymentContextInformation payment, PaymentsAggregate accumulator) {
        accumulator.setWindowTimestamp(Instant.now());
    }

    private void incrementAmounts(PaymentContextInformation paymentInfo, PaymentsAggregate accumulator) {
        PaymentStatus.PaymentStatusEnum status = PaymentStatus.valueOf(paymentInfo.getPayment().getStatus());
        Double amount = paymentInfo.getPayment().getAmount();
        switch (status) {
            case APPROVED:
                accumulator.incrementApprovedAmount(amount);
                break;
            case DECLINED:
                accumulator.incrementDeclinedAmount(amount);
                break;
            case CANCELLED:
                accumulator.incrementCanceledAmount(amount);
                break;
            default:
                accumulator.incrementOthersAmount(amount);
        }

    }

    private void incrementTransactions(PaymentsAggregate accumulator, PaymentStatus.PaymentStatusEnum status) {

        switch (status) {
            case APPROVED:
                accumulator.incrementApproved();
                break;
            case DECLINED:
                accumulator.incrementDeclined();
                break;
            case CANCELLED:
                accumulator.incrementCanceled();
                break;
            default:
                accumulator.incrementOthers();
        }
    }


    @Override
    public PaymentsAggregate getResult(PaymentsAggregate accumulator) {
        return accumulator;
    }

    @Override
    public PaymentsAggregate merge(PaymentsAggregate a, PaymentsAggregate b) {

        PaymentsAggregate mergedResult = new PaymentsAggregate();
        mergedResult.setApproved(a.getApproved() + b.getApproved());
        mergedResult.setApprovedAmount(a.getApprovedAmount() + (b.getApprovedAmount()));
        mergedResult.setOthers(a.getOthers() + b.getOthers());
        mergedResult.setCanceled(a.getCanceled() + b.getCanceled());
        mergedResult.setDeclined(a.getDeclined() + b.getDeclined());
        mergedResult.setCanceledAmount(a.getCanceledAmount()+(b.getCanceledAmount()));
        mergedResult.setDeclinedAmount(a.getDeclinedAmount()+(b.getDeclinedAmount()));
        mergedResult.setOthersAmount(a.getOthersAmount()+(b.getOthersAmount()));

        mergedResult.setWindowTimestamp(Instant.now());

        return mergedResult;
    }
}