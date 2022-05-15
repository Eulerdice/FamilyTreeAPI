package com.example.familytreeapi.controller;

import com.example.familytreeapi.model.FamilyMember;
import com.example.familytreeapi.model.Gender;
import com.example.familytreeapi.repository.InMemoryFamilyTree;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    InMemoryFamilyTree inMemoryFamilyTree;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenFamilyTreeInitialisedAndChildInTree_whenFindParents_thenParentsAreReturned() throws Exception {
        // Initialise tree
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        inMemoryFamilyTree.initialiseNewTree(firstParent, secondParent);

        FamilyMember child = FamilyMember.builder().firstParent(firstParent).secondParent(secondParent).build();
        inMemoryFamilyTree.addChild(child);

        this.mockMvc.perform(get("/api/family_member/get_parents").param("id", child.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstParent.id", is(firstParent.getId())))
                .andExpect(jsonPath("$.secondParent.id", is(secondParent.getId())));
    }

    @Test
    public void givenChildNotInFamilyTree_whenFindParents_thenErrorThrown() throws Exception {
        FamilyMember child = FamilyMember.builder().build();

        this.mockMvc.perform(get("/api/family_member/get_parents").param("id", child.getId().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to find child with id="+child.getId()+" in family tree"));
    }

}