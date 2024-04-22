package dev.pdsf.eaglai.resource;

import com.fasterxml.jackson.annotation.JsonView;
import dev.pdsf.eaglai.model.Contact;
import dev.pdsf.eaglai.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/contacts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Contact add(@Valid @ModelAttribute Contact newContact) {
        return contactService.newContact(newContact);
    }

    @JsonView(Views.General.class)
    @PutMapping(value = "/contacts/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Contact update(@Valid @ModelAttribute Contact newContact, @PathVariable Long id) {
        return contactService.updateContact(newContact, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/contacts/{id}")
    void delete(@PathVariable Long id) {
        contactService.deleteContact(id);
    }
}
