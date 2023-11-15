package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;

public interface Verification {

    VerificationStatus verify();
}
