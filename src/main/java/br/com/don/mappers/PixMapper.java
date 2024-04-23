package br.com.don.mappers;

import org.springframework.stereotype.Service;

import br.com.don.dtos.PixDto;
import br.com.don.models.Pix;

@Service
public class PixMapper {


    public PixDto toDto(Pix pix) {
        return new PixDto(
            pix.getId(),
            pix.getTipo(),
            pix.getChave()
        );
    }


    public Pix toPix(PixDto pixDto) {
        Pix pix = new Pix();
        pix.setId(pixDto.id());
        pix.setTipo(pixDto.tipo());
        pix.setChave(pixDto.chave());
        return pix;
    }
}
