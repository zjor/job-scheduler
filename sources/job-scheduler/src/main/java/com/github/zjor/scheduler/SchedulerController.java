package com.github.zjor.scheduler;

import com.github.zjor.scheduler.dto.JobAction;
import com.github.zjor.scheduler.dto.JobOutput;
import com.github.zjor.scheduler.dto.JobSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/scheduler")
public class SchedulerController {

    private final JobDefinitionRepository jobDefinitionRepository;

    public SchedulerController(JobDefinitionRepository jobDefinitionRepository) {
        this.jobDefinitionRepository = jobDefinitionRepository;
    }

    @PostMapping
    public JobDefinition create(@RequestBody CreateJobDefinitionRequest req) {
        return jobDefinitionRepository.save(JobDefinition.builder()
                        .action(req.action)
                        .arguments(req.arguments)
                        .schedule(req.schedule)
                        .output(req.output)
                .build());
    }

    @GetMapping
    public List<JobDefinition> findAll() {
        return spliteratorToList(jobDefinitionRepository.findAll().spliterator());
    }

    @GetMapping("{id}")
    public JobDefinition get(@PathVariable("id") String id) {
        return jobDefinitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, id));
    }

    @DeleteMapping("{id}")
    public JobDefinition delete(@PathVariable("id") String id) {
        var obj = jobDefinitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, id));
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
        private JobOutput output;
    }
}
