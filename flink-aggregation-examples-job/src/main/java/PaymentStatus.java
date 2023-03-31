public class PaymentStatus {

    public enum PaymentStatusEnum {
        APPROVED,
        DECLINED,
        CANCELLED,
        OTHER
    }
    public static PaymentStatusEnum valueOf(Integer status){

        switch(status) {
            case 1:
                return PaymentStatusEnum.APPROVED;
            case 2:
                return PaymentStatusEnum.DECLINED;
            case 3:
                return PaymentStatusEnum.CANCELLED;
        }
        return PaymentStatusEnum.OTHER;
    }

}
