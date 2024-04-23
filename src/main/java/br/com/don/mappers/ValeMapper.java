package br.com.don.mappers;

import org.springframework.stereotype.Service;

import br.com.don.dtos.ValeDto;
import br.com.don.models.Vale;

@Service
public class ValeMapper {


    public ValeDto toDto(Vale vale) {
        return new ValeDto(
            vale.getColaborador().getId(),
            vale.getValor(),
            vale.getData());
    }


    public Vale toVale(ValeDto valeDto) {
        return new Vale(
            valeDto.valor(),
            valeDto.data()
        );
    }
}
