package dev.pdsf.eagleai.resource;

import dev.pdsf.eagleai.model.Contact;
import dev.pdsf.eagleai.service.ContactService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class ContactResource {
    private final ContactService contactService;

    public ContactResource(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    List<Contact> list() {
        return contactService.listContacts();
    }

    @GetMapping("/contacts/{id}")
    Contact find(@PathVariable Long id) {
        return contactService.findContact(id);
    }

    @PostMapping("/contacts")
    Contact add(@RequestBody Contact newContact) {
        return contactService.newContact(newContact);
    }

    @PutMapping("/contacts/{id}")
    Contact update(@RequestBody Contact newContact, @PathVariable Long id) {
        return contactService.updateContact(newContact, id);
    }

    @DeleteMapping("/contacts/{id}")
    void delete(@PathVariable Long id) {
        contactService.deleteContact(id);
    }
}
