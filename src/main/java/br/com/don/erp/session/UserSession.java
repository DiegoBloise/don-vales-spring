package br.com.don.erp.session;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import br.com.don.erp.enums.TipoUsuario;
import br.com.don.erp.model.Usuario;
import br.com.don.erp.repository.UsuarioRepository;
import br.com.don.erp.util.CookieHelper;
import lombok.Data;

@Data
@Named
@SessionScoped
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username = "Admin";

    private TipoUsuario tipoUsuario = TipoUsuario.ADMIN;


    @Inject
    private CookieHelper cookieHelper;

    @Inject
    private UsuarioRepository repository;



    @PostConstruct
    void init() {

    }



    public void doLogin(String username, String password) throws IOException {

        Boolean usuarioOk = false;
        Boolean senhaOk = false;

        Usuario usuario = repository.findByUsername(username);

        if (usuario != null) {
            usuarioOk = true;
            senhaOk = BCrypt.checkpw(password, usuario.getPassword());
        }

        if (usuarioOk && senhaOk) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            HttpSession session = (HttpSession) externalContext.getSession(true);
            session.setAttribute("username", username);

            this.username = username;

            cookieHelper.setCookie("username", username, 30 * 60);

            externalContext.redirect(externalContext.getRequestContextPath() + "/app/index.xhtml");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Login inválido:", "Verifique seu nome de usuário e senha."));
        }
    }


    public void doLogout() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        cookieHelper.setCookie("username", username, 0);

        externalContext.invalidateSession();

        externalContext.redirect(externalContext.getRequestContextPath() + "/auth/login.xhtml");
    }


    @Override
    public String toString() {
        return "UserSession [username=" + username + ", tipoUsuario=" + tipoUsuario + "]";
    }
}
