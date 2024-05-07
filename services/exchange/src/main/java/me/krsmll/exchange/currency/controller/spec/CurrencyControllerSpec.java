package me.krsmll.exchange.currency.controller.spec;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

import me.krsmll.exchange.currency.dto.CurrencyConversionResultResponse;
import me.krsmll.exchange.currency.dto.CurrencyListResponse;
import me.krsmll.exchange.currency.dto.CurrencyRateHistoryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface CurrencyControllerSpec {
    @Operation(summary = "Get full list of supported currencies")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "List of supported currencies",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CurrencyListResponse.class))
                        }),
                @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
            })
    ResponseEntity<CurrencyListResponse> getCurrencies();

    ResponseEntity<CurrencyRateHistoryResponse> getCurrencyRateHistory(String fromCurrencyCode, String toCurrencyCode);

    ResponseEntity<CurrencyConversionResultResponse> convert(
            String fromCurrencyCode, String toCurrencyCode, BigDecimal amount);
}
