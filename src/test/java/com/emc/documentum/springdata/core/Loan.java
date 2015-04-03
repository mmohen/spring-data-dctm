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


    public Loan(int amount){
        this.amount = amount;
    }

    public Loan(){

    }

    @Id
    String loanId;
    int amount;


}
