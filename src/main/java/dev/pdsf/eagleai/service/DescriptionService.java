package dev.pdsf.eagleai.service;

import dev.pdsf.eagleai.exception.DescriptionNotFoundException;
import dev.pdsf.eagleai.model.Description;
import dev.pdsf.eagleai.repository.DescriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class DescriptionService {
    private final DescriptionRepository repository;

    public DescriptionService(DescriptionRepository repository) {
        this.repository = repository;
    }
    
    public Description getDescription(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DescriptionNotFoundException(id));
    }
    
    public Description newDescription(Description newDescription) {
        return repository.save(newDescription);
    }

    public Description updateDescription(Description newDescription, Long id) {
        Description description = repository.findById(id)
                .map(d -> {
                    d.setSex(newDescription.getSex());
                    d.setSkinTone(newDescription.getSkinTone());
                    d.setEyeColor(newDescription.getEyeColor());
                    d.setHairType(newDescription.getHairType());
                    d.setHairColor(newDescription.getHairColor());
                    d.setFacialHair(newDescription.isFacialHair());
                    return d;
                })
                .orElseThrow(() -> new DescriptionNotFoundException(id));
        return repository.save(description);
    }

    public void deleteDescription(Long id) {
        repository.deleteById(id);
    }
    
    
}
