package com.example.project.model;

import jakarta.persistence.Entity;

@Entity
public class ClientProfessionnel extends Client {
    private String entreprise;
    private String typeClient;
    private String siret;
    private String adresseEntreprise;

    public String getEntreprise() { return entreprise; }
    public void setEntreprise(String entreprise) { this.entreprise = entreprise; }
    public String getTypeClient() { return typeClient; }
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }
    public String getSiret() { return siret; }
    public void setSiret(String siret) { this.siret = siret; }
    public String getAdresseEntreprise() { return adresseEntreprise; }
    public void setAdresseEntreprise(String adresseEntreprise) { this.adresseEntreprise = adresseEntreprise; }
}