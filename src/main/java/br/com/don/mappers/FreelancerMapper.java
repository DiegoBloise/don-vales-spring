package br.com.don.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.FreelancerDto;
import br.com.don.models.Freelancer;

@Service
public class FreelancerMapper {

    @Autowired
    private PixMapper pixMapper;

    public FreelancerDto toDto(Freelancer freelancer) {
        return new FreelancerDto(
            freelancer.getNome(),
            freelancer.getTelefone(),
            freelancer.getDataNascimento(),
            freelancer.getTipo(),
            pixMapper.toDto(freelancer.getPix()));
    }


    public Freelancer toFreelancer(FreelancerDto freelancerDto) {
        return new Freelancer(
            freelancerDto.nome(),
            freelancerDto.telefone(),
            freelancerDto.dataNascimento(),
            freelancerDto.tipo(),
            pixMapper.toPix(freelancerDto.pix())
        );
    }
}
