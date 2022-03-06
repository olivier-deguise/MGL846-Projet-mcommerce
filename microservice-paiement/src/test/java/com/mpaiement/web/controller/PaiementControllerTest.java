package com.mpaiement.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mpaiement.beans.CommandeBean;
import com.mpaiement.dao.PaiementDao;
import com.mpaiement.model.Paiement;
import com.mpaiement.proxies.MicroserviceCommandeProxy;
import com.mpaiement.web.controller.PaiementController;
import com.mpaiement.web.exceptions.PaiementExistantException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PaiementControllerTest {

    @Mock private PaiementDao paiementDao;
    @Mock private MicroserviceCommandeProxy microserviceCommandeProxy;

    private PaiementController underTest;

    @BeforeEach
    void setUp() {
        underTest = new PaiementController(paiementDao, microserviceCommandeProxy);
    }

    @Test
    void payerUneCommandeNotPaid(){
        //given
        Paiement paiement = Paiement.builder()
                .id(1)
                .idCommande(1)
                .montant(10.0)
                .numeroCarte(Long.valueOf("1234567891234567"))
                .build();

        CommandeBean commande = CommandeBean.builder()
                .id(1)
                .dateCommande(LocalDate.now())
                .quantite(1)
                .productId(1)
                .commandePayee(Boolean.FALSE)
                .build();
        Optional<CommandeBean> commandeOpt = Optional.of(commande);

        given(paiementDao.findByidCommande(paiement.getIdCommande())).willReturn(null);
        given(paiementDao.save(paiement)).willReturn(paiement);
        given(microserviceCommandeProxy.recupererUneCommande(paiement.getIdCommande())).willReturn(commandeOpt);

        //when
        ResponseEntity<Paiement> response = underTest.payerUneCommande(paiement);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(paiement);
    }

    @Test
    void payerUneCommandeAlreadyPaid(){
        //given
        Paiement paiement = Paiement.builder()
                .id(1)
                .idCommande(1)
                .montant(10.0)
                .numeroCarte(Long.valueOf("1234567891234567"))
                .build();

        given(paiementDao.findByidCommande(paiement.getIdCommande())).willReturn(paiement);

        //when

        //then
        assertThatThrownBy(() -> underTest.payerUneCommande(paiement))
                .isInstanceOf(PaiementExistantException.class)
                .hasMessageContaining("Cette commande est déjà payée");
    }

}
