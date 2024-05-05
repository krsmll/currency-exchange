package me.krsmll.exchange.currency.job;

import lombok.RequiredArgsConstructor;
import me.krsmll.exchange.currency.service.CurrencyService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyDataFetchingJob implements Job {
    private final CurrencyService currencyService;

    public void execute(JobExecutionContext jobExecutionContext) {
        currencyService.updateCurrencyRates();
    }
}
