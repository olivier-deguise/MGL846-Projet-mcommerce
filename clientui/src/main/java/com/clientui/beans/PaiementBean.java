package com.clientui.beans;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class PaiementBean {

    private int id;

    private Integer idCommande;

    private Double montant;

    private Long numeroCarte;

}
