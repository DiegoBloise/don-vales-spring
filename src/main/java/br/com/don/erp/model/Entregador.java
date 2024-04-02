package br.com.don.erp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@DiscriminatorValue("ENTREGADOR")
public class Entregador extends Colaborador {

    @OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Entrega> entregas = new ArrayList<>();

	@Column(name = "qtd_total_dias")
	private Integer qtdTotalDias;

	@Column(name = "qtd_entregas")
	private Integer qtdEntregas;

	@Column(name = "valor_total_entregas")
	private BigDecimal valorTotalEntregas;

	@Column(name = "valor_total_ifood")
	private BigDecimal valorTotalIfood;

	@Column(name = "valor_total_sem_desconto")
	private BigDecimal valorTotalSemDesconto;

	@Column(name = "valor_total_com_desconto")
	private BigDecimal valorTotalComDesconto;

	@Column(name = "valor_total_diarias")
	private BigDecimal valorTotalDiarias;

	@Column(name = "valor_total_vales")
	private BigDecimal valorTotalVales;

	@Column(name = "valor_saldo")
	private BigDecimal valorSaldo;


	public Entregador() {

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