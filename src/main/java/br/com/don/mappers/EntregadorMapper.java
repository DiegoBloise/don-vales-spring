package br.com.don.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.EntregadorDto;
import br.com.don.models.Entregador;

@Service
public class EntregadorMapper {

    @Autowired
    private PixMapper pixMapper;

    public EntregadorDto toDto(Entregador entregador) {
        return new EntregadorDto(
            entregador.getId(),
            entregador.getNome(),
            entregador.getTelefone(),
            entregador.getDataNascimento(),
            entregador.getTipo(),
            pixMapper.toDto(entregador.getPix()));
    }


    public Entregador toEntregador(EntregadorDto entregadorDto) {
        return new Entregador(
            entregadorDto.id(),
            entregadorDto.nome(),
            entregadorDto.telefone(),
            entregadorDto.dataNascimento(),
            entregadorDto.tipo(),
            pixMapper.toPix(entregadorDto.pix())
        );
    }
}
