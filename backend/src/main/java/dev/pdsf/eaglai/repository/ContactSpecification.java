package dev.pdsf.eaglai.repository;

import org.springframework.data.jpa.domain.Specification;
import dev.pdsf.eaglai.model.Contact;
import dev.pdsf.eaglai.model.Description;
import dev.pdsf.eaglai.model.types.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ContactSpecification {
    public static Specification<Contact> withFilters(
            String name,
            Age age, Sex sex, SkinTone skinTone, EyeColor eyeColor,
            HairType hairType, HairColor hairColor, Boolean facialHair) {
        return (root, query, cb) -> {
            Join<Contact, Description> desc = root.join("description", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                String pattern = "%" + name.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("nickname")), pattern)));
            }
            if (age != null)
                predicates.add(cb.equal(desc.get("age"), age));
            if (sex != null)
                predicates.add(cb.equal(desc.get("sex"), sex));
            if (skinTone != null)
                predicates.add(cb.equal(desc.get("skinTone"), skinTone));
            if (eyeColor != null)
                predicates.add(cb.equal(desc.get("eyeColor"), eyeColor));
            if (hairType != null)
                predicates.add(cb.equal(desc.get("hairType"), hairType));
            if (hairColor != null)
                predicates.add(cb.equal(desc.get("hairColor"), hairColor));
            if (facialHair != null)
                predicates.add(cb.equal(desc.get("facialHair"), facialHair));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}