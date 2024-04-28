package br.com.don.model;

import java.time.LocalDate;

import br.com.don.enums.TipoColaborador;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@Entity
@DiscriminatorValue("FREELANCER")
public class Freelancer extends Colaborador {


    public Freelancer() {

    }


    public Freelancer(Long id, String nome, String telefone, LocalDate dataNascimento, TipoColaborador tipo, Pix pix) {
        this.setId(id);
        this.setNome(nome);
        this.setTelefone(telefone);
        this.setDataNascimento(dataNascimento);
        this.setTipo(tipo);
        this.setPix(pix);
    }

}