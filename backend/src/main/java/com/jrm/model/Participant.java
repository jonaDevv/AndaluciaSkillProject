package com.jrm.model;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@Validated
@NoArgsConstructor
public class Participant {

}
