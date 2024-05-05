package me.krsmll.exchange.currency.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * This holds the currency rate against Euro. Every other currency rate is calculated based on this rate.
 * 1 EUR = X USD, 1 EUR = Y GBP, 1 EUR = Z JPY, etc.
 */
@Data
@Entity(name = "currency_rate_against_euro")
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateAgainstEuro {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", unique = true, nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "currency_name", unique = true, nullable = false)
    private String currencyName;

    @Column(name = "rate", nullable = false, precision = 14, scale = 6)
    private BigDecimal rate;

    @Column(name = "minor_units", nullable = false)
    private Integer minorUnits;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
