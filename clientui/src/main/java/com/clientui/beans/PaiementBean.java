package com.clientui.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class PaiementBean {

    private int id;

    private Integer idCommande;

    private Double montant;

    private Long numeroCarte;

}
