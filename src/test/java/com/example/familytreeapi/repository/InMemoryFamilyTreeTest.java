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
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        assertThat(familyTree.getFirstParent(), is(firstParent));
        assertThat(familyTree.getSecondParent(), is(secondParent));
    }

    @Test
    public void givenTwoMaleParents_whenFamilyTreeConstructor_thenExceptionIsRaised() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.MALE).build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new InMemoryFamilyTree(firstParent, secondParent));

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
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        FamilyMember outsideMember = FamilyMember.builder().build();

        Boolean expected = false;
        Boolean actual = familyTree.isFamilyMember(outsideMember);
        assertThat(actual, is(expected));
    }

    @Test
    public void givenFamilyTreeAndFamilyMemberIsEitherFamilyParent_whenIsFamilyMember_thenTrue() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        Boolean expected = true;
        Boolean actual = familyTree.isFamilyMember(firstParent) && familyTree.isFamilyMember(secondParent);
        assertThat(actual, is(expected));
    }

    @Test
    public void givenChildFromIntialFamilyTreeParents_whenAddChild_thenChildIsAddedToFamilyTreeAndReturned() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        FamilyMember newChild = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();

        FamilyMember expected = newChild;
        FamilyMember actual = familyTree.addChild(newChild);
        assertThat(actual, is(expected));
    }

    @Test
    public void givenChildFromFamilyTreeParents_whenAddChildSuccessful_thenParentsChildrenListsAreUpdated() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        FamilyMember newChild = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();
        familyTree.addChild(newChild);

        Boolean expected = true;
        Boolean actual = firstParent.getChildren().contains(newChild) && secondParent.getChildren().contains(newChild);
        assertThat(actual, is(expected));
    }

    @Test
    public void givenChildFromFamilyTreeParentsChildren_whenAddChild_thenChildIsAddedToFamilyTreeAndReturned() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        FamilyMember child1 = FamilyMember.builder().gender(Gender.MALE).firstParent(firstParent).secondParent(secondParent).build();
        FamilyMember child2 = FamilyMember.builder().gender(Gender.FEMALE).firstParent(firstParent).secondParent(secondParent).build();
        familyTree.addChild(child1);
        familyTree.addChild(child2);

        FamilyMember newChild = FamilyMember.builder().firstParent(child1).secondParent(child2).build();

        FamilyMember expected = newChild;
        FamilyMember actual = familyTree.addChild(newChild);
        assertThat(actual, is(expected));
    }

    @Test
    public void givenChildFromTwoMaleParentsFromFamilyTree_whenAddChild_thenExceptionIsThrown() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        FamilyMember badMaleParent = FamilyMember.builder().gender(Gender.MALE).firstParent(firstParent).secondParent(secondParent).build();
        familyTree.addChild(badMaleParent);

        FamilyMember newChild = FamilyMember.builder().firstParent(firstParent).secondParent(badMaleParent).build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> familyTree.addChild(newChild));
        String expectedMessage = "One parent must be female";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage, is(expectedMessage));
    }

    @Test
    public void givenChildFromTwoFemaleParentsFromFamilyTree_whenAddChild_thenExceptionIsThrown() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        FamilyMember badFemaleParent = FamilyMember.builder().gender(Gender.FEMALE).firstParent(firstParent).secondParent(secondParent).build();
        familyTree.addChild(badFemaleParent);

        FamilyMember newChild = FamilyMember.builder().firstParent(badFemaleParent).secondParent(secondParent).build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> familyTree.addChild(newChild));
        String expectedMessage = "One parent must be male";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage, is(expectedMessage));
    }

    @Test
    public void givenChildFromParentsNotInFamilyTree_whenAddChild_thenExceptionIsThrown() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        FamilyMember badFirstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember badSecondParent = FamilyMember.builder().gender(Gender.FEMALE).build();

        FamilyMember newChild = FamilyMember.builder().firstParent(badFirstParent).secondParent(badSecondParent).build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> familyTree.addChild(newChild));
        String expectedMessage = "Both parents must be part of the family tree";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage, is(expectedMessage));
    }

    @Test
    public void givenChildFromOneParentInFamilyTreeAndOtherNot_whenAddChild_thenExceptionIsThrown() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        InMemoryFamilyTree familyTree = new InMemoryFamilyTree(firstParent, secondParent);

        FamilyMember badSecondParent = FamilyMember.builder().gender(Gender.FEMALE).build();

        FamilyMember newChild = FamilyMember.builder().firstParent(firstParent).secondParent(badSecondParent).build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> familyTree.addChild(newChild));
        String expectedMessage = "Both parents must be part of the family tree";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage, is(expectedMessage));
    }
}