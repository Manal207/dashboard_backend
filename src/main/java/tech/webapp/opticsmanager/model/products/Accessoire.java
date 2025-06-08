package tech.webapp.opticsmanager.model.products;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accessoire extends Produit{

    private String nom;
    private String reference;
    private String description;
    private String marque;

    @Column(name = "quantite_stock")
    private int quantiteStock;

}
