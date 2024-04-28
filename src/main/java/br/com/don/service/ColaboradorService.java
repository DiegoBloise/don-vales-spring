package br.com.don.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dto.ColaboradorDto;
import br.com.don.model.Colaborador;
import br.com.don.repository.ColaboradorRepository;

@Service
public class ColaboradorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ColaboradorRepository repository;

	@Autowired
	private ModelMapper modelMapper;


	public List<ColaboradorDto> listar(){
		return repository.findAll().stream()
            .map(colaborador -> {
				return modelMapper.map(colaborador, ColaboradorDto.class);
            })
            .collect(Collectors.toList());
	}


	public List<Colaborador> listarPorNome(){
		return repository.listarPorNome();
	}


	public Colaborador getById(Long id) {
		Optional<Colaborador> colaboradorOptional = repository.findById(id);

        if(colaboradorOptional.isEmpty()) {
            return null;
        }

        return colaboradorOptional.get();
	}


	public ColaboradorDto getDtoById(Long id) {
		Optional<Colaborador> colaboradorOptional = repository.findById(id);

        if(colaboradorOptional.isEmpty()) {
            return null;
        }

        return modelMapper.map(colaboradorOptional.get(), ColaboradorDto.class);
	}


	public List<Colaborador> buscarPorNome(String nome){
		return repository.findAllByNome(nome);
	}


	public Colaborador salvarColaborador(Colaborador colaborador) {
		return repository.save(colaborador);
	}


	public ColaboradorDto salvarColaborador(ColaboradorDto colaboradorDto) {
		Colaborador colaborador;

        if (colaboradorDto.getId() != null) {
            colaborador = this.getById(colaboradorDto.getId());
        } else {
            colaborador = new Colaborador();
        }

        if (colaborador == null) {
            return null;
        }

		modelMapper.map(colaboradorDto, colaborador);

		this.salvarColaborador(colaborador);

		modelMapper.map(colaborador, colaboradorDto);

		return colaboradorDto;
	}


	public void deletarColaborador(Colaborador colaborador) {
		repository.delete(colaborador);
	}


	public void deletarColaboradorPorId(Long id) {
		repository.deleteById(id);
	}
}
