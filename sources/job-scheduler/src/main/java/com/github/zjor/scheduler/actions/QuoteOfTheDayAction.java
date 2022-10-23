package com.github.zjor.scheduler.actions;

import com.github.zjor.scheduler.SchedulerService;
import com.github.zjor.scheduler.outputs.Output;
import kong.unirest.Unirest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.util.List;
import java.util.Map;

@Slf4j
public class QuoteOfTheDayAction extends Action {

    private static final String URL = "https://quotes.rest/qod.json";
    private static final String DEFAULT_CATEGORY = "inspire";

    private JQuote quote = null;

    public QuoteOfTheDayAction(String jobId,
                               SchedulerService schedulerService,
                               CronExpression cron,
                               Map<String, Object> args,
                               List<Output> outputs
    ) {
        super(jobId, schedulerService, cron, args, outputs);
    }

    @Override
    public String getName() {
        return "QOTD";
    }

    @Override
    protected void execute(Map<String, Object> args) {
        if (quote == null) {
            var category = (String) args.getOrDefault("category", DEFAULT_CATEGORY);
            quote = fetchQuoteOfTheDay(category);
        }
        log.info("QOTD: {}", quote);
    }

    private JQuote fetchQuoteOfTheDay(String category) {
        var res = Unirest
                .get(URL)
                .connectTimeout(15_000)
                .socketTimeout(45_000)
                .queryString("category", category)
                .asObject(JQuoteResponse.class).getBody();
        return res.getContents().quotes.get(0);
    }

    @Data
    public static class JQuoteResponse {
        private JContents contents;
    }

    @Data
    public static class JContents {
        private List<JQuote> quotes;
    }

    @Data
    public static class JQuote {
        private String quote;
        private String author;
    }
}
