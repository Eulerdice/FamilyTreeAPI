package com.example.familytreeapi.repository;

import com.example.familytreeapi.model.FamilyMember;
import com.example.familytreeapi.model.Gender;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InMemoryFamilyTree {
    private final FamilyMember firstParent;
    private final FamilyMember secondParent;
    private final List<FamilyMember> familyMembers;

    public InMemoryFamilyTree(FamilyMember firstParent, FamilyMember secondParent) {
        checkValidParentsGenderOrThrowException(firstParent, secondParent);

        this.familyMembers = new ArrayList<>();
        this.familyMembers.add(firstParent);
        this.familyMembers.add(secondParent);

        this.firstParent = firstParent;
        this.secondParent = secondParent;
    }

    public FamilyMember addChild(FamilyMember child) {
        checkValidParentsGenderOrThrowException(child.getFirstParent(), child.getSecondParent());

        if(!isFamilyMember(child.getFirstParent()) || !isFamilyMember(child.getSecondParent())) {
            throw new IllegalArgumentException("Both parents must be part of the family tree");
        }

        // Update parents' direct children lists and add child to familyMembers
        child.getFirstParent().getChildren().add(child);
        child.getSecondParent().getChildren().add(child);
        familyMembers.add(child);

        return child;
    }

    public Boolean isFamilyMember(FamilyMember familyMember) {
        return familyMember != null && familyMembers.contains(familyMember);
    }

    private void checkValidParentsGenderOrThrowException(FamilyMember firstParent, FamilyMember secondParent) {
        if(firstParent.getGender() == Gender.MALE && secondParent.getGender() == Gender.MALE) {
            throw new IllegalArgumentException("One parent must be female");
        }
        if(firstParent.getGender() == Gender.FEMALE && secondParent.getGender() == Gender.FEMALE) {
            throw new IllegalArgumentException("One parent must be male");
        }
    }
}
