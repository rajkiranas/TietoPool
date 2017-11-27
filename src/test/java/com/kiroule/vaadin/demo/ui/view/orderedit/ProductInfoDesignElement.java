package com.kiroule.vaadin.demo.ui.view.orderedit;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.GridLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextAreaElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.kiroule.vaadin.demo.ui.view.orderedit.ProductInfoDesign")
@AutoGenerated
public class ProductInfoDesignElement extends GridLayoutElement {

	public ComboBoxElement getProduct() {
		return $(com.vaadin.testbench.elements.ComboBoxElement.class).id("product");
	}

	public TextFieldElement getQuantity() {
		return $(com.vaadin.testbench.elements.TextFieldElement.class).id("quantity");
	}

	public LabelElement getPrice() {
		return $(com.vaadin.testbench.elements.LabelElement.class).id("price");
	}

	public TextAreaElement getComment() {
		return $(com.vaadin.testbench.elements.TextAreaElement.class).id("comment");
	}

	public LabelElement getReportModeComment() {
		return $(com.vaadin.testbench.elements.LabelElement.class).id("comment");
	}

	public ButtonElement getDelete() {
		return $(com.vaadin.testbench.elements.ButtonElement.class).id("delete");
	}
}