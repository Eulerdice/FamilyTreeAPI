package com.example.familytreeapi.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
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
}