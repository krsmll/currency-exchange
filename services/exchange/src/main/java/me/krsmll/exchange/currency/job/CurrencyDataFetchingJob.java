package me.krsmll.exchange.currency.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import me.krsmll.exchange.currency.service.CurrencyService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrencyDataFetchingJob implements Job {
    private final CurrencyService currencyService;

    public void execute(JobExecutionContext jobExecutionContext) {
        currencyService.updateCurrencyRates();
    }
}
