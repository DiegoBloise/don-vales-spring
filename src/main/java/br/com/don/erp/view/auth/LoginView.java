package br.com.don.erp.view.auth;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.don.erp.session.UserSession;
import lombok.Data;

@Data
@Named
@ViewScoped
public class LoginView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    @Inject
    private UserSession userSession;


    public void doLogin() throws IOException {
        userSession.doLogin(username, password);
    }
}