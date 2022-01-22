package com.clientui.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ProductBean {

    private int id;

    private String titre;

    private String description;

    private String image;

    private Double prix;

}
