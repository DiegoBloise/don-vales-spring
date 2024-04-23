package br.com.don.mappers;

import java.util.List;
import java.util.stream.Collectors;

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


    public List<ValeDto> toDtos(List<Vale> vales) {
        return vales.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
    }


    public List<Vale> toVales(List<ValeDto> valesDtos) {
        return valesDtos.stream()
                        .map(this::toVale)
                        .collect(Collectors.toList());
    }
}
