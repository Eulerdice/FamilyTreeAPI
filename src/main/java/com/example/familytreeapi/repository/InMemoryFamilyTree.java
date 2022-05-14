package com.example.familytreeapi.repository;

import com.example.familytreeapi.model.FamilyMember;
import com.example.familytreeapi.model.Gender;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InMemoryFamilyTree {
    private final FamilyMember parentA;
    private final FamilyMember parentB;
    private final List<Long> familyMemberIds;

    public InMemoryFamilyTree(FamilyMember parentA, FamilyMember parentB) {
        if(parentA.getGender() == Gender.MALE && parentB.getGender() == Gender.MALE) {
            throw new IllegalArgumentException("One parent must be female");
        }
        if(parentA.getGender() == Gender.FEMALE && parentB.getGender() == Gender.FEMALE) {
            throw new IllegalArgumentException("One parent must be male");
        }

        this.familyMemberIds = new ArrayList<>();
        this.familyMemberIds.add(parentA.getId());
        this.familyMemberIds.add(parentB.getId());

        this.parentA = parentA;
        this.parentB = parentB;
    }

    public Boolean isFamilyMember(FamilyMember familyMember) {
        return familyMemberIds.contains(familyMember.getId());
    }
}
