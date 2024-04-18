package dev.pdsf.eagleai.repository;

import dev.pdsf.eagleai.model.Description;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends CrudRepository<Description, Long> {
}
