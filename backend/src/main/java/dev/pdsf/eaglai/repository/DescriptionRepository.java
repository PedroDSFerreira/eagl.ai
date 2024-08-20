package dev.pdsf.eaglai.repository;

import dev.pdsf.eaglai.model.Description;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends CrudRepository<Description, Long> {
}
