package com.emc.documentum.springdata;

import java.util.List;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.emc.documentum.springdata.core.Loan;
import com.emc.documentum.springdata.repository.DctmRepositoryWithContent;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public interface LoanQueryDslDctmRepository extends DctmRepositoryWithContent<Loan, String>, QueryDslPredicateExecutor<Loan> {
  List<Loan> findByAmountGreaterThan(int amount);
}
