package com.example.familytreeapi.controller;

import com.example.familytreeapi.controller.param.ParentsParam;
import com.example.familytreeapi.model.FamilyMember;
import com.example.familytreeapi.repository.InMemoryFamilyTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/family_member")
public class FamilyMemberController {
    private static final Logger LOGGER = LogManager.getLogManager().getLogger("");

    private final InMemoryFamilyTree familyTree;

    @Autowired
    public FamilyMemberController(InMemoryFamilyTree familyTree) {
        this.familyTree = familyTree;
    }

    @GetMapping(value = "/parents", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getParents(@RequestParam Long id) {
        Optional<FamilyMember> childOptional = familyTree.findById(id);

        if(childOptional.isEmpty()) {
            LOGGER.info("Failed to find child with id=" + id + " in family tree");
            return new ResponseEntity<>(
                    "Failed to find child with id=" + id + " in family tree",
                    HttpStatus.BAD_REQUEST);
        } else {
            FamilyMember child = childOptional.get();

            LOGGER.info("Received request to get parents of child:" + child);
            ParentsParam parentsParam = new ParentsParam(child.getFirstParent(), child.getSecondParent());
            return ResponseEntity.ok(parentsParam);
        }
    }

    @GetMapping(value = "/children", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getChildren(@RequestParam Long id) {
        Optional<FamilyMember> familyMemberOptional = familyTree.findById(id);

        if(familyMemberOptional.isEmpty()) {
            LOGGER.info("Failed to find family member with id=" + id + " in family tree");
            return new ResponseEntity<>(
                    "Failed to find family member with id=" + id + " in family tree",
                    HttpStatus.BAD_REQUEST);
        } else {
            FamilyMember familyMember = familyMemberOptional.get();

            LOGGER.info("Received request to get children of family member:" + familyMember);
            return ResponseEntity.ok(familyMember.getChildren());
        }
    }

    @GetMapping(value = "/descendants", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getDescendants(@RequestParam Long id) {
        Optional<FamilyMember> familyMemberOptional = familyTree.findById(id);

        if(familyMemberOptional.isEmpty()) {
            LOGGER.info("Failed to find family member with id=" + id + " in family tree");
            return new ResponseEntity<>(
                    "Failed to find family member with id=" + id + " in family tree",
                    HttpStatus.BAD_REQUEST);
        } else {
            FamilyMember familyMember = familyMemberOptional.get();

            LOGGER.info("Received request to get descendants of family member:" + familyMember);
            return ResponseEntity.ok(familyMember.getDescendants());
        }
    }

    @GetMapping(value = "/ancestors", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getAncestors(@RequestParam Long id) {
        Optional<FamilyMember> familyMemberOptional = familyTree.findById(id);

        if(familyMemberOptional.isEmpty()) {
            LOGGER.info("Failed to find family member with id=" + id + " in family tree");
            return new ResponseEntity<>(
                    "Failed to find family member with id=" + id + " in family tree",
                    HttpStatus.BAD_REQUEST);
        } else {
            FamilyMember familyMember = familyMemberOptional.get();

            LOGGER.info("Received request to get ancestors of family member:" + familyMember);
            return ResponseEntity.ok(familyMember.getAncestors());
        }
    }
}
