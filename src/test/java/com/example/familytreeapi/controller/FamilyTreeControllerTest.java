package com.example.familytreeapi.controller;

import com.example.familytreeapi.controller.param.ChildParam;
import com.example.familytreeapi.controller.param.ParentsParam;
import com.example.familytreeapi.model.FamilyMember;
import com.example.familytreeapi.model.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FamilyTreeControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    //region new
    @Test
    public void givenTwoParents_whenNewMethod_thenNewFamilyTreeInitalised() throws Exception {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        ParentsParam parentsParam = ParentsParam.builder().firstParent(firstParent).secondParent(secondParent).build();
        this.mockMvc.perform(post("/api/family_tree/new")
                        .content(objectMapper.writeValueAsString(parentsParam))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstParent.firstName", is(firstParent.getFirstName())))
                .andExpect(jsonPath("$.secondParent.firstName", is(secondParent.getFirstName())))
                .andExpect(jsonPath("$.familyMembers.length()", is(2)));
    }

    @Test
    public void givenOneParent_whenNewMethod_thenExceptionIsThrown() throws Exception {
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        ParentsParam parentsParam = ParentsParam.builder().firstParent(firstParent).build();
        this.mockMvc.perform(post("/api/family_tree/new")
                        .content(objectMapper.writeValueAsString(parentsParam))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to initialise new family tree due to the input error: Missing either firstParent or secondParent from the request"));
    }
    //endregion

    //region addChild
    @Test
    public void givenInitialisedTreeAndValidChild_whenAddChild_thenChildIsAddedToFamilyTree() throws Exception {
        // Initialise tree
        FamilyMember firstParent = FamilyMember.builder().gender(Gender.MALE).build();
        FamilyMember secondParent = FamilyMember.builder().gender(Gender.FEMALE).build();
        ParentsParam parentsParam = ParentsParam.builder().firstParent(firstParent).secondParent(secondParent).build();
        MvcResult mvcResult = this.mockMvc.perform(post("/api/family_tree/new")
                        .content(objectMapper.writeValueAsString(parentsParam))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        Long firstParentId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.firstParent.id");
        Long secondParentId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.secondParent.id");
        ChildParam childParam = ChildParam.builder().firstParentId(firstParentId)
                .secondParentId(secondParentId).build();

        this.mockMvc.perform(post("/api/family_tree/add_child")
                .content(objectMapper.writeValueAsString(childParam))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstParent.firstName", is(firstParent.getFirstName())))
                .andExpect(jsonPath("$.secondParent.firstName", is(secondParent.getFirstName())))
                .andExpect(jsonPath("$.firstName", is(childParam.getFirstName())));
    }

    @Test
    public void givenUninitialisedTree_whenAddChild_thenErrorIsThrown() throws Exception {
        ChildParam childParam = ChildParam.builder().build();

        this.mockMvc.perform(post("/api/family_tree/add_child")
                .content(objectMapper.writeValueAsString(childParam))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to add child because Family tree was not initialised. Please use /api/family_tree/new first"));
    }
    //endregion
}