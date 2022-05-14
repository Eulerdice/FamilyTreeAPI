package com.example.familytreeapi.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

class FamilyMemberTest {
    @Test
    public void givenFamilyMemberWithNoDescendants_whenGetDescendants_thenEmptyListIsReturned() {
        FamilyMember familyMember = FamilyMember.builder().build();

        List<FamilyMember> expected = emptyList();
        List<FamilyMember> actual = familyMember.getDescendants();
        assertThat(actual, is(expected));
    }

    @Test
    public void givenFamilyMemberWithOneDescendant_whenGetDescendants_thenListWithSingleDescendantIsReturned() {
        FamilyMember descendant = FamilyMember.builder().build();
        FamilyMember familyMember = FamilyMember.builder().children(List.of(descendant)).build();

        List<FamilyMember> expected = List.of(descendant);
        List<FamilyMember> actual = familyMember.getDescendants();
        assertThat(actual, is(expected));
    }

    @Test
    public void givenFamilyMemberWithOneChildAndMultipleDescendants_whenGetDescendants_thenListOfAllDescendantsIsReturned() {
        FamilyMember childDescendant1 = FamilyMember.builder().build();
        FamilyMember childDescendant2 = FamilyMember.builder().build();
        FamilyMember child = FamilyMember.builder().children(List.of(childDescendant1, childDescendant2)).build();

        FamilyMember familyMember = FamilyMember.builder().children(List.of(child)).build();

        List<FamilyMember> expected = List.of(child, childDescendant1, childDescendant2);
        List<FamilyMember> actual = familyMember.getDescendants();
        assertThat(actual, is(expected));
    }

    @Test
    public void givenInitialFamilyTreeParent_whenGetAncestors_thenEmptyListIsReturned() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();

        List<FamilyMember> expected = emptyList();
        List<FamilyMember> actual = firstParent.getAncestors();
        assertThat(actual, is(expected));
    }

    @Test
    public void givenChildFromInitialFamilyTreeParents_whenGetAncestors_thenListWithInitialParentsIsReturned() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();

        FamilyMember child = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();

        List<FamilyMember> expected = List.of(firstParent, secondParent);
        List<FamilyMember> actual = child.getAncestors();
        assertThat(actual, is(expected));
    }

    @Test
    public void givenChildOfOtherChildren_whenGetAncestors_thenListWithAllAncestorsIsReturned() {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        FamilyMember firstChild = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();
        FamilyMember secondChild = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();

        FamilyMember child = FamilyMember.builder().firstParent(firstChild).secondParent(secondChild).build();

        List<FamilyMember> expected = List.of(firstChild, secondChild, firstParent, secondParent);
        List<FamilyMember> actual = child.getAncestors();
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }


}