package br.com.don.erp.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.don.erp.session.UserSession;

@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Inject
    private UserSession userSession;


    public void init(FilterConfig fConfig) throws ServletException {

    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

            if (userSession.getUsuario() != null) {

                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;

                HttpSession session = httpRequest.getSession();
                session.setMaxInactiveInterval(30 * 60);

                if (userSession.getUsuario().isAdmin()) {
                    chain.doFilter(request, response);
                } else {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/access.xhtml");
                }
            } else {
                chain.doFilter(request, response);
            }
    }


    public void destroy() {

    }
}
