package com.clientui.beans;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class CommandeBean {

    private int id;

    private Integer productId;

    private LocalDate dateCommande;

    private Integer quantite;

    private Boolean commandePayee = Boolean.FALSE;


}
