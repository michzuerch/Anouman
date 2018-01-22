package ch.internettechnik.anouman.presentation.ui;

import ch.internettechnik.anouman.backend.entity.Adresse;
import ch.internettechnik.anouman.backend.session.deltaspike.jpa.facade.AdresseDeltaspikeFacade;
import ch.internettechnik.anouman.presentation.ui.field.BetragField;
import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.crudui.crud.Crud;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.EditableGridCrud;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.form.factory.VerticalCrudFormFactory;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collection;

@CDIView("GridCrudTestView")
public class GridCrudTestView extends VerticalLayout implements View, CrudListener<Adresse> {
    private static Logger logger = LoggerFactory.getLogger(GridCrudTestView.class.getName());

    @Inject
    Menu menu;

    @Inject
    AdresseDeltaspikeFacade facade;

    private TabSheet tabSheet = new TabSheet();

    @PostConstruct
    void init() {
        logger.debug("GridCrudTest init");
        addComponents(menu);
        addComponentsAndExpand(tabSheet);
        //addCrud(getDefaultCrud(), "Default");
        //addCrud(getDefaultCrudWithFixes(), "Default (with fixes)");
        addCrud(getCrud(), "Configured");
        //addCrud(getEditableGridCrud(), "Editable Grid");
    }


    private void addCrud(Crud crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private Crud getCrud() {
        GridCrud<Adresse> crud = new GridCrud<Adresse>(Adresse.class, new WindowBasedCrudLayout());
        crud.setCrudListener(this);

        //GridLayoutCrudFormFactory<Adresse> formFactory = new GridLayoutCrudFormFactory<>(Adresse.class, 2, 2);

        VerticalCrudFormFactory<Adresse> formFactory = new VerticalCrudFormFactory<>(Adresse.class);

        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.READ, "id", "firma", "anrede", "vorname", "nachname", "strasse", "postleitzahl",
                "ort", "stundensatz");
        formFactory.setVisibleProperties(CrudOperation.ADD, "firma", "anrede", "vorname", "nachname", "strasse", "postleitzahl",
                "ort", "stundensatz");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "id", "firma", "anrede", "vorname", "nachname", "strasse", "postleitzahl",
                "ort", "stundensatz");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "firma");

        formFactory.setDisabledProperties("id");
        formFactory.setDisabledProperties("anzahlRechnungen");

        crud.getGrid().setColumns("id", "firma", "anrede", "vorname", "nachname", "strasse", "postleitzahl",
                "ort", "stundensatz", "anzahlRechnungen");

        crud.getGrid().addColumn(adresse -> adresse.getAnzahlRechnungen(), new ButtonRenderer(event -> {
            Adresse adresse = (Adresse) event.getItem();
            if (adresse.getAnzahlRechnungen() > 0) {
                UI.getCurrent().getNavigator().navigateTo("RechnungOldView/adresseId/" + adresse.getId().toString());
            }
        })).setCaption("Anzahl Rechnungen").setStyleGenerator(item -> "v-align-center");


        //crud.getGrid().getColumn("mainGroup").setRenderer(group -> group == null ? "" : ((Group) group).getName(), new TextRenderer());
        //((Grid.Column<User, Date>) crud.getGrid().getColumn("birthDate")).setRenderer(new DateRenderer("%1$tY-%1$tm-%1$te"));

        //formFactory.setFieldType("password", PasswordField.class);
        //formFactory.setFieldCreationListener("birthDate", field -> ((DateField) field).setDateFormat("yyyy-MM-dd"));

        //formFactory.setFieldProvider("groups", new CheckBoxGroupProvider<>("Groups", GroupRepository.findAll(), Group::getName));
        //formFactory.setFieldProvider("mainGroup", new ComboBoxProvider<>("Main Group", GroupRepository.findAll(), Group::getName));

        formFactory.setFieldType("stundensatz", BetragField.class);
        formFactory.setButtonCaption(CrudOperation.ADD, "Neue Adresse erstellen");
        formFactory.setButtonCaption(CrudOperation.DELETE, "Adresse löschen");

        crud.setRowCountCaption("%d Adressen gefunden");

        crud.setClickRowToUpdate(false);
        crud.setUpdateOperationVisible(true);
        crud.setDeleteOperationVisible(true);

        return crud;
    }

    private Crud getEditableGridCrud() {
        EditableGridCrud<Adresse> crud = new EditableGridCrud<>(Adresse.class, this);

        crud.getGrid().setColumns("id", "firma", "anrede", "vorname", "nachname", "strasse", "postleitzahl",
                "ort", "stundensatz");
        crud.getCrudFormFactory().setVisibleProperties("id", "firma", "anrede", "vorname", "nachname", "strasse", "postleitzahl",
                "ort", "stundensatz");

        //crud.getGrid().getColumn("password").setRenderer(user -> "********", new TextRenderer());
        //crud.getGrid().getColumn("mainGroup").setRenderer(group -> group == null ? "" : ((Group) group).getName(), new TextRenderer());
        //crud.getGrid().getColumn("groups").setRenderer(groups -> StringUtils.join(((Set<Group>) groups).stream().map(g -> g.getName()).collect(Collectors.toList()), ", "), new TextRenderer());

        //crud.getCrudFormFactory().setFieldType("password", PasswordField.class);
        //crud.getCrudFormFactory().setFieldProvider("groups", new CheckBoxGroupProvider<>(null, GroupRepository.findAll(), group -> group.getName()));
        //crud.getCrudFormFactory().setFieldProvider("mainGroup", new ComboBoxProvider<>(null, GroupRepository.findAll(), group -> group.getName()));

        crud.getCrudFormFactory().setUseBeanValidation(true);

        return crud;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        logger.debug("GridCrudTest enter");
    }

    @Override
    public Collection<Adresse> findAll() {
        return facade.findAll();
    }

    @Override
    public Adresse add(Adresse adresse) {
        return facade.save(adresse);
    }

    @Override
    public Adresse update(Adresse adresse) {
        return facade.save(adresse);
    }

    @Override
    public void delete(Adresse adresse) {
        facade.delete(adresse);
    }
}