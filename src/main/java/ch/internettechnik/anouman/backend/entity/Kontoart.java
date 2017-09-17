package ch.internettechnik.anouman.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by michzuerch on 25.08.15.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Kontoart.findAll", query = "SELECT k FROM Kontoart k"),
        @NamedQuery(name = "Kontoart.findById", query = "SELECT k FROM Kontoart k where k.id = :id")
})
public class Kontoart extends AbstractEntity {
    @Column
    @NotNull
    private String bezeichnung;

    @Column
    @NotNull
    private String kontonummer;

    @ManyToOne
    private Kontogruppe kontogruppe;

    @OneToMany(mappedBy = "kontoart")
    private Set<Konto> kontos = new HashSet<>();

    @Transient
    public String getShowKontonummer() {
        return getKontogruppe().getShowKontonummer() + getKontonummer();
    }

    public Kontogruppe getKontogruppe() {
        return kontogruppe;
    }

    public void setKontogruppe(Kontogruppe kontogruppe) {
        this.kontogruppe = kontogruppe;
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public void setKontonummer(String kontonummer) {
        this.kontonummer = kontonummer;
    }

    public Set<Konto> getKontos() {
        return kontos;
    }

    public void setKontos(Set<Konto> kontos) {
        this.kontos = kontos;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
}