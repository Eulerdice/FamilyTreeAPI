package com.example.familytreeapi.controller.param;

import com.example.familytreeapi.model.FamilyMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentsParam {
    private FamilyMember firstParent;
    private FamilyMember secondParent;
}
