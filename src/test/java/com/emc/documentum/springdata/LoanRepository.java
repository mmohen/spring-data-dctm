package com.emc.documentum.springdata;

import com.emc.documentum.springdata.core.Loan;
import com.emc.documentum.springdata.repository.DctmRepositoryWithContent;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public interface LoanRepository extends DctmRepositoryWithContent<Loan, String> {
}
