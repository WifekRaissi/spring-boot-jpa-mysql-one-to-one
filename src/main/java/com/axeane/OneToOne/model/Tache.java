package com.axeane.OneToOne.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "tache")
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private static final AtomicInteger count = new AtomicInteger(0);
    @NotEmpty
    @NotNull
    private String nom;
    @NotEmpty
    @NotNull
    private String description;

    private Timestamp delai;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salarie_id", nullable = false)
    private Salarie salarie;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Tache() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDelai() {
        return delai;
    }

    public void setDelai(Timestamp delai) {
        this.delai = delai;
    }

    @JsonIgnore
    public Salarie getSalarie() {
        return salarie;
    }

    public void setSalarie(Salarie salarie) {
        this.salarie = salarie;
    }

    @Override
    public String toString() {
        return "Tache{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", delai=" + delai +
                '}';
    }
}
