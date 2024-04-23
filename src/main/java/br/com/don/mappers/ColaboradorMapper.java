package br.com.don.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.ColaboradorDto;
import br.com.don.models.Colaborador;

@Service
public class ColaboradorMapper {

    @Autowired
    private PixMapper pixMapper;

    public ColaboradorDto toDto(Colaborador colaborador) {
        return new ColaboradorDto(
            colaborador.getNome(),
            colaborador.getTelefone(),
            colaborador.getDataNascimento(),
            colaborador.getTipo(),
            pixMapper.toDto(colaborador.getPix()));
    }


    public Colaborador toColaborador(ColaboradorDto colaboradorDto) {
        return new Colaborador(
            colaboradorDto.nome(),
            colaboradorDto.telefone(),
            colaboradorDto.dataNascimento(),
            colaboradorDto.tipo(),
            pixMapper.toPix(colaboradorDto.pix())
        );
    }
}
