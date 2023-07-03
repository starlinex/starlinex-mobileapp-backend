package com.starlinex.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TempUser {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String password;
}
