package dev.pdsf.eagleai.resource;

import dev.pdsf.eagleai.exception.ContactNotFoundException;
import dev.pdsf.eagleai.model.Contact;
import dev.pdsf.eagleai.repository.ContactRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class ContactResource {
    private final ContactRepository repository;

    ContactResource(ContactRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/contacts")
    List<Contact> listContacts() {
        return repository.findAll();
    }

    @GetMapping("/contacts/{id}")
    Contact findContact(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    @PostMapping("/contacts")
    Contact newContact(@RequestBody Contact newContact) {
        return repository.save(newContact);
    }

    @PutMapping("/contacts/{id}")
    Contact updateContact(@RequestBody Contact newContact, @PathVariable Long id) {
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

    @DeleteMapping("/contacts/{id}")
    void deleteContact(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
