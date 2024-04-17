package dev.pdsf.eagleai.service;

import dev.pdsf.eagleai.exception.ContactNotFoundException;
import dev.pdsf.eagleai.model.Contact;
import dev.pdsf.eagleai.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public List<Contact> listContacts() {
        return repository.findAll();
    }

    public Contact findContact(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public Contact newContact(Contact newContact) {
        return repository.save(newContact);
    }

    public Contact updateContact(Contact newContact, Long id) {
        return repository.findById(id)
                .map(contact -> {
                    contact.setName(newContact.getName());
                    contact.setEmail(newContact.getEmail());
                    contact.setPhone(newContact.getPhone());
                    contact.setAddress(newContact.getAddress());
                    return repository.save(contact);
                })
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public void deleteContact(Long id) {
        repository.deleteById(id);
    }
}
