package com.clientui.beans;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class ProductBean {

    private int id;

    private String titre;

    private String description;

    private String image;

    private Double prix;

}
