package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;

import java.util.ArrayList;
import java.util.List;

public class VerificationProcessor {

    private final List<Verification> verifications = new ArrayList<>();

    public void addVerification(Verification verification){
        verifications.add(verification);
    }

    public VerificationStatus verify(){
        VerificationStatus status = VerificationStatus.VERIFICATION_INCOMPLETE;
        for(Verification verification: verifications){
            status = verification.verify();
            if(status != VerificationStatus.VERIFIED_SUCCESSFULLY){
                return status;
            }
        }
        return status;
    }
}
