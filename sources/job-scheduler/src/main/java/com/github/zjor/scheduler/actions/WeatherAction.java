package com.github.zjor.scheduler.actions;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.Language;
import com.github.prominence.openweathermap.api.model.weather.Weather;
import com.github.zjor.scheduler.SchedulerService;
import com.github.zjor.scheduler.outputs.Output;
import com.github.zjor.util.TimedCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.util.List;
import java.util.Map;

@Slf4j
public class WeatherAction extends Action {

    private static final String CACHE_KEY = "weather";
    private final OpenWeatherMapClient client;

    private final TimedCache<String, Weather> cache = new TimedCache(12_3600_000L);

    // TODO: inject environment & read the API key
    public WeatherAction(
            String jobId,
            SchedulerService schedulerService,
            CronExpression cron,
            Map<String, Object> args,
            List<Output> outputs) {
        super(jobId, schedulerService, cron, args, outputs);
        client = new OpenWeatherMapClient("cb072fa47b3b00a9bc42520e39b57904");
    }

    @Override
    public String getName() {
        return "WEATHER";
    }

    private Weather getWeather(Map<String, Object> args) {
        Weather weather = cache.get(CACHE_KEY);
        if (weather == null) {
            log.info("Cache miss. Loading weather.");
            var city = (String) args.getOrDefault("city", "Ubud");
            var countryCode = (String) args.getOrDefault("countryCode", "ID-BA");
            weather = client.currentWeather().single()
                    .byCityName(city, countryCode).language(Language.ENGLISH)
                    .retrieve().asJava();

            cache.put(CACHE_KEY, weather);
        }
        return weather;
    }

    @Override
    protected void execute(Map<String, Object> args) {
        var weather = getWeather(args);
        getOutputs().forEach(output -> output.output(weather));
    }
}
