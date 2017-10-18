package ch.internettechnik.anouman.presentation.ui.backup.xml.templatebuchhaltungen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by michzuerch on 16.11.15.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class BackupTemplateKontogruppe {
    @XmlAttribute
    private String bezeichnung;
    @XmlAttribute
    private String kontonummer;
    @XmlElement(name = "konto")
    private Set<BackupTemplateKonto> backupTemplateKontos = new HashSet<>();

    public BackupTemplateKontogruppe() {
    }

    public BackupTemplateKontogruppe(String bezeichnung, String kontonummer) {
        this.bezeichnung = bezeichnung;
        this.kontonummer = kontonummer;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public void setKontonummer(String kontonummer) {
        this.kontonummer = kontonummer;
    }

    public Set<BackupTemplateKonto> getBackupTemplateKontos() {
        return backupTemplateKontos;
    }

    public void setBackupTemplateKontos(Set<BackupTemplateKonto> backupTemplateKontos) {
        this.backupTemplateKontos = backupTemplateKontos;
    }
}
