package com.github.zjor.scheduler.controllers;

import com.github.zjor.scheduler.JobDefinitionRepository;
import com.github.zjor.scheduler.JobOutputRepository;
import com.github.zjor.scheduler.SchedulerService;
import com.github.zjor.scheduler.model.JobOutput;
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

import java.io.Serializable;
import java.util.List;

import static com.github.zjor.scheduler.controllers.ControllerUtils.notFound;

@RestController
@RequestMapping("api/jobs/{jobId}/outputs")
public class JobOutputController {

    private final SchedulerService schedulerService;
    private final JobDefinitionRepository jobDefinitionRepository;
    private final JobOutputRepository jobOutputRepository;

    public JobOutputController(
            SchedulerService schedulerService,
            JobDefinitionRepository jobDefinitionRepository,
            JobOutputRepository jobOutputRepository) {
        this.schedulerService = schedulerService;
        this.jobDefinitionRepository = jobDefinitionRepository;
        this.jobOutputRepository = jobOutputRepository;
    }

    @PostMapping
    public JobOutput create(@PathVariable("jobId") String jobId, @RequestBody CreateJobOutputRequest req) {
        var job = jobDefinitionRepository.findById(jobId).orElseThrow(notFound(jobId));
        var output = jobOutputRepository.save(JobOutput.builder()
                .job(job)
                .type(req.getType())
                .build());
        schedulerService.stop(jobId);
        schedulerService.schedule(job);
        return output;
    }

    @GetMapping
    public List<JobOutput> findAll(@PathVariable("jobId") String jobId) {
        var job = jobDefinitionRepository.findById(jobId).orElseThrow(notFound(jobId));
        return jobOutputRepository.findJobOutputsByJobOrderByCreatedAtDesc(job);
    }

    @DeleteMapping("{outputId}")
    public JobOutput delete(@PathVariable("jobId") String jobId, @PathVariable("outputId") String outputId) {
        var job = jobDefinitionRepository.findById(jobId).orElseThrow(notFound(jobId));
        var output = jobOutputRepository.findById(outputId).orElseThrow(notFound(outputId));
        jobOutputRepository.delete(output);
        schedulerService.stop(jobId);
        schedulerService.schedule(job);
        return output;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateJobOutputRequest {
        private JobOutput.OutputType type;
        private Serializable value;
    }
}
