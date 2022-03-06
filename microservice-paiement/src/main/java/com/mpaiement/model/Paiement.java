package com.mpaiement.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true)
    private Integer idCommande;

    private Double montant;

    private Long numeroCarte;

}
