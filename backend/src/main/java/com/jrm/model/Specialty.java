package com.jrm.model;

import java.util.Set;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@Validated
@NoArgsConstructor
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre del especialidad no puede ser nulo")
    @Column(unique = true)
    private String cod;

    @Column(unique = true)
    @NotNull(message = "El nombre del especialidad no puede ser nulo")
    private String name;

    // @OneToMany(mappedBy = "specialty")
    // private Set<User> users;

  

}
