package br.com.don.erp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.don.erp.enums.TipoColaborador;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "entregadores")
public class Entregador extends Colaborador {

    @OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Entrega> entregas = new ArrayList<>();

	@Transient
	@Column(name = "qtd_total_dias")
	private Integer qtdTotalDias;

	@Transient
	@Column(name = "qtd_entregas")
	private Integer qtdEntregas;

	@Transient
	@Column(name = "valor_total_entregas")
	private BigDecimal valorTotalEntregas;

	@Transient
	@Column(name = "valor_total_ifood")
	private BigDecimal valorTotalIfood;

	@Transient
	@Column(name = "valor_total_sem_desconto")
	private BigDecimal valorTotalSemDesconto;

	@Transient
	@Column(name = "valor_total_com_desconto")
	private BigDecimal valorTotalComDesconto;

	@Transient
	@Column(name = "valor_total_diarias")
	private BigDecimal valorTotalDiarias;

	@Transient
	@Column(name = "valor_total_vales")
	private BigDecimal valorTotalVales;

	@Transient
	@Column(name = "valor_saldo")
	private BigDecimal valorSaldo;


	public Entregador() {
		this.tipo = TipoColaborador.ENTREGADOR;
	}


    public void calcularAsParadas(List<Entrega> entregas) {

	}


    public String gerarPayloadPix() {
		return this.pix.getPayload(this.nome, this.valorTotalComDesconto.toString(), "Sao Paulo", "PizzaDon");
	}


    public void adicionarEntrega(Entrega entrega) {
        this.entregas.add(entrega);
        entrega.setEntregador(this);
    }


    public void adicionarEntrega() {
        Entrega entrega = new Entrega();
        this.adicionarEntrega(entrega);
    }


    public void removerEntrega(Entrega entrega) {
        this.entregas.remove(entrega);
    }
}