package com.example.project.model;

import jakarta.persistence.Entity;

@Entity
public class ClientParticulier extends Client {
    private String typeClient;
    private String statut;

    public String getTypeClient() { return typeClient; }
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}