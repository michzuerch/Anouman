package ch.internettechnik.anouman.presentation.ui.rechnung;

import ch.internettechnik.anouman.backend.entity.Aufwand;
import ch.internettechnik.anouman.backend.entity.Rechnung;
import ch.internettechnik.anouman.backend.entity.Rechnungsposition;
import ch.internettechnik.anouman.backend.session.jpa.api.AufwandService;
import ch.internettechnik.anouman.backend.session.jpa.api.RechnungService;
import ch.internettechnik.anouman.backend.session.jpa.api.RechnungspositionService;
import ch.internettechnik.anouman.presentation.ui.Menu;
import ch.internettechnik.anouman.presentation.ui.aufwand.AufwandForm;
import ch.internettechnik.anouman.presentation.ui.rechnungsposition.RechnungspositionForm;
import com.vaadin.cdi.CDIView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

import javax.inject.Inject;

@CDIView("RechnungDetail")
public class RechnungDetailView extends VerticalLayout implements View {
    @Inject
    RechnungspositionForm rechnungspositionForm;
    @Inject
    AufwandForm aufwandForm;
    @Inject
    RechnungService rechnungService;
    @Inject
    RechnungspositionService rechnungspositionService;
    @Inject
    AufwandService aufwandService;
    Long idRechnung = new Long(0);
    TextField fieldId = new TextField("id");
    TextField fieldAdresseFirma = new TextField("Adresse Firma");
    TextField fieldAdresseNachname = new TextField("Adresse Nachname");
    TextField fieldAdresseOrt = new TextField("Adresse Ort");
    TextField fieldBezeichnung = new TextField("Bezeichnung");
    TextField fieldRechnungsdatum = new TextField("Rechnungsdatum");
    TextField fieldFaelligInTagen = new TextField("Fällig in Tagen");
    TextField fieldFaelligkeitsdatum = new TextField("Fälligkeitsdatum");
    TextField fieldMehrwertsteuer = new TextField("Mehrwertsteuer");
    TextField fieldZwischentotal = new TextField("Zwischentotal");
    TextField fieldRechnungstotal = new TextField("Rechnungstotal");
    Grid<Rechnungsposition> rechnungspositionGrid = new Grid<>();
    Grid<Aufwand> aufwandGrid = new Grid<>();
    Button btnAddRechnungsposition = new Button("Add Rechnungsposition");
    Button btnAddAufwand = new Button("Add Aufwand");
    Button btnBack = new Button("Zurück", clickEvent -> getUI().getNavigator().navigateTo("Rechnung/id/" + getIdRechnung()));
    @Inject
    private Menu menu;

