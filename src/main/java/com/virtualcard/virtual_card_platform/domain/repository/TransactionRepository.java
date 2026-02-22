package com.virtualcard.virtual_card_platform.domain.repository;

import com.virtualcard.virtual_card_platform.domain.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByCardId(UUID cardId);
    Page<Transaction> findByCardId(UUID cardId, Pageable pageable);
}
