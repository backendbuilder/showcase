package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;

public class SenderIsAccountHolderVerification implements Verification {

    private final String accountHolderOfBankAccount;
    private final String verifiedUser;

    public SenderIsAccountHolderVerification(String accountHolderOfBankAccount, String verifiedUser) {
        this.accountHolderOfBankAccount = accountHolderOfBankAccount;
        this.verifiedUser = verifiedUser;
    }

    @Override
    public VerificationStatus verify() {
        return accountHolderOfBankAccount.equals(verifiedUser) ? VerificationStatus.VERIFIED_SUCCESSFULLY : VerificationStatus.SENDER_IS_NOT_ACCOUNTHOLDER;
    }
}
