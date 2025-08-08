package dev.pdsf.eaglai.service;

import dev.pdsf.eaglai.exception.ContactNotFoundException;
import dev.pdsf.eaglai.model.Contact;
import dev.pdsf.eaglai.model.Description;
import dev.pdsf.eaglai.model.ContactListDTO;
import dev.pdsf.eaglai.repository.ContactRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Base64;

import net.coobird.thumbnailator.Thumbnails;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class ContactService {
    private final ContactRepository repository;
    private final DescriptionService descriptionService;
    private final OllamaService ollamaService;

    public ContactService(ContactRepository repository, DescriptionService descriptionService,
            OllamaService ollamaService) {
        this.repository = repository;
        this.descriptionService = descriptionService;
        this.ollamaService = ollamaService;
    }

    public Page<ContactListDTO> listContacts(Pageable pageable, Specification<Contact> spec) {
        return repository.findAll(spec, pageable)
                .map(contact -> {
                    String thumbnail = null;
                    if (contact.getThumbnailData() != null && contact.getThumbnailData().length > 0) {
                        thumbnail = Base64.getEncoder().encodeToString(contact.getThumbnailData());
                    }
                    return new ContactListDTO(
                            contact.getId(),
                            contact.getName(),
                            contact.getPhone(),
                            contact.getEmail(),
                            thumbnail,
                            contact.getNickname(),
                            contact.getBirthday());
                });
    }

    public Contact findContact(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public Contact newContact(Contact newContact) {
        byte[] imageData = newContact.getImageData();
        if (imageData != null) {
            generateThumbnail(newContact, imageData);
            Description description = parseImage(imageData);
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
                    c.setNickname(newContact.getNickname());
                    c.setBirthday(newContact.getBirthday());
                    c.setNotes(newContact.getNotes());
                    return c;
                })
                .orElseThrow(() -> new ContactNotFoundException(id));

        byte[] imageData = newContact.getImageData();
        if (imageData != null) {
            contact.setImageData(imageData);
            generateThumbnail(contact, imageData);
            Description description = parseImage(imageData);
            addContactDescription(contact, description);
        }

        return repository.save(contact);
    }

    public void deleteContact(Long id) {
        repository.deleteById(id);
    }

    private Description parseImage(byte[] imageData) {
        String response = ollamaService.getResponse(imageData);
        return ollamaService.parseResponse(response);
    }

    private void generateThumbnail(Contact contact, byte[] imageData) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(imageData);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(in)
                    .size(128, 128)
                    .keepAspectRatio(true)
                    .outputFormat("jpeg")
                    .outputQuality(0.8)
                    .toOutputStream(out);
            byte[] thumbnailBytes = out.toByteArray();

            // Log sizes for debugging
            System.out.println("Original image size: " + imageData.length + " bytes");
            System.out.println("Thumbnail size: " + thumbnailBytes.length + " bytes");

            contact.setThumbnailData(thumbnailBytes);
        } catch (Exception e) {
            System.out.println("Thumbnail generation failed: " + e.getMessage());
            // fallback to original image if thumbnail generation fails
            contact.setThumbnailData(imageData);
        }
    }

    private void addContactDescription(Contact contact, Description description) {
        descriptionService.newDescription(description);
        contact.setDescription(description);
    }
}
