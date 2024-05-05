package me.krsmll.exchange.currency.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.krsmll.exchange.currency.entity.CurrencyRateAgainstEuro;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyRateAgainstEuro, Long> {
    Optional<CurrencyRateAgainstEuro> findByCurrencyCodeIgnoreCase(String currencyCode);
}
