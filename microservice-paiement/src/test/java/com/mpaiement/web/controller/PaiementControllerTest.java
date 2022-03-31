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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

@Tag("UnitTests")
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

    @Test
    void payerUneCommandeNotPaidWithNoArgsConstr(){
        //given
        Paiement paiement = new Paiement();
        paiement.setId(1);
        paiement.setIdCommande(1);
        paiement.setMontant(10.00);
        paiement.setNumeroCarte(Long.valueOf("1234567891234567"));

        CommandeBean commande = new CommandeBean();
        commande.setId(1);
        commande.setDateCommande(LocalDate.now());
        commande.setQuantite(1);
        commande.setProductId(1);
        commande.setCommandePayee(Boolean.FALSE);

        System.out.println("Commande: Id=" + commande.getId() + ", dateCommande=" + commande.getDateCommande() + ", quantité="+ commande.getQuantite() + ", productId=" + commande.getProductId() + ", commandePayee=" + commande.getCommandePayee());
        System.out.println("Paiement: Id=" + paiement.getId() + ", idCommande=" + paiement.getIdCommande() + ", montant=" + paiement.getMontant() + ", numeroCarte=" + paiement.getNumeroCarte());

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
}
