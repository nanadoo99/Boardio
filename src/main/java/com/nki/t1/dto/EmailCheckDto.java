package com.nki.t1.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class EmailCheckDto {
    @Email
    private String email;
    private String authNum;
}
