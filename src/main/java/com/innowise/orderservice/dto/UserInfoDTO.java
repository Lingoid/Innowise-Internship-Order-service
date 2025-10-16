package com.innowise.orderservice.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
}