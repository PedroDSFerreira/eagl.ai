package dev.pdsf.eagleai.service;

import dev.pdsf.eagleai.exception.ContactNotFoundException;
import dev.pdsf.eagleai.model.Contact;
import dev.pdsf.eagleai.model.Description;
import dev.pdsf.eagleai.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository repository;
    private final DescriptionService descriptionService;

    public ContactService(ContactRepository repository, DescriptionService descriptionService) {
        this.repository = repository;
        this.descriptionService = descriptionService;
    }

    public List<Contact> listContacts() {
        return repository.findAll();
    }

    public Contact findContact(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public Contact newContact(Contact newContact) {
        if (newContact.getImageData() != null) {
            // Call ollama, get reply
            // Parse reply, get description params
            // Create new description w/params, and add to contact
            Description description = new Description(null, null,null, null, null, false);
            descriptionService.newDescription(description);
            newContact.setDescription(description);
        }
        return repository.save(newContact);
    }

    public Contact updateContact(Contact newContact, Long id) {
        Contact contact = repository.findById(id)
                .map(c -> {
                    c.setName(newContact.getName());
                    c.setEmail(newContact.getEmail());
                    c.setPhone(newContact.getPhone());
                    c.setAddress(newContact.getAddress());
                    return c;
                })
                .orElseThrow(() -> new ContactNotFoundException(id));
        return repository.save(contact);
    }

    public void deleteContact(Long id) {
        repository.deleteById(id);
    }
}
