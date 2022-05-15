package com.example.familytreeapi.controller.param;

import com.example.familytreeapi.model.FamilyMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ParentsParam {
    private FamilyMember firstParent;
    private FamilyMember secondParent;
}
