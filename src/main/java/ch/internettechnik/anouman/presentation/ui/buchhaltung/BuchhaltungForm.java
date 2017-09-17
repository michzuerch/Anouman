package ch.internettechnik.anouman.presentation.ui.buchhaltung;

import ch.internettechnik.anouman.backend.entity.Buchhaltung;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.ui.*;
import org.jboss.logging.Logger;
import org.vaadin.viritin.fields.IntegerField;
import org.vaadin.viritin.form.AbstractForm;

/**
 * Created by michzuerch on 09.08.15.
 */
@ViewScoped
public class BuchhaltungForm extends AbstractForm<Buchhaltung> {
    private static final Logger LOGGER = Logger.getLogger(BuchhaltungForm.class);

    TextField bezeichnung = new TextField("Bezeichnung");
    IntegerField jahr = new IntegerField("Jahr");


    public BuchhaltungForm() {
        super(Buchhaltung.class);
    }

    @Override
    public Window openInModalPopup() {
        final Window openInModalPopup = super.openInModalPopup();
        openInModalPopup.setWidth("400px");
        return openInModalPopup;
    }

    @Override
    protected Component createContent() {


        return new VerticalLayout(new FormLayout(
                bezeichnung, jahr
        ), getToolbar());
    }


}