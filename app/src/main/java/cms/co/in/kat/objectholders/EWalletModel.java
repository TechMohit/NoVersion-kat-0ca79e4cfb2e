package cms.co.in.kat.objectholders;

/**
 * Created by subham_naik on 18-Jul-17.
 */

public class EWalletModel {
    private String transectionId;
    private String particulars;
    private String debit;
    private String credit;
    private String transactionDate;
    private String type;

    public void setTransectionId(String transectionId) {
        this.transectionId = transectionId;
    }

    public String getTransectionId() {
        return transectionId;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getDebit() {
        return debit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCredit() {
        return credit;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
