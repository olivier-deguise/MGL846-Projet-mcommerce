package com.mcommandes.web.controller;

import com.mcommandes.dao.CommandesDao;
import com.mcommandes.model.Commande;
import com.mcommandes.web.exceptions.CommandeNotFoundException;
import com.mcommandes.web.exceptions.ImpossibleAjouterCommandeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class CommandeControllerTest {
    @Mock private CommandesDao commandesDao;

    private CommandeController underTest;

    @BeforeEach
    void setUp() {
        underTest = new CommandeController(commandesDao);
    }

    @Test
    void ajouterCommandeValid(){
        //given
        Commande commande = Commande.builder()
                                        .id(1)
                                        .dateCommande(LocalDate.now())
                                        .quantite(1)
                                        .productId(1)
                                        .commandePayee(Boolean.FALSE)
                                        .build();
        given(commandesDao.save(commande)).willReturn(commande);

        //when
        ResponseEntity<Commande> response = underTest.ajouterCommande(commande);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(commande);
    }

    @Test
    void ajouterCommandeInvalid(){
        //given
        given(commandesDao.save(null)).willReturn(null);

        //when

        //then
        assertThatThrownBy(() -> underTest.ajouterCommande(null))
                .isInstanceOf(ImpossibleAjouterCommandeException.class)
                .hasMessageContaining("Impossible d'ajouter cette commande");
    }

    @Test
    void recupererUneCommandeValid(){
        //given

        Commande commande = Commande.builder()
                .id(1)
                .dateCommande(LocalDate.now())
                .quantite(1)
                .productId(1)
                .commandePayee(Boolean.FALSE)
                .build();
        Optional<Commande> commandeOpt = Optional.of(commande);
        given(commandesDao.findById(1)).willReturn(commandeOpt);

        //when
        Optional<Commande> returnedCommand = underTest.recupererUneCommande(1);

        //then
        assertThat(returnedCommand.get()).isEqualTo(commande);
    }

    @Test
    void recupererUneCommandeInvalid(){
        //given
        given(commandesDao.findById(1)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> underTest.recupererUneCommande(1))
                .isInstanceOf(CommandeNotFoundException.class)
                .hasMessageContaining("Cette commande n'existe pas");
    }

    @Test
    void updateCommande(){
        //given
        Commande commande = Commande.builder()
                .id(1)
                .dateCommande(LocalDate.now())
                .quantite(1)
                .productId(1)
                .commandePayee(Boolean.FALSE)
                .build();
        given(commandesDao.save(commande)).willReturn(commande);

        //when
        underTest.updateCommande(commande);

        //then
        verify(commandesDao).save(commande);
    }

}
