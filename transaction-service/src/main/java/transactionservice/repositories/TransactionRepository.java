package transactionservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transactionservice.model.entities.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
