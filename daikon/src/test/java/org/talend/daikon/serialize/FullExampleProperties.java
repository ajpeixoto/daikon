// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize;

import static org.talend.daikon.properties.presentation.Widget.BUTTON_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.DATALIST_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.FILE_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.HIDDEN_TEXT_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.NAME_SELECTION_AREA_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.NAME_SELECTION_REFERENCE_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.RADIO_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.SCHEMA_EDITOR_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.SCHEMA_REFERENCE_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.SELECT_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.TABLE_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.TEXT_AREA_WIDGET_TYPE;
import static org.talend.daikon.properties.presentation.Widget.widget;
import static org.talend.daikon.properties.property.PropertyFactory.newBoolean;
import static org.talend.daikon.properties.property.PropertyFactory.newDate;
import static org.talend.daikon.properties.property.PropertyFactory.newEnum;
import static org.talend.daikon.properties.property.PropertyFactory.newEnumList;
import static org.talend.daikon.properties.property.PropertyFactory.newInteger;
import static org.talend.daikon.properties.property.PropertyFactory.newProperty;
import static org.talend.daikon.properties.property.PropertyFactory.newSchema;
import static org.talend.daikon.properties.property.PropertyFactory.newString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.commons.lang3.reflect.TypeLiteral;
import org.talend.daikon.NamedThing;
import org.talend.daikon.SimpleNamedThing;
import org.talend.daikon.properties.PresentationItem;
import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.ReferenceProperties;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.ValidationResult.Result;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.EnumListProperty;
import org.talend.daikon.properties.property.EnumProperty;
import org.talend.daikon.properties.property.Property;

/**
 * TODO This is a duplicate one by org.talend.components.fullexample.FullExampleProperties, but for test json schema
 * serialize, can be moved or removed if there is a better way
 */
public class FullExampleProperties extends PropertiesImpl {

    private static final long serialVersionUID = 8049049878260901939L;

    /**
     * table property to use with table widget.
     */
    private static final String POPUP_FORM_NAME = "popup";

    /** use the default widget for this String type */
    public Property<String> stringProp = newString("stringProp", "initialValue");

    /** this shall hide stringProp widget according to it value. */
    public Property<Boolean> hideStringPropProp = newBoolean("hideStringPropProp", false);

    /**
     * property to check the {@link WidgetType#NAME_SELECTION_AREA} and {@link WidgetType#NAME_SELECTION_REFERENCE}
     * widgets.
     */
    public Property<String> multipleSelectionProp = newProperty("multipleSelectionProp");

    /**
     * property to check the {@link Widget#PLACEHOLDER_WIDGET_CONF}.
     */
    public Property<String> stringWithPlaceholder = newProperty("stringWithPlaceholder");

    /** checking {@link WidgetType#BUTTON} */
    public PresentationItem showNewForm = new PresentationItem("showNewForm");

    /** checking {@link WidgetType#TABLE} */
    public TableProperties tableProp = new TableProperties("tableProp");

    // TODO some Component Reference widget use case.
    /** reuse common properties */
    public CommonProperties commonProp = new CommonProperties("commonProp");

    /** checking {@link WidgetType#FILE} */
    public Property<String> filepathProp = newString("filepathProp");

    /** checking {@link WidgetType#HIDDEN_TEXT} */
    public Property<String> hiddenTextProp = newString("hiddenTextProp");

    /** use the default widget for this Integer type */
    public Property<Integer> integerProp = newInteger("integerProp").setRequired();

    public Property<Long> longProp = newProperty(Long.class, "longProp");

    /** use the default widget for this Date type */
    public Property<Date> dateProp = newDate("dateProp").setRequired();

    /** checking {@link WidgetType#TEXT_AREA} */
    public Property<String> textareaProp = newString("textareaProp");

    /** checking {@link WidgetType#RADIO} */
    public Property<String> radioProp = newString("radioProp");

    /** checking {@link WidgetType#SELECT} */
    public Property<String> selectProp = newString("selectProp");

    /** checking {@link WidgetType#DATALIST} */
    public Property<String> datalistProp = newString("datalistProp");

    /**
     * uses 2 widgets, {@link WidgetType#SCHEMA_EDITOR} in the Main form and {@link WidgetType#SCHEMA_REFERENCE} on the
     * REFERENCE form
     */
    public Property<Schema> schema = newSchema("schema"); //$NON-NLS-1$

