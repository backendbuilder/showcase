package com.showcase.bankaccountservice.repositories;

import com.showcase.bankaccountservice.model.entities.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BankAccountRepositoryTest {

    @Container
    public static DockerComposeContainer dockerComposeContainer =
            new DockerComposeContainer(new File("src/test/resources/docker-compose_test.yml"))
                    .withExposedService("bank-account-db", 5433);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:tc:postgresql:///bank-account-db" ); //
        registry.add("spring.datasource.username", () -> "showcase-user");
        registry.add("spring.datasource.password", () -> "showcase-password");
    }

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    public void testFindByAccountHolder() {
        // Prepare
        String accountHolder1 = "MikeBanks";
        String accountHolder2 = "Bill";
        BigDecimal balance1 = BigDecimal.valueOf(100);
        BigDecimal balance2 = BigDecimal.valueOf(200);
        BigDecimal balance3 = BigDecimal.valueOf(300);
        BankAccount account1 = new BankAccount();
        account1.setAccountHolder(accountHolder1);
        account1.setBalance(balance1);
        BankAccount account2 = new BankAccount();
        account2.setAccountHolder(accountHolder1);
        account2.setBalance(balance2);
        BankAccount account3 = new BankAccount();
        account3.setAccountHolder(accountHolder2);
        account3.setBalance(balance3);


        bankAccountRepository.saveAll(List.of(account1, account2, account3));

        // Perform
        List<BankAccount> accountsForMikeBanks = bankAccountRepository.findByAccountHolder(accountHolder1);
        List<BankAccount> accountsForBill = bankAccountRepository.findByAccountHolder(accountHolder2);

        // Assert
        assertEquals(2, accountsForMikeBanks.size());
        assertEquals(accountHolder1, accountsForMikeBanks.get(0).getAccountHolder());
        assertEquals(accountHolder1, accountsForMikeBanks.get(1).getAccountHolder());
        assertEquals(1, accountsForBill.size());
        assertEquals(accountHolder2, accountsForBill.get(0).getAccountHolder());
    }
}