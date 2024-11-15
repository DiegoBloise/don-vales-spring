package br.com.don.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dto.FreelancerDto;
import br.com.don.model.Freelancer;
import br.com.don.repository.FreelancerRepository;

@Service
public class FreelancerService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FreelancerRepository repository;

	@Autowired
	private ModelMapper modelMapper;


	public List<FreelancerDto> listar(){
		return repository.findAll().stream()
            .map(freelancer -> {
                return modelMapper.map(freelancer, FreelancerDto.class);
            })
            .collect(Collectors.toList());
	}


	public Freelancer getById(Long id) {
		Optional<Freelancer> freelancerOptional = repository.findById(id);

        if(freelancerOptional.isEmpty()) {
            return null;
        }

        return freelancerOptional.get();
	}


	public FreelancerDto getDtoById(Long id) {
		Optional<Freelancer> freelancerOptional = repository.findById(id);

        if(freelancerOptional.isEmpty()) {
            return null;
        }

        return modelMapper.map(freelancerOptional.get(), FreelancerDto.class);
	}


	public Freelancer salvarFreelancer(Freelancer freelancer) {
		return repository.save(freelancer);
	}


	public FreelancerDto salvarFreelancer(FreelancerDto freelancerDto) {
		Freelancer freelancer = new Freelancer();

        if (freelancerDto.getId() != null) {
            freelancer = this.getById(freelancerDto.getId());
        }

		modelMapper.map(freelancerDto, freelancer.getClass());

		freelancer = this.salvarFreelancer(freelancer);

		modelMapper.map(freelancer, freelancerDto.getClass());

		return freelancerDto;
	}


	public void deletarFreelancer(Freelancer freelancer) {
		repository.delete(freelancer);
	}


	public void deletarFreelancerPorId(Long id) {
		repository.deleteById(id);
	}
}
