package br.com.don.erp.view;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class AuthView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String authKey = "321654";

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private Boolean isAdmin;


    @PostConstruct
    public void init() {
        authKey = authKey + LocalDate.now().getDayOfMonth();

        isAdmin = false;

        password = "";
    }


    public void setUser() throws IOException {

        if (password.equals(authKey) && !isAdmin) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Autorizado Com Sucesso", ""));
            isAdmin = true;
        } else if (isAdmin) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"LogOff Realizado com Sucesso", ""));
            isAdmin = false;

            /* FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/index.xhtml"); */
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Credenciais Inválidas", "This action will be reported to the admin."));
            isAdmin = false;
        }

        password = "";
    }
}