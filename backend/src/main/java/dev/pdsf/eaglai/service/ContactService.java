package dev.pdsf.eaglai.service;

import dev.pdsf.eaglai.exception.ContactNotFoundException;
import dev.pdsf.eaglai.model.Contact;
import dev.pdsf.eaglai.model.Description;
import dev.pdsf.eaglai.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class ContactService {
    private final ContactRepository repository;
    private final DescriptionService descriptionService;
    private final OllamaService ollamaService;

    public ContactService(ContactRepository repository, DescriptionService descriptionService, OllamaService ollamaService) {
        this.repository = repository;
        this.descriptionService = descriptionService;
        this.ollamaService = ollamaService;
    }

    public List<Contact> listContacts() {
        return repository.findAll();
    }

    public Contact findContact(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public Contact newContact(Contact newContact) {
        byte[] imageData = newContact.getImageData();
        if (imageData != null) {
            Description description = parseImage(newContact.getImageData());
            addContactDescription(newContact, description);
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

        byte[] imageData = newContact.getImageData();
        if (imageData != null) {
            contact.setImageData(imageData);
            Description description = parseImage(imageData);
            addContactDescription(contact, description);
        }

        return repository.save(contact);
    }

    public void deleteContact(Long id) {
        repository.deleteById(id);
    }

    private Description parseImage(byte[] imageData) {
        String encodedImg = Base64.getEncoder().encodeToString(imageData);
        String response = ollamaService.getResponse(encodedImg);
        return ollamaService.parseResponse(response);
    }

    private void addContactDescription(Contact contact, Description description) {
        descriptionService.newDescription(description);
        contact.setDescription(description);
    }
}
