package com.mpaiement.beans;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
public class CommandeBean {

    private int id;

    private Integer productId;

    private LocalDate dateCommande;

    private Integer quantite;

    private Boolean commandePayee;

}