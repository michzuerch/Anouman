package ch.internettechnik.anouman.backend.entity;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: michzuerch
 * Date: 31.08.14
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Rechnung.findAll", query = "SELECT r FROM Rechnung r"),
        @NamedQuery(name = "Rechnung.findById", query = "SELECT r FROM Rechnung r where r.id = :id"),
        @NamedQuery(name = "Rechnung.findByDatum", query = "SELECT r FROM Rechnung r where r.rechnungsdatum = :datum"),
        @NamedQuery(name = "Rechnung.findByBezeichnungLike", query = "SELECT r FROM Rechnung r where r.bezeichnung LIKE :bezeichnung")
})
@XmlAccessorType(XmlAccessType.NONE)
public class Rechnung extends AbstractEntity {
    @Temporal(TemporalType.DATE)
    @XmlElement
    @NotNull
    private Date rechnungsdatum;

    @Column
    @NotNull
    @Size(min = 3, max = 50)
    @XmlElement
    private String bezeichnung;

    @Column
    @NotNull
    @Range(min = 1, max = 365)
    @XmlElement
    private int faelligInTagen;

    @Column
    @XmlElement
    private boolean bezahlt;

    @Column
    @XmlElement
    private boolean verschickt;

    @ManyToOne
    private Adresse adresse;

    @XmlElement
    @OneToMany(mappedBy = "rechnung", cascade = CascadeType.ALL)
    private Set<Rechnungsposition> rechnungspositionen = new HashSet<Rechnungsposition>();

    @XmlElement
    @OneToMany(mappedBy = "rechnung", cascade = CascadeType.ALL)
    private Set<Aufwand> aufwands = new HashSet<Aufwand>();

    public Rechnung() {
    }

    public Rechnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Date getRechnungsdatum() {
        return rechnungsdatum;
    }

    public void setRechnungsdatum(Date rechnungsdatum) {
        this.rechnungsdatum = rechnungsdatum;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public int getFaelligInTagen() {
        return faelligInTagen;
    }

    public void setFaelligInTagen(int faelligInTagen) {
        this.faelligInTagen = faelligInTagen;
    }

    public boolean isBezahlt() {
        return bezahlt;
    }

    public void setBezahlt(boolean bezahlt) {
        this.bezahlt = bezahlt;
    }

    public boolean isVerschickt() {
        return verschickt;
    }

    public void setVerschickt(boolean verschickt) {
        this.verschickt = verschickt;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Set<Rechnungsposition> getRechnungspositionen() {
        return rechnungspositionen;
    }

    public void setRechnungspositionen(Set<Rechnungsposition> rechnungspositionen) {
        this.rechnungspositionen = rechnungspositionen;
    }

    public Set<Aufwand> getAufwands() {
        return aufwands;
    }

    public void setAufwands(Set<Aufwand> aufwands) {
        this.aufwands = aufwands;
    }

    @Transient
    public Float getZwischentotal() {
        BigDecimal zt = new BigDecimal(0f);
        for (Rechnungsposition p : getRechnungspositionen()) {
            BigDecimal pt = new BigDecimal(p.getPositionstotal());
            zt = zt.add(pt);
        }
        for (Aufwand aw : getAufwands()) {
            BigDecimal pt = new BigDecimal(aw.getPositionstotal());
            zt = zt.add(pt);
        }
        Float result = zt.floatValue();
        return result;
    }

    @Transient
    public Float getMehrwertsteuer() {
        BigDecimal zt = new BigDecimal(getZwischentotal());
        BigDecimal mwst = zt.multiply(BigDecimal.valueOf(0.08));
        return mwst.floatValue();
    }

    @Transient
    public Float getRechnungstotal() {
        BigDecimal zt = new BigDecimal(getZwischentotal());
        zt = zt.add(new BigDecimal(getMehrwertsteuer()));
        return zt.floatValue();
    }

    @Transient
    public int getAnzahlRechnungspositionenUndAufwands() {
        int t = getRechnungspositionen().size() + getAufwands().size();
        return t;
    }

    @Transient
    public int getAnzahlRechnungspositionen() {
        int t = getRechnungspositionen().size();
        return t;
    }

    @Transient
    public int getAnzahlAufwands() {
        int t = getAufwands().size();
        return t;
    }

    @Transient
    public Date getFaelligkeitsdatum() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getRechnungsdatum());
        cal.add(Calendar.DATE, getFaelligInTagen());
        return cal.getTime();
    }

    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.adresse = (Adresse) parent;
    }

}