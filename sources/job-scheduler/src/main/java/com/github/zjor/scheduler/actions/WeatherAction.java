package com.github.zjor.scheduler.actions;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.Language;
import com.github.zjor.scheduler.SchedulerService;
import com.github.zjor.scheduler.outputs.Output;
import org.springframework.scheduling.support.CronExpression;

import java.util.List;
import java.util.Map;

public class WeatherAction extends Action {

    private final OpenWeatherMapClient client;

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

    @Override
    protected void execute(Map<String, Object> args) {
        var city = (String) args.getOrDefault("city", "Ubud");
        var countryCode = (String) args.getOrDefault("countryCode", "ID-BA");
        var weather = client.currentWeather().single()
                .byCityName(city, countryCode).language(Language.ENGLISH)
                .retrieve().asJava();
        getOutputs().forEach(output -> output.output(weather));
    }
}
