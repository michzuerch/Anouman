package ch.internettechnik.anouman.presentation.ui.rechnungsposition;

import ch.internettechnik.anouman.backend.entity.Rechnung;
import ch.internettechnik.anouman.backend.entity.Rechnungsposition;
import ch.internettechnik.anouman.backend.session.deltaspike.jpa.facade.RechnungDeltaspikeFacade;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.ui.NumberField;
import org.vaadin.viritin.form.AbstractForm;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Created by michzuerch on 09.08.15.
 */
public class RechnungspositionForm extends AbstractForm<Rechnungsposition> {
    private static Logger logger = LoggerFactory.getLogger(RechnungspositionForm.class.getName());

    NativeSelect<Rechnung> rechnung = new NativeSelect<>();
    TextField bezeichnung = new TextField("Bezeichnung");
    TextArea bezeichnunglang = new TextArea("Bezeichnung Lang");
    TextField mengeneinheit = new TextField("Mengeneinheit");
    NumberField stueckpreis = new NumberField("Stückpreis");
    NumberField anzahl = new NumberField("Anzahl");

    @Inject
    RechnungDeltaspikeFacade rechnungDeltaspikeFacade;

    public RechnungspositionForm() {
        super(Rechnungsposition.class);

    }

    public void lockSelect() {
        rechnung.setEnabled(false);
    }

    @Override
    public Window openInModalPopup() {
        final Window openInModalPopup = super.openInModalPopup();
        openInModalPopup.setCaption("Rechnungsposition");
        openInModalPopup.setWidth("400px");
        return openInModalPopup;
    }

    @Override
    protected Component createContent() {

        rechnung.setCaption("Rechnung");
        rechnung.setEmptySelectionAllowed(false);
        rechnung.setItems(rechnungDeltaspikeFacade.findAll());
        rechnung.setItemCaptionGenerator(rechnung1 -> rechnung1.getBezeichnung());

        stueckpreis.setLocale(Locale.GERMAN);
        //numberField.setCaption("Modified settings:");
        stueckpreis.setDecimalPrecision(2);
        stueckpreis.setDecimalSeparator('.');
        stueckpreis.setGroupingSeparator('\'');
        stueckpreis.setDecimalSeparatorAlwaysShown(true);
        stueckpreis.setMinimumFractionDigits(2);
        stueckpreis.setMinValue(5);

        anzahl.setLocale(getUI().getLocale());
        //numberField.setCaption("Modified settings:");
        anzahl.setDecimalPrecision(2);
        anzahl.setDecimalSeparator('.');
        anzahl.setGroupingSeparator('\'');
        anzahl.setDecimalSeparatorAlwaysShown(true);
        anzahl.setMinimumFractionDigits(2);
        anzahl.setMinValue(5);

        getBinder().forField(stueckpreis).withConverter(
                NumberField.getConverter("Muss Betrag sein")
        ).bind("stueckpreis");

        getBinder().forField(anzahl).withConverter(
                NumberField.getConverter("Muss Anzahl sein")
        ).bind("anzahl");

        return new VerticalLayout(new FormLayout(
                rechnung, bezeichnung, bezeichnunglang, mengeneinheit, stueckpreis, anzahl
        ), getToolbar());
    }


}