    private void update() {
        Rechnung val = rechnungService.findById(getIdRechnung());
        fieldId.setValue(val.getId().toString());
        fieldAdresseFirma.setValue(val.getAdresse().getFirma());
        fieldAdresseNachname.setValue(val.getAdresse().getNachname());
        fieldAdresseOrt.setValue(val.getAdresse().getOrt());
        fieldBezeichnung.setValue(val.getBezeichnung());
        fieldRechnungsdatum.setValue(val.getRechnungsdatum().toLocaleString());
        fieldFaelligInTagen.setValue(new Integer(val.getFaelligInTagen()).toString());
        fieldFaelligkeitsdatum.setValue(val.getFaelligkeitsdatum().toLocaleString());
        fieldMehrwertsteuer.setValue(val.getMehrwertsteuer().toString());
        fieldZwischentotal.setValue(val.getZwischentotal().toString());
        fieldRechnungstotal.setValue(val.getRechnungstotal().toString());

        rechnungspositionGrid.setItems(rechnungspositionService.findByCriteria(val));
        aufwandGrid.setItems(aufwandService.findByCriteria(val));

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        if (viewChangeEvent.getParameters() != null) {
            String[] msgs = viewChangeEvent.getParameters().split("/");
            String target = new String();
            Long id = new Long(0);
            for (String msg : msgs) {
                if (target.isEmpty()) {
                    target = msg;
                } else {
                    id = Long.valueOf(msg);
                }
            }
            if (target.equals("id")) {
                setIdRechnung(id);
            }
        }

        fieldId.setEnabled(false);
        fieldAdresseFirma.setEnabled(false);
        fieldAdresseNachname.setEnabled(false);
        fieldAdresseOrt.setEnabled(false);
        fieldBezeichnung.setEnabled(false);
        fieldRechnungsdatum.setEnabled(false);
        fieldFaelligInTagen.setEnabled(false);
        fieldFaelligkeitsdatum.setEnabled(false);
        fieldMehrwertsteuer.setEnabled(false);
        fieldZwischentotal.setEnabled(false);
        fieldRechnungstotal.setEnabled(false);

        rechnungspositionGrid.setCaption("Rechnungspositionen");
        aufwandGrid.setCaption("Aufwand");

        rechnungspositionGrid.addColumn(Rechnungsposition::getBezeichnung).setCaption("Bezeichnung");
        rechnungspositionGrid.addColumn(Rechnungsposition::getAnzahl).setCaption("Anzahl");
        rechnungspositionGrid.addColumn(Rechnungsposition::getMengeneinheit).setCaption("Einheit");
        rechnungspositionGrid.addColumn(Rechnungsposition::getStueckpreis).setCaption("Preis");
        rechnungspositionGrid.addColumn(Rechnungsposition::getPositionstotal).setCaption("Total");
        rechnungspositionGrid.addColumn(rechnungsposition -> "löschen",
                new ButtonRenderer(event -> {
                    Notification.show("Lösche Rechnungsposition id:" + event.getItem(), Notification.Type.HUMANIZED_MESSAGE);
                    rechnungspositionService.delete((Rechnungsposition) event.getItem());
                    Rechnung val = rechnungService.findById(getIdRechnung());
                    val.getRechnungspositionen().remove(event.getItem());
                    rechnungService.saveOrPersist(val);
                    update();
                })
        );

        aufwandGrid.addColumn(Aufwand::getBezeichnung).setCaption("Bezeichnung");
        aufwandGrid.addColumn(Aufwand::getDauerInStunden).setCaption("Stunden");
        aufwandGrid.addColumn(Aufwand::getStart).setCaption("Start");
        aufwandGrid.addColumn(Aufwand::getPositionstotal).setCaption("Total");
        aufwandGrid.addColumn(aufwand -> "löschen",
                new ButtonRenderer(event -> {
                    Notification.show("Lösche Aufwand id:" + event.getItem(), Notification.Type.HUMANIZED_MESSAGE);
                    aufwandService.delete((Aufwand) event.getItem());
                    Rechnung val = rechnungService.findById(getIdRechnung());
                    val.getAufwands().remove(event.getItem());
                    update();
                })
        );

        btnAddAufwand.setIcon(VaadinIcons.ASTERISK);
        btnAddAufwand.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnAddRechnungsposition.setIcon(VaadinIcons.ASTERISK);
        btnAddRechnungsposition.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnBack.setIcon(VaadinIcons.BACKWARDS);
        btnBack.setStyleName(ValoTheme.BUTTON_ICON_ONLY);

        btnAddRechnungsposition.addClickListener(event -> {
            rechnungspositionGrid.asSingleSelect().clear();
            Rechnungsposition val = new Rechnungsposition();
            val.setStueckpreis(0d);
            val.setMengeneinheit("Stück");
            val.setAnzahl(0d);
            val.setRechnung(rechnungService.findById(getIdRechnung()));
            rechnungspositionForm.lockSelect();
            rechnungspositionForm.setEntity(val);
            rechnungspositionForm.openInModalPopup();
            rechnungspositionForm.setSavedHandler(rechnungsposition -> {
                rechnungspositionService.saveOrPersist(rechnungsposition);
                Rechnung rechnung = rechnungService.findById(getIdRechnung());
                rechnung.getRechnungspositionen().add(val);
                rechnungService.saveOrPersist(rechnung);
                rechnungspositionForm.closePopup();
                update();
            });
        });

        btnAddAufwand.addClickListener(event -> {
            aufwandGrid.asSingleSelect().clear();
            Aufwand val = new Aufwand();
            val.setRechnung(rechnungService.findById(getIdRechnung()));
            aufwandForm.lockSelect();
            aufwandForm.setEntity(val);
            aufwandForm.openInModalPopup();
            aufwandForm.setSavedHandler(aufwand -> {
                aufwandService.saveOrPersist(aufwand);
                Rechnung rechnung = rechnungService.findById(getIdRechnung());
                rechnung.getAufwands().add(aufwand);
                rechnungService.saveOrPersist(rechnung);
                aufwandForm.closePopup();
                update();
            });
        });

        addComponent(menu);
        addComponent(btnBack);
        addComponents(new HorizontalLayout(
                new FormLayout(fieldId, fieldBezeichnung, fieldRechnungsdatum),
                new FormLayout(fieldAdresseFirma, fieldAdresseNachname, fieldAdresseOrt),
                new FormLayout(fieldFaelligkeitsdatum, fieldFaelligInTagen),
                new FormLayout(fieldZwischentotal, fieldMehrwertsteuer, fieldRechnungstotal)
        ));

        HorizontalLayout gridsLayout = new HorizontalLayout();
        HorizontalLayout rechnungspositionLayout = new HorizontalLayout();
        HorizontalLayout aufwandLayout = new HorizontalLayout();

        rechnungspositionLayout.addComponent(btnAddRechnungsposition);
        rechnungspositionLayout.addComponentsAndExpand(rechnungspositionGrid);

        aufwandLayout.addComponent(btnAddAufwand);
        aufwandLayout.addComponentsAndExpand(aufwandGrid);

        gridsLayout.addComponentsAndExpand(rechnungspositionLayout, aufwandLayout);
        addComponentsAndExpand(gridsLayout);
        update();
    }

    public Long getIdRechnung() {
        return idRechnung;
    }

    public void setIdRechnung(Long idRechnung) {
        this.idRechnung = idRechnung;
    }
}