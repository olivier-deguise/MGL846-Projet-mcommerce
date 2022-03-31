package com.mproduits.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mproduits.configurations.ApplicationPropertiesConfiguration;
import com.mproduits.dao.ProductDao;
import com.mproduits.model.Product;
import com.mproduits.web.exceptions.ProductNotFoundException;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.actuate.health.Health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag("UnitTests")
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Mock private ProductDao productDao;
    @Mock private ApplicationPropertiesConfiguration apc;

    private ProductController underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProductController(productDao, apc);
    }

    @Test
    void listeDesProduitsCanGetProducts() {
        //given
        Product product = Product.builder()
                .id(1)
                .prix(10.00)
                .description("desc")
                .image("img")
                .build();
        List<Product> list = new ArrayList<Product>();
        list.add(product);
        given(productDao.findAll()).willReturn(list);

        // when
        underTest.listeDesProduits();

        //then
        verify(productDao).findAll();
    }

    @Test
    void listeDesProduitsWillReturnMaxDisplaySizeWhenDoaListIsGreater(){
        //given
        List<Product> list = new ArrayList<Product>();
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();

        list.add(product1);
        list.add(product2);
        list.add(product3);
        list.add(product4);

        int maxDisplaySize = 2;
        given(apc.getLimitDeProduits()).willReturn(maxDisplaySize);
        given(productDao.findAll()).willReturn(list);

        //when
        List<Product> returnedList = underTest.listeDesProduits();

        //then
        assertThat(returnedList.size()).isEqualTo(maxDisplaySize);
    }

    @Test
    void listeDesProduitsWillReturnProductListSizeWhenDoaListIsLower(){
        //given
        List<Product> list = new ArrayList<Product>();
        Product product1 = new Product();

        list.add(product1);

        int maxDisplaySize = 2;
        given(apc.getLimitDeProduits()).willReturn(maxDisplaySize);
        given(productDao.findAll()).willReturn(list);

        //when
        List<Product> returnedList = underTest.listeDesProduits();

        //then
        assertThat(returnedList.size()).isEqualTo(1);
    }

    @Test
    void listDesProduitsWillThrowProductNotFoundExceptionWhenProductListIsEmpty(){
        //given
        List<Product> list = new ArrayList<Product>();
        given(productDao.findAll()).willReturn(list);

        //when

        //then
        assertThatThrownBy(() -> underTest.listeDesProduits())
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Aucun produit n'est disponible à la vente");
    }

    @Test
    void recupererUnProduitThatExists(){
        //given
        int id = 1;
        Product product = new Product(id, "TitreBidon", "DescriptionBidon", "ImageBidon", 10.0);
        Optional<Product> optionalProduct = Optional.of(product);
        given(productDao.findById(id)).willReturn(optionalProduct);

        //when
        Optional<Product> returnedProduct = underTest.recupererUnProduit(id);

        //then
        assertThat(optionalProduct).isEqualTo(returnedProduct);
    }

    @Test
    void recupererUnProduitThatNotExists(){
        //given
        int id = 1;
        Optional<Product> optionalProduct = Optional.empty();
        given(productDao.findById(id)).willReturn(optionalProduct);

        //when

        //then
        assertThatThrownBy(() -> underTest.recupererUnProduit(id))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Le produit correspondant à l'id " + id + " n'existe pas");
    }

    @Test
    void healthWillBeDownIfProductListIsEmpty(){
        //given
        List<Product> list = new ArrayList<Product>();
        given(productDao.findAll()).willReturn(list);
        Health down = Health.down().build();

        //when
        Health returnedHealth = underTest.health();

        //then
        assertThat(returnedHealth).isEqualTo(down);
    }

    @Test
    void healthWillBeUpIfProductListIsNotEmpty(){
        //given
        List<Product> list = new ArrayList<Product>();
        Product product = new Product();
        list.add(product);
        given(productDao.findAll()).willReturn(list);
        Health up = Health.up().build();

        //when
        Health returnedHealth = underTest.health();

        //then
        assertThat(returnedHealth).isEqualTo(up);
    }

    @Test
    void toStringContainsProductValues(){
        //given
        int id = 55;
        Double prix = 10.00;
        String description = "desc1";
        String image = "img1";
        String titre = "titre1";

        Product product = new Product();
        product.setId(id);
        product.setPrix(prix);
        product.setDescription(description);
        product.setImage(image);
        product.setTitre(titre);

        //when
        String productString = product.toString();

        //then
        assertThat(productString).contains(String.valueOf(id), String.valueOf(prix), description, image, titre);
    }


}
