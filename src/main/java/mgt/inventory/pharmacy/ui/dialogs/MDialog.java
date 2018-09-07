package mgt.inventory.pharmacy.ui.dialogs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.server.VaadinSession;

public class MDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	private final Span header;
	private final FlexMe headerflex = new FlexMe();
	private final Div contentdiv = new Div();
	private final Div footerflex = new Div();

	public MDialog(DialogAction actiontype) {

		header = new Span();
		header.getStyle().set("fontWeight", "600").set("fontSize", "var(--lumo-font-size-l)");
		headerflex.add(header);

		footerflex.getStyle().set("flexGrow", "1");

		FlexLayout footdiv = new FlexLayout(footerflex);
		footdiv.getStyle().set("marginTop", "0.5em");
		footdiv.setJustifyContentMode(JustifyContentMode.END);
		footdiv.setAlignItems(Alignment.CENTER);

		if (actiontype != DialogAction.VIEW) {
			Button cancel = new Button("Cancel");
			cancel.getElement().setAttribute("theme", "small");
			cancel.getStyle().set("minWidth", "6em").set("minWidth", "6em");
			cancel.addClickListener(e -> closeme());
			footdiv.add(cancel);
		}

		add(headerflex, contentdiv, footdiv);
	}

	public void closeme() {
		close();
	}

	protected void setHeader(String text) {
		header.setText(text);
	}

	public void setIcon(Component icon) {
		headerflex.add(icon);
	}

	protected void setHeader(Component component) {
		header.add(component);
	}

	protected void setHeaderBText(String text) {
		Span hd = new Span(text);
		hd.getStyle().set("fontSize", "var(--lumo-font-size-s)").set("color", "var(--lumo-tertiary-text-color)");
		headerflex.add(hd);
	}

	protected void setHeaderB(Component c) {
		headerflex.add(c);
	}

	protected void setContents(Component... components) {
		contentdiv.add(components);
	}

	protected void addActions(Component... components) {
		for (Component component : components) {
			Div wrapr = new Div(component);
			wrapr.addClassNames("mr-2", "dialog-left");
			footerflex.add(wrapr);
		}
	}

	protected void addTerminalActions(Button... components) {
		for (Button component : components) {
			component.addClassNames("ml-2", "dialog-right");
			footerflex.add(component);
		}
	}

	public enum DialogAction {
		NEW, EDIT, DELETE, VIEW
	}

	protected void addFooterComponent(Component component) {
		footerflex.add(component);
	}

	public static String getAdminUser() {
		try {
			return VaadinSession.getCurrent().getAttribute("username").toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static class FlexMe extends FlexLayout {

		private static final long serialVersionUID = 1L;

		public FlexMe() {
			setJustifyContentMode(JustifyContentMode.BETWEEN);
			setAlignItems(Alignment.CENTER);
		}

		public FlexMe(Component... c) {
			setJustifyContentMode(JustifyContentMode.BETWEEN);
			setAlignItems(Alignment.CENTER);
			add(c);
		}

	}

}
