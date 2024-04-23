package br.com.don.services;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.ColaboradorDto;
import br.com.don.mappers.ColaboradorMapper;
import br.com.don.models.Colaborador;
import br.com.don.repositories.ColaboradorRepository;

@Service
public class ColaboradorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ColaboradorRepository repository;

	@Autowired
	private ColaboradorMapper mapper;


	public List<ColaboradorDto> listar(){
		return repository.findAll().stream()
            .map(colaborador -> {
                return mapper.toDto(colaborador);
            })
            .collect(Collectors.toList());
	}


	public List<Colaborador> listarPorNome(){
		return repository.listarPorNome();
	}


	public Colaborador buscar(Colaborador colaborador){
		Optional<Colaborador> colaboradorOptional = repository.findById(colaborador.getId());
		return colaboradorOptional.isPresent() ? colaboradorOptional.get() : null;
	}


	public ColaboradorDto buscarPorId(Long id) {
		Optional<Colaborador> colaboradorOptional = repository.findById(id);

        if(colaboradorOptional.isEmpty()) {
            return null;
        }

        return mapper.toDto(colaboradorOptional.get());
	}


	public List<Colaborador> buscarPorNome(String nome){
		return repository.findAllByProperty("nome", nome);
	}


	public Object salvarColaborador(ColaboradorDto colaboradorDto) {
		return mapper.toDto(
			repository.save(
				mapper.toColaborador(colaboradorDto)
			)
		);
	}


	public void deletarColaborador(Colaborador colaborador) {
		repository.delete(colaborador);
	}


	public void deletarColaboradorPorId(Long id) {
		repository.deleteById(id);
	}


	public void deletarColaboradores(List<Colaborador> colaboradores) {
		for (Colaborador colaborador : colaboradores) {
			this.deletarColaborador(colaborador);
		}
	}
}
