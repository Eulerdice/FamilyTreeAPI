package com.example.familytreeapi.controller;

import com.example.familytreeapi.model.FamilyMember;
import com.example.familytreeapi.model.Gender;
import com.example.familytreeapi.repository.InMemoryFamilyTree;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FamilyMemberControllerTest {
    @Autowired
    private InMemoryFamilyTree inMemoryFamilyTree;

    @Autowired
    private MockMvc mockMvc;

    //region getParents
    @Test
    public void givenFamilyTreeInitialisedAndChildInTree_whenGetParents_thenParentsAreReturned() throws Exception {
        // Initialise tree
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        inMemoryFamilyTree.initialiseNewTree(firstParent, secondParent);

        FamilyMember child = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();
        inMemoryFamilyTree.addChild(child);

        this.mockMvc.perform(get("/api/family_member/parents").param("id", child.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstParent.id", is(firstParent.getId())))
                .andExpect(jsonPath("$.secondParent.id", is(secondParent.getId())));
    }

    @Test
    public void givenChildNotInFamilyTree_whenGetParents_thenErrorThrown() throws Exception {
        FamilyMember child = FamilyMember.builder().build();

        this.mockMvc.perform(get("/api/family_member/parents").param("id", child.getId().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to find child with id="+child.getId()+" in family tree"));
    }
    //endregion

    //region getChildren
    @Test
    public void givenFamilyTreeInitialisedAndParentInTreeWithNoChildren_whenGetChildren_thenEmptyListIsReturned() throws Exception {
        // Initialise tree
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        inMemoryFamilyTree.initialiseNewTree(firstParent, secondParent);

        this.mockMvc.perform(get("/api/family_member/children").param("id", firstParent.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    public void givenFamilyTreeInitialisedAndParentInTreeWithOneChild_whenGetChildren_thenListWithChildIsReturned() throws Exception {
        // Initialise tree
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        inMemoryFamilyTree.initialiseNewTree(firstParent, secondParent);

        FamilyMember child = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();
        inMemoryFamilyTree.addChild(child);

        this.mockMvc.perform(get("/api/family_member/children").param("id", firstParent.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(child.getId())));
    }
    //endregion

    //region getDescendants
    @Test
    public void givenFamilyTreeInitialisedAndParentInTreeWithNoChildren_whenGetDescendants_thenEmptyListIsReturned() throws Exception {
        // Initialise tree
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        inMemoryFamilyTree.initialiseNewTree(firstParent, secondParent);

        this.mockMvc.perform(get("/api/family_member/descendants").param("id", firstParent.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    public void givenFamilyTreeInitialisedAndParentInTreeWithMultipleChildrenAndChildrenOfChildren_whenGetDescendants_thenListWithAllChildrenIsReturned() throws Exception {
        // Initialise tree
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        inMemoryFamilyTree.initialiseNewTree(firstParent, secondParent);

        FamilyMember child = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();
        inMemoryFamilyTree.addChild(child);

        FamilyMember child2 = FamilyMember.builder().gender(Gender.FEMALE).firstParent(firstParent).secondParent(secondParent).build();
        inMemoryFamilyTree.addChild(child2);

        FamilyMember child1Child2Child = FamilyMember.builder().firstParent(child).secondParent(child2).build();
        inMemoryFamilyTree.addChild(child1Child2Child);

        this.mockMvc.perform(get("/api/family_member/descendants").param("id", firstParent.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)));
    }
    //endregion

    //region getAncestors
    @Test
    public void givenFamilyTreeInitialisedAndFamilyMemberWithNoAncestors_whenGetAncestors_thenEmptyListIsReturned() throws Exception {
        // Initialise tree
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        inMemoryFamilyTree.initialiseNewTree(firstParent, secondParent);

        this.mockMvc.perform(get("/api/family_member/ancestors").param("id", firstParent.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    public void givenFamilyTreeInitialisedAndFamilyMemberWithMultipleAncestors_whenGetAncestors_thenAllAncestorsAreReturned() throws Exception {
        // Initialise tree
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        inMemoryFamilyTree.initialiseNewTree(firstParent, secondParent);

        FamilyMember child = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();
        inMemoryFamilyTree.addChild(child);

        FamilyMember child2 = FamilyMember.builder().gender(Gender.FEMALE).firstParent(firstParent).secondParent(secondParent).build();
        inMemoryFamilyTree.addChild(child2);

        FamilyMember child1Child2Child = FamilyMember.builder().firstParent(child).secondParent(child2).build();
        inMemoryFamilyTree.addChild(child1Child2Child);

        this.mockMvc.perform(get("/api/family_member/ancestors").param("id", child1Child2Child.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(4)));
    }
    //endregion
}