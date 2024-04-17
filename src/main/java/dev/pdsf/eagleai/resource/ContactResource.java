package dev.pdsf.eagleai.resource;

import com.fasterxml.jackson.annotation.JsonView;
import dev.pdsf.eagleai.model.Contact;
import dev.pdsf.eagleai.model.Views;
import dev.pdsf.eagleai.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class ContactResource {
    private final ContactService contactService;

    public ContactResource(ContactService contactService) {
        this.contactService = contactService;
    }

    @JsonView(Views.Minimal.class)
    @GetMapping("/contacts")
    List<Contact> list() {
        return contactService.listContacts();
    }

    @JsonView(Views.General.class)
    @GetMapping("/contacts/{id}")
    Contact find(@PathVariable Long id) {
        return contactService.findContact(id);
    }

    @JsonView(Views.General.class)
    @PostMapping("/contacts")
    Contact add(@Valid @RequestBody Contact newContact) {
        return contactService.newContact(newContact);
    }

    @JsonView(Views.General.class)
    @PutMapping("/contacts/{id}")
    Contact update(@Valid @RequestBody Contact newContact, @PathVariable Long id) {
        return contactService.updateContact(newContact, id);
    }

    @DeleteMapping("/contacts/{id}")
    void delete(@PathVariable Long id) {
        contactService.deleteContact(id);
    }
}
