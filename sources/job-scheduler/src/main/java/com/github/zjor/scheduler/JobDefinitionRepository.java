package com.github.zjor.scheduler;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface JobDefinitionRepository extends CrudRepository<JobDefinition, String> {
}
