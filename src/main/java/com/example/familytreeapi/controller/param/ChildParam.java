package com.example.familytreeapi.controller.param;

import com.example.familytreeapi.model.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChildParam {
    private String firstName;
    private Gender gender;
    private Long firstParentId;
    private Long secondParentId;
}
