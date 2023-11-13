package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;

public class SenderIsAccountHolderVerification extends Verification {

    private final String accountHolderOfBankAccount;
    private final String verifiedUser;

    public SenderIsAccountHolderVerification(String accountHolderOfBankAccount, String verifiedUser) {
        this.accountHolderOfBankAccount = accountHolderOfBankAccount;
        this.verifiedUser = verifiedUser;
    }

    @Override
    VerificationStatus verify() {
        return accountHolderOfBankAccount.equals(verifiedUser) ? VerificationStatus.VERIFIED_SUCCESFULLY : VerificationStatus.SENDER_IS_NOT_ACCOUNTHOLDER;
    }

/*    @Override
    public VerificationStatus verify() {
        boolean senderIsBankAccountHolder = dto.verifiedUser().equals(bankAccount.getAccountHolder());
        if(senderIsBankAccountHolder) {
            return VerificationStatus.VERIFIED_SUCCESFULLY;
        }
        else {
            return VerificationStatus.SENDER_IS_NOT_ACCOUNTHOLDER;
        }
    }*/
}
