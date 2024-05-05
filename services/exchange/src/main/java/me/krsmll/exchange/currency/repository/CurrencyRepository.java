package me.krsmll.exchange.currency.repository;

import me.krsmll.exchange.currency.entity.CurrencyRateAgainstEuro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyRateAgainstEuro, Long> {
    Optional<CurrencyRateAgainstEuro> findByCurrencyCodeIgnoreCase(String currencyCode);
}
