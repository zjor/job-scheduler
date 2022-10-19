package com.github.zjor.scheduler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    @GetMapping
    public List<JobDefinition> findAll() {
        return spliteratorToList(jobDefinitionRepository.findAll().spliterator());
    }

    @GetMapping("{id}")
    public JobDefinition get(@PathVariable("id") String id) {
        return jobDefinitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, id));
    }

    public static <T> List<T> spliteratorToList(Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false).collect(Collectors.toList());
    }
}
