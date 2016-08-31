package de.ituv.demo.vaadin.ui;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView(name="first")
public class FirstView extends VerticalLayout implements View {
	
	
	private Logger log = LoggerFactory.getLogger(getClass());

	private Label lbl;

	private static final long serialVersionUID = 5877893830645888032L;

	public FirstView() {
		super();
		setSizeFull();
		lbl = new Label("<h1>First View</h1>", ContentMode.HTML);
		
		
		Button button = new Button("click me first");
		button.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
//				Notification.show("Hello "+SecurityContextHolder.getContext().getAuthentication().getName()+"!");
				UI.getCurrent().getNavigator().navigateTo("secondview");
				
			}
		});
		
		
		MenuBar mBar = new MenuBar();
		mBar.addItem("MenuItem 1", createCommand());
		
		
		
		HorizontalLayout layout = new HorizontalLayout(button);
		addComponent(lbl);
		addComponent(layout);
		addComponent(mBar);
	}




	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	private Command createCommand(){
		Command command = new Command() {
			private static final long serialVersionUID = 99919440775327514L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				UI.getCurrent().access(new Runnable() {
					
					@Override
					public void run() {
				    	HashMap<String, String> parameters = new HashMap<String, String>();
				    	parameters.put("processName", "Name");
				    	parameters.put("processCaption", "Bezeichnung");
				    	ExternalResource extRessource = createParameterizedUIClassURL(SecondUI.class, parameters);
				    	
						UI.getCurrent().getPage().open(extRessource.getURL(), "Bezeichnung");
					}
				});
			}

			
		};
		return command;
		
	}
	
	
	public ExternalResource createParameterizedUIClassURL(Class<? extends UI> uiClass, Map<String, String> parameter) {
		URI uri = UI.getCurrent().getPage().getLocation();
		String bwourl = "popup/" + uiClass.getSimpleName();
		String appctx = uri.getScheme()+"://"+uri.getAuthority()+uri.getPath();
		if(!appctx.endsWith("/")) appctx += "/";
    	String alturl = appctx+bwourl;
    	String url = "";
    	
    	
    	String urlArgs = (parameter == null || parameter.isEmpty() ? "" : "?");
    	Iterator<Entry<String, String>> it = parameter.entrySet().iterator();
    	while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			String value;
			try {
				value = URLEncoder.encode(entry.getValue(), "UTF-8");
				urlArgs += entry.getKey()+"="+value;
				if(it.hasNext())urlArgs += "&";
			} catch (UnsupportedEncodingException e) {
				log.error("Cannot encode URL-Parameter "+entry.getKey()+"="+entry.getValue(), e);
			}
		}
    	
    	url = alturl+urlArgs;
		
		BrowserWindowOpenerUIProvider uiProvider = new BrowserWindowOpenerUIProvider(uiClass, bwourl);
		ExternalResource extRessource = new ExternalResource(url);
		
		UI ui = UI.getCurrent();
		
		if (uiProvider != null
		        && !ui.getSession().getUIProviders().contains(uiProvider)) {
		    ui.getSession().addUIProvider(uiProvider);
		}
		return extRessource;
	}
	
	private static class BrowserWindowOpenerUIProvider extends UIProvider {

		private static final long serialVersionUID = -5409437988913223609L;
		private final String path;
        private final Class<? extends UI> uiClass;

        public BrowserWindowOpenerUIProvider(Class<? extends UI> uiClass,
                String path) {
            this.path = ensureInitialSlash(path);
            this.uiClass = uiClass;
        }

        private static String ensureInitialSlash(String path) {
            if (path == null) {
                return null;
            } else if (!path.startsWith("/")) {
                return '/' + path;
            } else {
                return path;
            }
        }

        @Override
        public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
            String requestPathInfo = event.getRequest().getPathInfo();
            if (path.equals(requestPathInfo)) {
                return uiClass;
            } else {
                return null;
            }
        }
    }
	
}
