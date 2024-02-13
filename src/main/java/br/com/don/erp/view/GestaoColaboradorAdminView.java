package br.com.don.erp.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.don.erp.model.Colaborador;
import br.com.don.erp.service.ColaboradorService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class GestaoColaboradorAdminView implements Serializable {

    private static final long serialVersionUID = 1L;

    private Colaborador colaborador;
    private List<Colaborador> colaboradores;

    private Colaborador colaboradorSelecionado;
    private List<Colaborador> colaboradoresSelecionados;

    @Inject
    private ColaboradorService colaboradorService;


    @PostConstruct
    public void init() {
        inicializarObjetos();
    }


    public void inicializarObjetos() {
        colaboradores = colaboradorService.getColaboradores();
        colaboradorSelecionado = new Colaborador();
        colaboradoresSelecionados = new ArrayList<>();
    }


    public void adicionar() {
        colaboradorSelecionado = new Colaborador();
    }


    public void salvar() {
        if (colaboradorSelecionado.getId() == null) {
            colaboradores.add(colaboradorSelecionado);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Cadastrado", "Colaborador: " + colaboradorSelecionado.getNome());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Atualizado", "Colaborador: " + colaboradorSelecionado.getNome());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        PrimeFaces.current().executeScript("PF('ColaboradorDialog').hide()");
        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        colaboradorService.cadastrarColaborador(colaboradorSelecionado);

        inicializarObjetos();
    }


    public void deletarSelecionado() {
        colaboradores.remove(colaboradorSelecionado);

        colaboradoresSelecionados.remove(colaboradorSelecionado);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Removido", "Colaborador: " + colaboradorSelecionado.getNome());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        colaboradorService.removerColaborador(colaboradorSelecionado);

        colaboradorSelecionado = null;
    }


    public String getDeleteButtonMessage() {
        if (hasColaboradoresSelecionados()) {
            int size = colaboradoresSelecionados.size();
            return size > 1 ? size + " colaboradores selecionados" : "1 colaborador selecionado";
        }

        return "Remover";
    }


    public boolean hasColaboradoresSelecionados() {
        return colaboradoresSelecionados != null && !colaboradoresSelecionados.isEmpty();
    }


    public void deletarColaboradoresSelecionados() {
        colaboradores.removeAll(colaboradoresSelecionados);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaboradores Removidos", "Todas os colaboradores selecionados foram removidos.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");
        PrimeFaces.current().executeScript("PF('dtColaboradores').clearFilters()");

        colaboradorService.removerColaboradores(colaboradoresSelecionados);

        colaboradoresSelecionados.clear();
    }
}