package br.com.don.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.don.enums.TipoVale;
import br.com.don.util.Util;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "vales")
public class Vale extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal valor;

	private LocalDate data;

	private TipoVale tipo;

	@ManyToOne
    @JoinColumn(name = "colaborador_id")
	@JsonBackReference
    private Colaborador colaborador;


	public Vale() {
		this.valor = new BigDecimal(0);
		this.tipo = TipoVale.DINHEIRO;
		this.setData(LocalDate.now());
	}


	public Vale(Long id, BigDecimal valor, LocalDate data) {
		this.setId(id);
		this.setValor(valor);
		this.setData(data);

		this.tipo = TipoVale.DINHEIRO;
	}


	@Transient
	@JsonIgnore
	public String getDataFormatada() {
		return Util.localDateFormatado(this.data);
	}


	@Transient
	@JsonIgnore
	public String getDataSemanaFormatada() {
		return Util.dataSemanaFormatada(this.data);
	}
}
