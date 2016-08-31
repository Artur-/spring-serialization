package de.ituv.demo.vaadin.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


@UIScope
@SpringView(name="secondview", ui=DemoUI.class)
public class SecondView extends VerticalLayout implements View {

	private static final long serialVersionUID = 5877893830645888032L;
	private Label lbl;

	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public SecondView() {
		super();
		setSizeFull();
		lbl = new Label("<h1>Second View</h1>", ContentMode.HTML);
		
		
		Button button = new Button("click me second");
		button.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
//				Notification.show("Hello "+SecurityContextHolder.getContext().getAuthentication().getName()+"!");
				UI.getCurrent().getNavigator().navigateTo("first");
				
			}
		});
		
		HorizontalLayout layout = new HorizontalLayout(button);
		addComponent(lbl);
		addComponent(layout);
	
	}




	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
