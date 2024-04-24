package br.com.don.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.don.enums.TipoColaborador;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("ENTREGADOR")
public class Entregador extends Colaborador {

    @OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
	private List<Entrega> entregas;

	@Transient
	@JsonIgnore
	private Integer qtdTotalDias;

	@Transient
	@JsonIgnore
	private Integer qtdEntregas;

	@Transient
	@JsonIgnore
	private Double valorTotalEntregas;

	@Transient
	@JsonIgnore
	private Double valorTotalIfood;

	@Transient
	@JsonIgnore
	private Double valorTotalSemDesconto;

	@Transient
	@JsonIgnore
	private Double valorTotalComDesconto;

	@Transient
	@JsonIgnore
	private Double valorTotalDiarias;

	@Transient
	@JsonIgnore
	private Double valorTotalVales;

	@Transient
	@JsonIgnore
	private Double valorSaldo;


	public Entregador() {
		this.entregas = new ArrayList<>();
	}


	public Entregador(Long id, String nome, String telefone, LocalDate dataNascimento, TipoColaborador tipo, Pix pix) {
        this.entregas = new ArrayList<>();
		this.setId(id);
		this.setNome(nome);
		this.setTelefone(telefone);
		this.setDataNascimento(dataNascimento);
        this.setTipo(tipo);
        this.setPix(pix);
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