    public PresentationItem validateAllCallbackCalled = new PresentationItem("validateAllCallbackCalled");

    public ReferenceProperties<?> referencedComponent = new ReferenceProperties<>("referencedComponent", "def1");

    private List<String> methodCalled = new ArrayList<>();

    public FullExampleProperties(String name) {
        super(name);
    }

    private void thisMethodWasCalled() {
        methodCalled.add(Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        // setup multipleSelectionProp
        ArrayList<NamedThing> multipleSelectableList = new ArrayList<NamedThing>();
        multipleSelectableList.add(new SimpleNamedThing("foo", "fooo"));
        multipleSelectableList.add(new SimpleNamedThing("bar", "barr"));
        multipleSelectableList.add(new SimpleNamedThing("foobar", "foobarr"));
        multipleSelectionProp.setPossibleValues(multipleSelectableList);

        List<String> values4Radio = new ArrayList<>();
        values4Radio.add("option1");
        values4Radio.add("option2");
        values4Radio.add("option3");
        radioProp.setPossibleValues(values4Radio);

        List<String> values4Select = new ArrayList<>();
        values4Select.add("table1");
        values4Select.add("table2");
        values4Select.add("table3");
        selectProp.setPossibleValues(values4Select);

        List<String> values4Datalist = new ArrayList<>();
        values4Datalist.add("data1");
        values4Datalist.add("data2");
        values4Datalist.add("data3");
        datalistProp.setPossibleValues(values4Datalist);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(stringProp);
        mainForm.addRow(widget(schema).setWidgetType(SCHEMA_EDITOR_WIDGET_TYPE));
        mainForm.addRow(widget(schema).setWidgetType(SCHEMA_REFERENCE_WIDGET_TYPE));
        mainForm.addRow(widget(multipleSelectionProp).setWidgetType(NAME_SELECTION_AREA_WIDGET_TYPE));
        mainForm.addRow(widget(multipleSelectionProp).setWidgetType(NAME_SELECTION_REFERENCE_WIDGET_TYPE));
        mainForm.addRow(widget(stringWithPlaceholder).setWidgetType(TEXT_AREA_WIDGET_TYPE)
                .setConfigurationValue(Widget.PLACEHOLDER_WIDGET_CONF, "Place Holder"));
        mainForm.addRow(widget(showNewForm).setWidgetType(BUTTON_WIDGET_TYPE));
        Form popUpForm = new Form(this, POPUP_FORM_NAME);
        showNewForm.setFormtoShow(popUpForm);
        mainForm.addColumn(commonProp);
        mainForm.addColumn(widget(hiddenTextProp).setWidgetType(HIDDEN_TEXT_WIDGET_TYPE));
        mainForm.addColumn(widget(filepathProp).setWidgetType(FILE_WIDGET_TYPE));
        mainForm.addRow(integerProp);
        mainForm.addRow(longProp);
        mainForm.addRow(dateProp);
        mainForm.addRow(widget(tableProp).setWidgetType(TABLE_WIDGET_TYPE));
        mainForm.addRow(widget(radioProp).setWidgetType(RADIO_WIDGET_TYPE));
        mainForm.addRow(widget(selectProp).setWidgetType(SELECT_WIDGET_TYPE));
        mainForm.addRow(widget(datalistProp).setWidgetType(DATALIST_WIDGET_TYPE));

        Form advancedForm = new Form(this, Form.ADVANCED);
        advancedForm.addRow(widget(textareaProp).setWidgetType(TEXT_AREA_WIDGET_TYPE));

        /*
         * Scenario example: when connection properties are using reference connection.
         * In this case MAIN form is added on REFERENCE form. We can't add just
         * connection properties as a row, since in most cases it also has WIZARD form,
         * that we don't need to add in other properties(Input, Output, etc). Actual
         * MAIN form is used in t*Connection component properties.
         *
         */
        Form referenceForm = new Form(this, Form.REFERENCE);
        Widget compListWidget = widget(referencedComponent).setWidgetType(Widget.COMPONENT_REFERENCE_WIDGET_TYPE);
        referenceForm.addRow(compListWidget);
        referenceForm.addRow(mainForm);
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        if (Form.MAIN.equals(form.getName())) {
            form.getWidget(stringProp.getName()).setHidden(hideStringPropProp.getValue());
        } // else do nothing
    }

    // callback for beforeFormPresent
    public void beforeFormPresentPopup() {
        thisMethodWasCalled();
    }

    public void afterHideStringPropProp() {
        thisMethodWasCalled();
        refreshLayout(getForm(Form.MAIN));
    }

    // implements all dynamic callbacks for the string property and it's default widget.
    // stringProp
    public void validateStringProp() {
        thisMethodWasCalled();
    }

    public void beforeStringProp() {
        thisMethodWasCalled();
    }

    public void beforeStringPropPresent() {
        thisMethodWasCalled();
    }

    public void afterStringProp() {
        thisMethodWasCalled();
    }

    // Schema
    public void validateSchema() {
        thisMethodWasCalled();
    }

    public void beforeSchema() {
        thisMethodWasCalled();
    }

    public void beforeSchemaPresent() {
        thisMethodWasCalled();
    }

    public void afterSchema() {
        thisMethodWasCalled();
    }

    // MultipleSelectionProp
    public void validateMultipleSelectionProp() {
        thisMethodWasCalled();
    }

    public void beforeMultipleSelectionProp() {
        thisMethodWasCalled();
    }

    public void beforeMultipleSelectionPropPresent() {
        thisMethodWasCalled();
    }

    public void afterMultipleSelectionProp() {
        thisMethodWasCalled();
    }

    // FilepathProp
    public void validateFilepathProp() {
        thisMethodWasCalled();
    }

    public void beforeFilepathProp() {
        thisMethodWasCalled();
    }

    public void beforeFilepathPropPresent() {
        thisMethodWasCalled();
    }

    public void afterFilepathProp() {
        thisMethodWasCalled();
    }

    // HiddenTextProp
    public void validateHiddenTextProp() {
        thisMethodWasCalled();
    }

    public void beforeHiddenTextProp() {
        thisMethodWasCalled();
    }

    public void beforeHiddenTextPropPresent() {
        thisMethodWasCalled();
    }

    public void afterHiddenTextProp() {
        thisMethodWasCalled();
    }

    // ShowNewForm
    public void validateShowNewForm() {
        thisMethodWasCalled();
    }

    public void beforeShowNewForm() {
        thisMethodWasCalled();
    }

    public void beforeShowNewFormPresent() {
        thisMethodWasCalled();
    }

    public void afterShowNewForm() {
        thisMethodWasCalled();
    }

    public ValidationResult validateValidateAllCallbackCalled() {
        if (methodCalled.size() == 25) {
            return ValidationResult.OK;
        } else {
            return new ValidationResult(Result.ERROR, "some method where not called :" + methodCalled);
        }
    }

    public static class CommonProperties extends PropertiesImpl {

        private static final long serialVersionUID = 8094271685971123111L;

        public Property<String> colString = newString("colString");

        public EnumProperty<ColEnum> colEnum = newEnum("colEnum", ColEnum.class);

        public Property<Boolean> colBoolean = newBoolean("colBoolean");

        public CommonProperties(String name) {
            super(name);
        }

        @Override
        public void setupLayout() {
            Form mainForm = new Form(this, Form.MAIN);
            mainForm.addRow(colString);
            mainForm.addRow(colEnum);
            mainForm.addRow(colBoolean);
        }

        public enum ColEnum {
            FOO,
            BAR
        }
    }

    public static class TableProperties extends PropertiesImpl {

        private static final long serialVersionUID = -2223674382898148884L;

        public static final TypeLiteral<List<ColEnum>> LIST_ENUM_TYPE = new TypeLiteral<List<ColEnum>>() {// empty
        };

        private static final TypeLiteral<List<String>> LIST_STRING_TYPE = new TypeLiteral<List<String>>() {// empty
        };

        private static final TypeLiteral<List<Boolean>> LIST_BOOLEAN_TYPE = new TypeLiteral<List<Boolean>>() {// empty
        };

        public Property<List<String>> colListString = newProperty(LIST_STRING_TYPE, "colListString");

        public EnumListProperty<ColEnum> colListEnum = newEnumList("colListEnum", LIST_ENUM_TYPE);

        public Property<List<Boolean>> colListBoolean = newProperty(LIST_BOOLEAN_TYPE, "colListBoolean");

        public TableProperties(String name) {
            super(name);
        }

        @Override
        public void setupLayout() {
            super.setupLayout();
            Form mainForm = new Form(this, Form.MAIN);
            mainForm.addColumn(colListString);
            mainForm.addColumn(colListEnum);
            mainForm.addColumn(colListBoolean);
        }

        public enum ColEnum {
            FOO,
            BAR
        }
    }

}
