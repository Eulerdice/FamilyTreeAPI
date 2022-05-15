package com.example.familytreeapi.repository;

import com.example.familytreeapi.model.FamilyMember;
import com.example.familytreeapi.model.Gender;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Component
public class InMemoryFamilyTree {
    private FamilyMember firstParent;
    private FamilyMember secondParent;
    private Map<Long, FamilyMember> familyMembers;

    public InMemoryFamilyTree() {
        this.firstParent = null;
        this.secondParent = null;
        this.familyMembers = new HashMap<>();
    }

    public void initialiseNewTree(FamilyMember firstParent, FamilyMember secondParent) {
        checkValidParentsGenderOrThrowException(firstParent, secondParent);

        this.familyMembers = new HashMap<>();
        this.familyMembers.put(firstParent.getId(), firstParent);
        this.familyMembers.put(secondParent.getId(), secondParent);

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
        familyMembers.put(child.getId(), child);

        return child;
    }

    public Boolean isFamilyMember(FamilyMember familyMember) {
        return familyMember != null && familyMembers.values().contains(familyMember);
    }

    public Optional<FamilyMember> findById(Long id) {
        return Optional.ofNullable(familyMembers.get(id));
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
