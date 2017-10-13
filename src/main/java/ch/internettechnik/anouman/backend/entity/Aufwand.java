package ch.internettechnik.anouman.backend.entity;

/**
 * Created by michzuerch on 06.07.15.
 */

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = "Aufwand.findAll", query = "SELECT a FROM Aufwand a order by a.start"),
        @NamedQuery(name = "Aufwand.findById", query = "SELECT a FROM Aufwand a where a.id = :id"),
        @NamedQuery(name = "Aufwand.findByTitel", query = "SELECT a FROM Aufwand a where a.titel LIKE :titel")
})
public class Aufwand extends AbstractEntity {
    @Column
    @NotNull
    private String titel;

    @Column
    private String bezeichnung;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date ende;

    @ManyToOne
    private ch.internettechnik.anouman.backend.entity.Rechnung rechnung;

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnde() {
        return ende;
    }

    public void setEnde(Date ende) {
        this.ende = ende;
    }

    public Rechnung getRechnung() {
        return rechnung;
    }

    public void setRechnung(Rechnung rechnung) {
        this.rechnung = rechnung;
    }

    @Transient
    public Double getDauerInMinuten() {
        return Double.valueOf(((ende.getTime() - start.getTime()) / 1000) / 60);
    }

    @Transient
    public Double getDauerInStunden() {
        return Double.valueOf((((ende.getTime() - start.getTime()) / 1000) / 60) / 60);
    }

    @Transient
    public Double getPositionstotal() {
        BigDecimal stk = null;
        if (getRechnung() != null) {
            stk = new BigDecimal(getRechnung().getAdresse().getStundensatz());
        } else {
            stk = new BigDecimal(130);
        }
        BigDecimal anz = new BigDecimal(getDauerInStunden());
        BigDecimal total = stk.multiply(anz);
        return total.doubleValue();
    }

    @Override
    public String toString() {
        return "Aufwand{" +
                ", id=" + id +
                "titel='" + titel + '\'' +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", start=" + start +
                ", ende=" + ende +
                ", rechnung=" + rechnung +
                '}';
    }
}
