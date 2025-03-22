package com.casino.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class PlayerDto {
    private Long id;
    private String name;
    private String username;
    private String password;
    private double balance;
    private LocalDate birthdate;
}
