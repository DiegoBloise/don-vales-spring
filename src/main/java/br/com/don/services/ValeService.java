package br.com.don.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.ValeDto;
import br.com.don.enums.TipoVale;
import br.com.don.mappers.ValeMapper;
import br.com.don.models.Colaborador;
import br.com.don.models.Entregador;
import br.com.don.models.Vale;
import br.com.don.repositories.ValeRepository;

@Service
public class ValeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ValeRepository repository;

	@Autowired
	private ValeMapper mapper;

	@Autowired
	private ColaboradorService colaboradorService;

	/* public List<Vale> findVales() {
        String jpql = "SELECT NEW br.com.don.model.Vale(v.id, v.nome) FROM Vale v";
        return findDTOs(jpql, Cliente.class);
    } */


	public List<ValeDto> listar(){
		return repository.findAll().stream()
            .map(vale -> {
                return mapper.toDto(vale);
            })
            .collect(Collectors.toList());
	}


	public List<Vale> listarOrdenadoPorData() {
		return repository.listarOrdenadoPorData();
	}


	public List<Vale> listarOrdenadoPorId() {
		return repository.listarOrdenadoPorId();
	}


	public ValeDto salvarVale(ValeDto valeDto) {
		Colaborador colaborador = colaboradorService.getById(valeDto.colaboradorId());

		if (colaborador == null) {
			return null;
		}

		Vale vale = mapper.toVale(valeDto);

		colaborador.adicionarVale(vale);

		colaboradorService.salvarColaborador(colaborador);

		return mapper.toDto(vale);
	}



	public List<Vale> buscarPorColaborador(Colaborador colaborador){
		return repository.findAllByProperty("colaborador", colaborador);
	}


	public List<Vale> buscarPorColaboradorDataTipo(Colaborador colaborador, LocalDate data,TipoVale tipo){
		return repository.buscarPorColaboradorDataTipo(colaborador, data, tipo);
	}


	public List<Vale> buscarSaldo(Entregador entregador, LocalDate data){
		return repository.buscarSaldo(entregador, data );
	}


	public List<Vale> buscarPorData(LocalDate data){
		return repository.buscarPorData(data);
	}


	public List<Vale> buscarPorColaboradorDataInicioFim(Colaborador colaborador,LocalDate dataInicio,LocalDate dataFim){
		return repository.buscarPorColaboradorDataInicioFim(colaborador, dataInicio, dataFim);
	}


	public ValeDto buscarPorId(Long id) {
		Optional<Vale> valeOptional = repository.findById(id);

        if(valeOptional.isEmpty()) {
            return null;
        }

        return mapper.toDto(valeOptional.get());
	}


	public void deletarVale(Vale vale) {
		repository.delete(vale);
	}


	public void deletarValePorId(Long id) {
		repository.deleteById(id);
	}


	public void prepararValeImpressao(Vale vale) {
		StringBuilder conteudo = new StringBuilder()
		.append(vale.getColaborador().getNome())
		.append(System.lineSeparator())
		.append(vale.getValor())
		.append(System.lineSeparator())
		.append(vale.getDataFormatada());

		@SuppressWarnings("unused")
		InputStream inputStream = new ByteArrayInputStream(conteudo.toString().getBytes());
	}
}
