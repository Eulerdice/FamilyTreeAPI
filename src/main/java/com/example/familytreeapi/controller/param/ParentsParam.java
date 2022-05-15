package com.example.familytreeapi.controller.param;

import com.example.familytreeapi.model.FamilyMember;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ParentsParam {
    private FamilyMember firstParent;
    private FamilyMember secondParent;
}
