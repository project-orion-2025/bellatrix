package com.example.project_orion.models;

import com.example.project_orion.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "persons")
public class Person{

    @Id
    private String userId;

    private String username;

    private String collegeName;

    private String bio;

    private Date dateOfBirth;

    private Gender gender;

    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
}
