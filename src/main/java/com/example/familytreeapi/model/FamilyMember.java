package com.example.familytreeapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Getter
@Setter
@Builder(builderClassName = "FamilyMemberBuilder")
@AllArgsConstructor
@ToString
public class FamilyMember {
    @JsonIgnore
    private Long id;

    private String firstName;
    private Gender gender;
    @JsonIgnore
    private FamilyMember firstParent;

    @JsonIgnore
    private FamilyMember secondParent;

    @JsonIgnore
    @ToString.Exclude
    @JsonBackReference
    private List<FamilyMember> children;

    public static class FamilyMemberBuilder {
        private Long id = new Random().nextLong();
        private String firstName = "firstName";
        private Gender gender = Gender.MALE;

        private FamilyMember firstParent = null;
        private FamilyMember secondParent = null;
        private List<FamilyMember> children = new ArrayList<>();
    }

    @JsonBackReference
    public List<FamilyMember> getAncestors() {
        List<FamilyMember> ancestors = new ArrayList<>();
        if(firstParent != null) {
            ancestors.add(firstParent);
            firstParent.getAncestors().forEach(parentAncestor -> {
                if(!ancestors.contains(parentAncestor)) {
                    ancestors.add(parentAncestor);
                }
            });
        }
        if(secondParent != null) {
            ancestors.add(secondParent);
            secondParent.getAncestors().forEach(parentAncestor -> {
                if(!ancestors.contains(parentAncestor)) {
                    ancestors.add(parentAncestor);
                }
            });
        }
        return ancestors;
    }

    @JsonBackReference
    public List<FamilyMember> getDescendants() {
        List<FamilyMember> descendants = new ArrayList<>();
        children.forEach(child -> {
            descendants.add(child);
            child.getDescendants().forEach(descendant -> {
                if(!descendants.contains(descendant)) {
                    descendants.add(descendant);
                }
            });
        });
        return descendants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMember that = (FamilyMember) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
