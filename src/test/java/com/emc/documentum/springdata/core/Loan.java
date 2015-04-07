package com.emc.documentum.springdata.core;

import com.emc.documentum.springdata.entitymanager.annotations.Content;
import com.emc.documentum.springdata.entitymanager.mapping.DctmEntity;
import org.springframework.data.annotation.Id;

/**
 * Created with IntelliJ IDEA.
 * User: ramanwalia
 * Date: 03/04/15
 * Time: 9:58 PM
 * To change this template use File | Settings | File Templates.
 */
@DctmEntity(repository = "loan")
public class Loan {
    public Loan() {}

    public Loan(int amount){
        this.amount = amount;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Loan loan = (Loan)o;

        if (amount != loan.amount) { return false; }
        return !(loanId != null ? !loanId.equals(loan.loanId) : loan.loanId != null);

    }

    @Override
    public int hashCode() {
        int result = loanId != null ? loanId.hashCode() : 0;
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        return "Loan{" +
            "loanId='" + loanId + '\'' +
            ", amount=" + amount +
            '}';
    }

    @Id
    String loanId;
    int amount;
}
