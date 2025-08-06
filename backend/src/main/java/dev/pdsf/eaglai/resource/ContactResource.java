package dev.pdsf.eaglai.resource;

import com.fasterxml.jackson.annotation.JsonView;
import dev.pdsf.eaglai.model.Contact;
import dev.pdsf.eaglai.model.ContactListDTO;
import dev.pdsf.eaglai.model.types.Age;
import dev.pdsf.eaglai.model.types.EyeColor;
import dev.pdsf.eaglai.model.types.HairColor;
import dev.pdsf.eaglai.model.types.HairType;
import dev.pdsf.eaglai.model.types.Sex;
import dev.pdsf.eaglai.model.types.SkinTone;
import dev.pdsf.eaglai.repository.ContactSpecification;
import dev.pdsf.eaglai.service.ContactService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class ContactResource {
    private final ContactService contactService;

    public ContactResource(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    public Page<ContactListDTO> getContacts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Age age,
            @RequestParam(required = false) Sex sex,
            @RequestParam(required = false) SkinTone skinTone,
            @RequestParam(required = false) EyeColor eyeColor,
            @RequestParam(required = false) HairType hairType,
            @RequestParam(required = false) HairColor hairColor,
            @RequestParam(required = false) Boolean facialHair,
            Pageable pageable) {
        Specification<Contact> spec = ContactSpecification.withFilters(
                name, age, sex, skinTone, eyeColor, hairType, hairColor, facialHair);
        return contactService.listContacts(pageable, spec);
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
