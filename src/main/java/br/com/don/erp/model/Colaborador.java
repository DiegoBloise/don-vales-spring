package br.com.don.erp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.don.erp.enums.TipoColaborador;
import br.com.don.erp.util.Util;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "colaboradores")
public class Colaborador implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String nome;

	private String telefone;

	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pix_id")
    private Pix pix;

	@OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vale> vales = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_colaborador")
	private TipoColaborador tipo;


///////////////////////////////////////////////////////////


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


	public Colaborador() {
		this.pix = new Pix(this);
	}


	public void calcularAsParadas(List<Entrega> entregas) {

	}


	@Transient
	public String getDataNascimentoFormatada() {
		return Util.localDateFormatado(this.dataNascimento);
	}


	public String getTelefone() {
        if (this.telefone == null) {
            return this.telefone;
        } else {
            return MessageFormat.format("({0}) {1}-{2}",
                this.telefone.substring(0, 2),
                this.telefone.substring(2, 7),
                this.telefone.substring(7));
        }
    }


	public void setTelefone(String telefone) {
        this.telefone = telefone.replaceAll("[^0-9]", "");
    }


	public String gerarPayloadPix() {
		return this.pix.getPayload(this.nome, this.valorTotalComDesconto.toString(), "Sao Paulo", "PizzaDon");
	}


	public void adicionarVale(Vale vale) {
        this.vales.add(vale);
        vale.setColaborador(this);
    }


    public void adicionarVale() {
        Vale vale = new Vale();
        this.adicionarVale(vale);
    }


    public void removerVale(Vale vale) {
        this.vales.remove(vale);
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((pix == null) ? 0 : pix.hashCode());
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
		Colaborador other = (Colaborador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;
		if (pix == null) {
			if (other.pix != null)
				return false;
		} else if (!pix.equals(other.pix))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Colaborador [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", dataNascimento="
				+ dataNascimento + ", pix=" + pix + "]";
	}
}