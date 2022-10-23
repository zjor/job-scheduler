package com.github.zjor.scheduler;

import com.github.zjor.scheduler.dto.JobOutput;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface JobOutputRepository extends CrudRepository<JobOutput, String> {
}
