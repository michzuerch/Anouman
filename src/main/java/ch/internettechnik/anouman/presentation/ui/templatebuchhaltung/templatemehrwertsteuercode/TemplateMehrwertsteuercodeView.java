package ch.internettechnik.anouman.presentation.ui.templatebuchhaltung.templatemehrwertsteuercode;

import ch.internettechnik.anouman.backend.entity.TemplateMehrwertsteuercode;
import ch.internettechnik.anouman.backend.session.deltaspike.jpa.facade.TemplateMehrwertsteuercodeDeltaspikeFacade;
import ch.internettechnik.anouman.presentation.ui.Menu;
import com.vaadin.cdi.CDIView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.crudui.crud.Crud;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.form.factory.VerticalCrudFormFactory;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collection;

@CDIView("TemplateMehrwertsteuercodeView")
public class TemplateMehrwertsteuercodeView extends VerticalLayout implements View, CrudListener<TemplateMehrwertsteuercode> {
    private static Logger logger = LoggerFactory.getLogger(TemplateMehrwertsteuercodeView.class.getName());

    @Inject
    TemplateMehrwertsteuercodeDeltaspikeFacade templateMehrwertsteuercodeDeltaspikeFacade;

    GridCrud<TemplateMehrwertsteuercode> crud;
    CssLayout filterToolbar = new CssLayout();
    TextField filterTextBezeichnung = new TextField();


    private Collection<TemplateMehrwertsteuercode> getItems() {
        if (!filterTextBezeichnung.isEmpty()) {
            //Suche mit Bezeichnung
            logger.debug("Suche mit Bezeichnung:" + filterTextBezeichnung.getValue());
            return templateMehrwertsteuercodeDeltaspikeFacade
                    .findByBezeichnungLikeIgnoreCase(filterTextBezeichnung.getValue() + "%");
        }
        return templateMehrwertsteuercodeDeltaspikeFacade.findAll();

    }

    private Crud createCrud() {
        crud = new GridCrud<TemplateMehrwertsteuercode>(TemplateMehrwertsteuercode.class, new WindowBasedCrudLayout());
        crud.setCrudListener(this);

        VerticalCrudFormFactory<TemplateMehrwertsteuercode> formFactory = new VerticalCrudFormFactory<>(TemplateMehrwertsteuercode.class);

        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.READ, "id", "bezeichnung");
        formFactory.setVisibleProperties(CrudOperation.ADD, "id", "bezeichnung");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "id", "bezeichnung");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "id", "bezeichnung");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("id", "bezeichnung");

        crud.getGrid().addColumn(templateMehrwertsteuercode -> templateMehrwertsteuercode.getTemplateBuchhaltung().getBezeichnung(), new ButtonRenderer(event -> {
            TemplateMehrwertsteuercode templateMehrwertsteuercode = (TemplateMehrwertsteuercode) event.getItem();
            UI.getCurrent().getNavigator().navigateTo("TemplateBuchhaltungView/id/" + templateMehrwertsteuercode.getTemplateBuchhaltung().getId().toString());
        })).setCaption("Template Buchhaltung").setStyleGenerator(item -> "v-align-center");

        //formFactory.setFieldType("anzahl", AnzahlField.class);
        //formFactory.setFieldType("stueckpreis", BetragField.class);
        formFactory.setButtonCaption(CrudOperation.ADD, "Neuen Template Mehrwertsteuercode erstellen");
        formFactory.setButtonCaption(CrudOperation.DELETE, "Template Mehrwertsteuercode löschen");

        crud.setRowCountCaption("%d Template Mehrwertsteuercodes gefunden");

        crud.getCrudLayout().addToolbarComponent(filterToolbar);
        crud.setClickRowToUpdate(false);
        crud.setUpdateOperationVisible(true);
        crud.setDeleteOperationVisible(true);

        return crud;
    }

    @PostConstruct
    void init() {
        filterTextBezeichnung.setPlaceholder("Filter für Bezeichnung");
        filterTextBezeichnung.addValueChangeListener(e -> crud.getGrid().setItems(getItems()));
        filterTextBezeichnung.setValueChangeMode(ValueChangeMode.LAZY);

        Button clearFilterTextBtn = new Button(VaadinIcons.RECYCLE);
        clearFilterTextBtn.setDescription("Entferne Filter");
        clearFilterTextBtn.addClickListener(e -> {
            filterTextBezeichnung.clear();
        });

        filterToolbar.addComponents(filterTextBezeichnung, clearFilterTextBtn);
        filterToolbar.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        addComponents(new Menu());
        addComponentsAndExpand(createCrud());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            String[] msgs = event.getParameters().split("/");
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
                crud.getGrid().select(templateMehrwertsteuercodeDeltaspikeFacade.findBy(id));
            }
        }
    }

    @Override
    public Collection<TemplateMehrwertsteuercode> findAll() {
        return getItems();
    }

    @Override
    public TemplateMehrwertsteuercode add(TemplateMehrwertsteuercode templateMehrwertsteuercode) {
        return templateMehrwertsteuercodeDeltaspikeFacade.save(templateMehrwertsteuercode);
    }

    @Override
    public TemplateMehrwertsteuercode update(TemplateMehrwertsteuercode templateMehrwertsteuercode) {
        return templateMehrwertsteuercodeDeltaspikeFacade.save(templateMehrwertsteuercode);
    }

    @Override
    public void delete(TemplateMehrwertsteuercode templateMehrwertsteuercode) {
        templateMehrwertsteuercodeDeltaspikeFacade.delete(templateMehrwertsteuercode);
    }
}