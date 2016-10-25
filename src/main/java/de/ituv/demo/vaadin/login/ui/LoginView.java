package de.ituv.demo.vaadin.login.ui;

import org.jdal.annotation.SerializableProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.security.VaadinSecurity;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringView(name = LoginView.NAME, ui = LoginUI.class)
public class LoginView extends VerticalLayout implements View {

    private static final long serialVersionUID = -4430276235082912377L;

    public static final String NAME = "";

    @Autowired
    @SerializableProxy
    private VaadinSecurity security;

    private TextField       username;
    private PasswordField   password;


    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

    }



    public void enter(ViewChangeEvent event) {
        Authentication auth = security.getAuthentication();
        if(auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            username.setValue(auth.getName());
        }

        username.setValue("NeverForgetMe");
        username.focus();

    }

    private Component buildLoginForm() {
        final Panel panel = new Panel();
        panel.setSizeUndefined();

        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeFull();
        loginPanel.setSpacing(true);
        loginPanel.setMargin(true);
        Responsive.makeResponsive(loginPanel);

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());

        panel.setContent(loginPanel);
        return panel;
    }

    private Component buildFields() {
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        username = new TextField("Benutzer");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password = new PasswordField("Passwort");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button();
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.setCaption("Anmelden");
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(final ClickEvent event) {


                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username.getValue(), "");


                try {
                    security.login(auth);
                    getUI().close();
                } catch(AuthenticationException e) {
                    e.printStackTrace();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Willkommen");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("mogena");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

}
