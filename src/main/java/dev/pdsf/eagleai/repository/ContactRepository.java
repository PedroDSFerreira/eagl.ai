package dev.pdsf.eagleai.repository;

import dev.pdsf.eagleai.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
