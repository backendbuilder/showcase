package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;

public abstract class Verification {

    abstract VerificationStatus verify();
}
