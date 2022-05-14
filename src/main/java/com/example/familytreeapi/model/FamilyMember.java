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

    private FamilyMember father;
    private FamilyMember mother;
    private List<FamilyMember> children;

    public static class FamilyMemberBuilder {
        private Long id = new Random().nextLong();
        private String firstName = "firstName";
        private Gender gender = Gender.MALE;

        private FamilyMember father = null;
        private FamilyMember mother = null;
        private List<FamilyMember> children = new ArrayList<>();
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
