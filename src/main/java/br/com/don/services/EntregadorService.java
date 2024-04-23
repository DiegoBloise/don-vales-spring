package br.com.don.services;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.EntregadorDto;
import br.com.don.mappers.EntregadorMapper;
import br.com.don.models.Entregador;
import br.com.don.repositories.EntregadorRepository;

@Service
public class EntregadorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EntregadorRepository repository;

	@Autowired
	private EntregadorMapper mapper;


	public List<EntregadorDto> listar(){
		return repository.findAll().stream()
            .map(entregaodor -> {
                return mapper.toDto(entregaodor);
            })
            .collect(Collectors.toList());
	}


	public List<Entregador> listarPorNome() {
		return repository.listarPorNome();
	}


	public Entregador buscar(Entregador entregador){
		return repository.find(entregador.getId());
	}


	public EntregadorDto buscarPorId(Long id) {
		Optional<Entregador> entregadorOptional = repository.findById(id);

        if(entregadorOptional.isEmpty()) {
            return null;
        }

        return mapper.toDto(entregadorOptional.get());
	}


	public Entregador buscarPorNome(String nome){
		return repository.findByProperty("nome", nome);
	}


	public EntregadorDto salvarEntregador(EntregadorDto entregadorDto) {
		return mapper.toDto(
			repository.save(
				mapper.toEntregador(entregadorDto)
			)
		);
	}

	



	public void deletarEntregador(Entregador entregador) {
		repository.delete(entregador);
	}


	public void deletarEntregadorPorId(Long id) {
		repository.deleteById(id);
	}


	public void deletarEntregadores(List<Entregador> entregadores) {
		for (Entregador entregador : entregadores) {
			this.deletarEntregador(entregador);
		}
	}


	public List<Entregador> listarEntregadoresPorData(LocalDate data){
		return repository.listarEntregadoresPorData(data);
	}


	public List<Entregador> listarEntregadoresPorDataInicioFim(LocalDate dataInicio, LocalDate dataFim ){
		return repository.listarEntregadoresPorDataInicioFim(dataInicio,dataFim);
	}


	public Entregador cadastrarEntregador(Entregador entregador) {
		return repository.save(entregador);
	}
}
