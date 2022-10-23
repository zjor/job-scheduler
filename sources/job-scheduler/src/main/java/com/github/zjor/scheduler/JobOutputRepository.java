package com.github.zjor.scheduler;

import com.github.zjor.scheduler.model.JobDefinition;
import com.github.zjor.scheduler.model.JobOutput;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface JobOutputRepository extends CrudRepository<JobOutput, String> {

    List<JobOutput> findJobOutputsByJobOrderByCreatedAtDesc(JobDefinition job);

}
