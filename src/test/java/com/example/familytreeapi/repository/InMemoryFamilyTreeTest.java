package com.example.familytreeapi.repository;

import com.example.familytreeapi.model.FamilyMember;
import com.example.familytreeapi.model.Gender;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryFamilyTreeTest {

    @Test
    public void givenMaleAndFemaleParents_whenFamilyTreeConstructor_thenNewFamilyTreeIsCreatedWithGivenParents() {
        FamilyMember parentA = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember parentB = FamilyMember.builder().gender(Gender.FEMALE).build();

        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(parentA, parentB);

        assertThat(familyTree.getParentA(), is(parentA));
        assertThat(familyTree.getParentB(), is(parentB));
    }

    @Test
    public void givenTwoMaleParents_whenFamilyTreeConstructor_thenExceptionIsRaised() {
        FamilyMember parentA = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember parentB = FamilyMember.builder().gender(Gender.MALE).build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new InMemoryFamilyTree(parentA, parentB));

        String expectedMessage = "One parent must be female";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage, is(expectedMessage));
    }

    @Test
    public void givenTwoFemaleParents_whenFamilyTreeConstructor_thenExceptionIsRaised() {
        FamilyMember father = FamilyMember.builder().gender(Gender.FEMALE).build();
        FamilyMember mother = FamilyMember.builder().gender(Gender.FEMALE).build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new InMemoryFamilyTree(father, mother));

        String expectedMessage = "One parent must be male";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage, is(expectedMessage));
    }

    @Test
    public void givenFamilyTreeAndFamilyMemberNotInTree_whenIsFamilyMember_thenFalse() {
        FamilyMember parentA = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember parentB = FamilyMember.builder().gender(Gender.FEMALE).build();

        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(parentA, parentB);

        FamilyMember outsideMember = FamilyMember.builder().build();

        Boolean expected = false;
        Boolean actual = familyTree.isFamilyMember(outsideMember);
        assertThat(actual, is(expected));
    }

    @Test
    public void givenFamilyTreeAndFamilyMemberIsEitherFamilyParent_whenIsFamilyMember_thenTrue() {
        FamilyMember parentA = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember parentB = FamilyMember.builder().gender(Gender.FEMALE).build();

        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(parentA, parentB);

        FamilyMember outsideMember = FamilyMember.builder().build();

        Boolean expected = true;
        Boolean actual = familyTree.isFamilyMember(parentA) && familyTree.isFamilyMember(parentB);
        assertThat(actual, is(expected));
    }
}