package br.com.don.services;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.FreelancerDto;
import br.com.don.mappers.FreelancerMapper;
import br.com.don.models.Freelancer;
import br.com.don.repositories.FreelancerRepository;

@Service
public class FreelancerService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FreelancerRepository repository;

	@Autowired
	private FreelancerMapper mapper;


	public List<FreelancerDto> listar(){
		return repository.findAll().stream()
            .map(freelancer -> {
                return mapper.toDto(freelancer);
            })
            .collect(Collectors.toList());
	}


	public Freelancer buscar(Freelancer freelancer){
		Optional<Freelancer> freelancerOptional = repository.findById(freelancer.getId());
		return freelancerOptional.isPresent() ? freelancerOptional.get() : null;
	}


	public FreelancerDto buscarPorId(Long id) {
		Optional<Freelancer> freelancerOptional = repository.findById(id);

        if(freelancerOptional.isEmpty()) {
            return null;
        }

        return mapper.toDto(freelancerOptional.get());
	}


	public FreelancerDto salvarFreelancer(FreelancerDto freelancerDto) {
		return mapper.toDto(
			repository.save(
				mapper.toFreelancer(freelancerDto)
			)
		);
	}


	public void deletarFreelancer(Freelancer freelancer) {
		repository.delete(freelancer);
	}


	public void deletarFreelancerPorId(Long id) {
		repository.deleteById(id);
	}


	public void removerFreelanceres(List<Freelancer> freelanceres) {
		for (Freelancer freelancer : freelanceres) {
			this.deletarFreelancer(freelancer);
		}
	}
}
