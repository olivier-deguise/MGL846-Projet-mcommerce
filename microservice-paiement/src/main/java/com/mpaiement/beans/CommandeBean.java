package com.mpaiement.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommandeBean {

    private int id;

    private Integer productId;

    private LocalDate dateCommande;

    private Integer quantite;

    private Boolean commandePayee;

}