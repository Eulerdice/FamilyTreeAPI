package com.example.familytreeapi.controller;

import com.example.familytreeapi.controller.param.ChildParam;
import com.example.familytreeapi.controller.param.ParentsParam;
import com.example.familytreeapi.model.FamilyMember;
import com.example.familytreeapi.repository.InMemoryFamilyTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/family_tree")
public class FamilyTreeController {
    private static final Logger LOGGER = LogManager.getLogManager().getLogger("");

    private InMemoryFamilyTree familyTree;

    @Autowired
    public FamilyTreeController(InMemoryFamilyTree familyTree) {
        this.familyTree = familyTree;
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity initialiseFamilyTree(@RequestBody ParentsParam parents) {
        if(parents.getFirstParent() == null || parents.getSecondParent() == null) {
            return new ResponseEntity<>(
                    "Failed to initialise new family tree due to the input error: Missing either firstParent or secondParent from the request",
                    HttpStatus.BAD_REQUEST);
        }
        LOGGER.info("Received request to initialise new tree with the values of: firstParent:" + parents.getFirstParent() + ", secondParent:" + parents.getSecondParent());

        Random random = new Random();
        FamilyMember firstParent = new FamilyMember(random.nextLong(), parents.getFirstParent().getFirstName(), parents.getFirstParent().getGender(), null, null, new ArrayList<>());
        FamilyMember secondParent = new FamilyMember(random.nextLong(), parents.getSecondParent().getFirstName(), parents.getSecondParent().getGender(), null, null, new ArrayList<>());

        try {
            LOGGER.info("Initialising Tree with parents - firstParent:" + firstParent + ", secondParent:" +secondParent);
            this.familyTree = new InMemoryFamilyTree(firstParent, secondParent);
        } catch (IllegalArgumentException e) {
            LOGGER.info("Failed to initialise new family tree due to the input error: " + e.getMessage());
            return new ResponseEntity<>(
                    "Failed to initialise new family tree due to the input error: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            LOGGER.info("Failed to initialise new family tree due to an unexpected error: " + e.getMessage());
            return new ResponseEntity<>(
                    "Failed to initialise new family tree due to an unexpected error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(this.familyTree);
    }

    @PostMapping(value = "/add_child", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity addChild(@RequestBody ChildParam childParam) {
        // Check tree is initialised and parents are part of it
        if(familyTree.getFirstParent() == null || familyTree.getSecondParent() == null) {
            LOGGER.info("Failed to add child because Family tree was not initialised");
            return new ResponseEntity<>(
                    "Failed to add child because Family tree was not initialised. Please use /api/family_tree/new first",
                    HttpStatus.BAD_REQUEST);
        }
        if(familyTree.findById(childParam.getFirstParentId()).isEmpty()) {
            LOGGER.info("Failed to add child because no parent could be found in Family tree with id " + childParam.getFirstParentId());
            return new ResponseEntity<>(
                    "Failed to add child because no parent could be found in Family tree with id " + childParam.getFirstParentId(),
                    HttpStatus.BAD_REQUEST);
        }
        if(familyTree.findById(childParam.getSecondParentId()).isEmpty()) {
            LOGGER.info("Failed to add child because no parent could be found in Family tree with id " + childParam.getSecondParentId());
            return new ResponseEntity<>(
                    "Failed to add child because no parent could be found in Family tree with id " + childParam.getSecondParentId(),
                    HttpStatus.BAD_REQUEST);
        }

        FamilyMember firstParent = familyTree.findById(childParam.getFirstParentId()).get();
        FamilyMember secondParent = familyTree.findById(childParam.getSecondParentId()).get();
        FamilyMember child = new FamilyMember(new Random().nextLong(), childParam.getFirstName(), childParam.getGender(), firstParent, secondParent, new ArrayList<>());

        try {
            familyTree.addChild(child);
        } catch (IllegalArgumentException e) {
            LOGGER.info("Failed to initialise new family tree due to the input error: " + e.getMessage());
            return new ResponseEntity<>(
                    "Failed to initialise new family tree due to the input error: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            LOGGER.info("Failed to initialise new family tree due to an unexpected error: " + e.getMessage());
            return new ResponseEntity<>(
                    "Failed to initialise new family tree due to an unexpected error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(this.familyTree);
    }
}
