package br.com.don.erp.view;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;

import br.com.don.erp.model.Vale;
import br.com.don.erp.service.ValeService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class HomeView implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Vale> vales;

	private List<Vale> filteredVales;

	private List<FilterMeta> filterBy;

	@Inject
	private ValeService valeService;

    @PostConstruct
	public void init() {
		vales = valeService.listar();

		filterBy = new ArrayList<>();

		//.filterValue(new ArrayList<>(Arrays.asList(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7))))
        filterBy.add(FilterMeta.builder()
                .field("data")
                .filterValue(new ArrayList<>(Arrays.asList(LocalDate.now(), LocalDate.now())))
                .matchMode(MatchMode.BETWEEN)
                .build());
	}
}