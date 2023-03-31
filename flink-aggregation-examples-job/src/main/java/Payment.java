public class Payment {

    Double amount;

    Integer status;
    String createdAt;
    String createdAtTimezone;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "amount=" + amount +
                ", status=" + status +
                ", createdAt='" + createdAt + '\'' +
                ", createdAtTimezone='" + createdAtTimezone + '\'' +
                '}';
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreatedAtTimezone() {
        return createdAtTimezone;
    }

    public void setCreatedAtTimezone(String createdAtTimezone) {
        this.createdAtTimezone = createdAtTimezone;
    }
}
