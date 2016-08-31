package de.ituv.demo.vaadin.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;


@PreserveOnRefresh
@SuppressWarnings("serial")
@Push(value = PushMode.DISABLED, transport = Transport.LONG_POLLING)
@SpringUI(path = "/ui/demo")
public class DemoUI extends UI {

	
	@Autowired
	private SpringViewProvider viewProvider;
	
	private Navigator navi;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	protected void init(VaadinRequest request) {
		
		SecurityContext s = SecurityContextHolder.getContext();
		System.out.println("Second UI: "+s.getAuthentication());
		navi = new Navigator(this, this);
		setNavigator(navi);
		navi.addProvider(viewProvider);
		
		navi.navigateTo("first");
		
		
		
	}

	
}
