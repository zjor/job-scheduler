package com.github.zjor.scheduler.actions;

import com.github.zjor.scheduler.outputs.Output;
import com.github.zjor.util.TimedCache;
import kong.unirest.Unirest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.support.CronExpression;

import java.util.List;
import java.util.Map;

@Slf4j
public class QuoteOfTheDayAction extends Action {

    private static final String CACHE_KEY = "quote";

    private static final String URL = "https://quotes.rest/qod.json";
    private static final String DEFAULT_CATEGORY = "inspire";

    private final TimedCache<String, JQuote> cache = new TimedCache(12_3600_000L);

    public QuoteOfTheDayAction(ApplicationContext context,
                               String jobId,
                               CronExpression cron,
                               Map<String, Object> args,
                               List<Output> outputs
    ) {
        super(context, jobId, cron, args, outputs);
    }

    @Override
    public String getName() {
        return "QOTD";
    }

    @Override
    protected void execute(Map<String, Object> args) {
        var quote = getQuote(args);
        outputs.forEach(output -> output.output(quote));
    }

    // TODO: code smell: caching logic duplication, cache could be centralized
    // timeout may be key-specific
    private JQuote getQuote(Map<String, Object> args) {
        JQuote quote = cache.get(CACHE_KEY);
        if (quote == null) {
            log.info("Cache miss. Loading quote.");
            var category = (String) args.getOrDefault("category", DEFAULT_CATEGORY);
            quote = fetchQuoteOfTheDay(category);
            cache.put(CACHE_KEY, quote);
        }
        return quote;
    }

    private JQuote fetchQuoteOfTheDay(String category) {
        var res = Unirest
                .get(URL)
                .connectTimeout(30_000)
                .socketTimeout(90_000)
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
