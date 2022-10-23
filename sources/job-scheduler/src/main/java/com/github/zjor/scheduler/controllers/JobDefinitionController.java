package com.github.zjor.scheduler.controllers;

import com.github.zjor.scheduler.JobDefinitionRepository;
import com.github.zjor.scheduler.SchedulerService;
import com.github.zjor.scheduler.dto.JobAction;
import com.github.zjor.scheduler.dto.JobSchedule;
import com.github.zjor.scheduler.model.JobDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.github.zjor.scheduler.controllers.ControllerUtils.notFound;

@RestController
@RequestMapping("api/jobs")
public class JobDefinitionController {

    private final SchedulerService schedulerService;
    private final JobDefinitionRepository jobDefinitionRepository;

    public JobDefinitionController(SchedulerService schedulerService,
                                   JobDefinitionRepository jobDefinitionRepository) {
        this.schedulerService = schedulerService;
        this.jobDefinitionRepository = jobDefinitionRepository;
    }

    @PostMapping
    public JobDefinition create(@RequestBody CreateJobDefinitionRequest req) {
        var job = jobDefinitionRepository.save(JobDefinition.builder()
                .action(req.action)
                .arguments(req.arguments)
                .schedule(req.schedule)
                .build());
        schedulerService.schedule(job);
        return job;
    }

    @GetMapping
    public List<JobDefinition> findAll() {
        return spliteratorToList(jobDefinitionRepository.findAll().spliterator());
    }

    @GetMapping("{id}")
    public JobDefinition get(@PathVariable("id") String id) {
        return jobDefinitionRepository.findById(id).orElseThrow(notFound(id));
    }

    @DeleteMapping("{id}")
    public JobDefinition delete(@PathVariable("id") String id) {
        var obj = jobDefinitionRepository.findById(id).orElseThrow(notFound(id));
        schedulerService.stop(id);
        jobDefinitionRepository.delete(obj);
        return obj;
    }

    public static <T> List<T> spliteratorToList(Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false).collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateJobDefinitionRequest {
        private JobAction action;
        private Map<String, Object> arguments;
        private JobSchedule schedule;
    }
}
