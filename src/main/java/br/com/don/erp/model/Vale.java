package br.com.don.erp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.don.erp.enums.TipoVale;
import br.com.don.erp.util.Util;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vales")
public class Vale implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private BigDecimal valor;

	private LocalDate data;

	@Enumerated(EnumType.STRING)
	private TipoVale tipo;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "colaborador_id")
    private Colaborador colaborador;


	public Vale() {
		this.valor = new BigDecimal(0);
	}


	@Transient
	public String getDataFormatada() {
		return Util.localDateFormatado(data);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vale other = (Vale) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Vale [id=" + id + ", valor=" + valor + ", data=" + data + ", tipo=" + tipo + "]";
	}
}
