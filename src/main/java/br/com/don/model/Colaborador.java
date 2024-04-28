package br.com.don.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.don.enums.TipoColaborador;
import br.com.don.util.Util;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@ToString(callSuper = true)
@Data
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "colaboradores")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_colaborador")
@DiscriminatorValue("FIXO")
public class Colaborador extends BaseEntity implements Serializable {

	protected static final long serialVersionUID = 1L;

	protected String nome;

	protected String telefone;

	@Column(name = "data_nascimento")
	protected LocalDate dataNascimento;

	@OneToOne(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	protected Pix pix;

	@OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
    protected List<Vale> vales;

    protected BigDecimal totalVales;

    @Column(name = "tipo_colaborador", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    protected TipoColaborador tipo;


	public Colaborador() {
        this.pix = new Pix(this);
        this.totalVales = new BigDecimal(0);
		this.vales = new ArrayList<>();
	}


	public Colaborador(Long id, String nome, String telefone, LocalDate dataNascimento, TipoColaborador tipo, Pix pix) {
        this.vales = new ArrayList<>();
        this.setId(id);
        this.setNome(nome);
		this.setTelefone(telefone);
		this.setDataNascimento(dataNascimento);
        this.setTipo(tipo);
        this.setPix(pix);
    }


    public void setPix(Pix pix) {
        this.pix = pix;
        this.pix.setColaborador(this);
    }


    @Transient
	@JsonIgnore
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


	public String getTelefoneWhatsApp() {
        if (this.telefone == null) {
            return this.telefone;
        } else {
            return "+55" + this.telefone;
        }
    }


	public void setTelefone(String telefone) {
        this.telefone = telefone.replaceAll("[^0-9]", "");
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Colaborador other = (Colaborador) obj;
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
        if (totalVales == null) {
            if (other.totalVales != null)
                return false;
        } else if (!totalVales.equals(other.totalVales))
            return false;
        if (tipo != other.tipo)
            return false;
        return true;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
        result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
        result = prime * result + ((totalVales == null) ? 0 : totalVales.hashCode());
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
        return result;
    }
}