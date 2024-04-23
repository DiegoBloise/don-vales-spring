package br.com.don.models;

import java.time.LocalDate;

import br.com.don.enums.TipoColaborador;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
@DiscriminatorValue("FREE")
public class Freelancer extends Colaborador {


    public Freelancer(String nome, String telefone, LocalDate dataNascimento, TipoColaborador tipo, Pix pix) {
        this.setNome(nome);
        this.setTelefone(telefone);
        this.setDataNascimento(dataNascimento);
        this.setTipo(tipo);
        this.setPix(pix);
    }

}