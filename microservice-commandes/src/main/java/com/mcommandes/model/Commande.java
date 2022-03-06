package com.mcommandes.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue
    private int id;

    private Integer productId;

    private LocalDate dateCommande;

    private Integer quantite;

    private Boolean commandePayee = Boolean.FALSE;

}
