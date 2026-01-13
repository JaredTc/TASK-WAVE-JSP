package com.taskwave.taskwave.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResDTO {
    private String token;
    private  String refreshToken;
}
