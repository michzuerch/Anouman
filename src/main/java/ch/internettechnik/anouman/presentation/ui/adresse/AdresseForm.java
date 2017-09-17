package ch.internettechnik.anouman.presentation.ui.adresse;

import ch.internettechnik.anouman.backend.entity.Adresse;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.ui.*;
import org.jboss.logging.Logger;
import org.vaadin.viritin.form.AbstractForm;

import java.util.Locale;

//import org.vaadin.ui.NumberField;

/**
 * Created by michzuerch on 09.08.15.
 */
@ViewScoped
public class AdresseForm extends AbstractForm<Adresse> {
    private static final Logger LOGGER = Logger.getLogger(AdresseForm.class);

    TextField firma = new TextField("Firma");
    TextField anrede = new TextField("Anrede");
    TextField vorname = new TextField("Vorname");
    TextField nachname = new TextField("Nachname");
    TextField strasse = new TextField("Strasse");
    TextField postleitzahl = new TextField("Postleitzahl");
    TextField ort = new TextField("Ort");
    //NumberField stundensatz = new NumberField("Stundensatz");
    TextField stundensatz = new TextField("Stundensatz");


    public AdresseForm() {
        super(Adresse.class);
    }

    @Override
    public Window openInModalPopup() {
        final Window openInModalPopup = super.openInModalPopup();
        openInModalPopup.setCaption("Adresse");
        openInModalPopup.setWidth("400px");
        return openInModalPopup;
    }

    @Override
    protected Component createContent() {
        stundensatz.setLocale(Locale.GERMAN);
        stundensatz.setCaption("Stundensatz");

        /*
        stundensatz.setDecimalPrecision(2);
        stundensatz.setDecimalSeparator(',');
        stundensatz.setGroupingSeparator('.');
        stundensatz.setDecimalSeparatorAlwaysShown(true);
        stundensatz.setMinimumFractionDigits(2);
        stundensatz.setMinValue(5);
        */

        getBinder().forField(stundensatz)
                .withConverter(new StringToDoubleConverter("Muss Betrag sein"))
                //.withConverter(NumberField.getConverter("Muss Betrag sein"))
                .bind("stundensatz");

        return new VerticalLayout(new FormLayout(
                firma, anrede, vorname, nachname, strasse, postleitzahl, ort, stundensatz
        ), getToolbar());
    }
}