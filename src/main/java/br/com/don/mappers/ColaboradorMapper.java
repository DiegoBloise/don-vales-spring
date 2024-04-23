package br.com.don.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.ColaboradorDto;
import br.com.don.models.Colaborador;

@Service
public class ColaboradorMapper {

    @Autowired
    private PixMapper pixMapper;

    @Autowired
    private ValeMapper valeMapper;


    public ColaboradorDto toDto(Colaborador colaborador) {
        return new ColaboradorDto(
            colaborador.getId(),
            colaborador.getNome(),
            colaborador.getTelefone(),
            colaborador.getDataNascimento(),
            colaborador.getTipo(),
            pixMapper.toDto(colaborador.getPix()),
            valeMapper.toDtos(colaborador.getVales())
        );
    }


    public Colaborador toColaborador(ColaboradorDto colaboradorDto) {
        return new Colaborador(
            colaboradorDto.id(),
            colaboradorDto.nome(),
            colaboradorDto.telefone(),
            colaboradorDto.dataNascimento(),
            colaboradorDto.tipo(),
            pixMapper.toPix(colaboradorDto.pix()),
            valeMapper.toVales(colaboradorDto.vales())
        );
    }
}
