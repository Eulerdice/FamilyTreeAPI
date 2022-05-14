package com.example.familytreeapi.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Getter
@Setter
@Builder(builderClassName = "FamilyMemberBuilder")
@AllArgsConstructor
public class FamilyMember {
    private Long id;
    private String firstName;
    private Gender gender;

    private FamilyMember firstParent;
    private FamilyMember secondParent;
    private List<FamilyMember> children;

    public static class FamilyMemberBuilder {
        private Long id = new Random().nextLong();
        private String firstName = "firstName";
        private Gender gender = Gender.MALE;

        private FamilyMember firstParent = null;
        private FamilyMember secondParent = null;
        private List<FamilyMember> children = new ArrayList<>();
    }

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

    public List<FamilyMember> getDescendants() {
        List<FamilyMember> descendants = new ArrayList<>();
        children.forEach(child -> {
            descendants.add(child);
            descendants.addAll(child.getDescendants());
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
