package de.ituv.demo.vaadin.login.ui;

import java.util.List;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

@SpringUI(path = "/login")
@Push(value = PushMode.DISABLED, transport = Transport.LONG_POLLING)
@Title("mogena Desktop - PM ")
@PreserveOnRefresh
public class LoginUI extends UI {

    private static final long serialVersionUID = 5310014981075920878L;

    @Autowired
    private List<SpringViewProvider> vProviders;
    
    
	@Override
    protected void init(VaadinRequest request) {
        Navigator navigator = new Navigator(this, this);
       
        for (ViewProvider provider : vProviders) {
        	try {
	        	if(provider.getView(LoginView.NAME) != null)
	        		navigator.addProvider(provider);
        	}catch(NoSuchBeanDefinitionException e) {
        		e.printStackTrace();
        	}
		}
        setNavigator(navigator);
    }

	@Override
	public void close() {
		super.close();
	}

	
	
}
