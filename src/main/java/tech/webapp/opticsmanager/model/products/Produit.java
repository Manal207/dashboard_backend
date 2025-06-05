package tech.webapp.opticsmanager.model.products;

import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.*;


import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public abstract class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nature; // ex: Optique, Solaire, etc.

    @Column(name = "prefix_achat")
    private BigDecimal prixAchat;

    @Column(name = "prix_vente")
    private BigDecimal prixVente;

    @Column(name = "remise_client")
    private Double remiseClient;

    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_produit")
    private TypeProduit typeProduit;

    public enum TypeProduit {
        MONTURE, LENTILLE, VERRE, ACCESSOIRE
    }




}
