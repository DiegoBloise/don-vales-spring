package br.com.don.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.ValeDto;
import br.com.don.enums.TipoVale;
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
	private ModelMapper modelMapper;


	public List<ValeDto> listar(){
		return repository.findAll().stream()
            .map(vale -> {
                return modelMapper.map(vale, ValeDto.class);
            })
            .collect(Collectors.toList());
	}


	public List<Vale> listarOrdenadoPorData() {
		return repository.listarOrdenadoPorData();
	}


	public List<Vale> listarOrdenadoPorId() {
		return repository.listarOrdenadoPorId();
	}


	public Vale salvarVale(Vale vale) {
		return repository.save(vale);
	}


	public ValeDto salvarVale(ValeDto valeDto) {
		Vale vale;

        if (valeDto.getId() != null) {
            vale = this.getById(valeDto.getId());
        } else {
            vale = new Vale();
        }

        if (vale == null) {
            return null;
        }

		modelMapper.map(valeDto, vale);

		this.salvarVale(vale);

		modelMapper.map(vale, valeDto);

		return valeDto;
	}



	public List<Vale> buscarPorColaborador(Colaborador colaborador){
		return repository.findAllByProperty("colaborador", colaborador);
	}


	public List<ValeDto> buscarPorColaboradorId(Long id){
		return repository.findAllByColaboradorId(id).stream()
            .map(vale -> {
                return modelMapper.map(vale, ValeDto.class);
            })
            .collect(Collectors.toList());
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


	public Vale getById(Long id) {
		Optional<Vale> valeOptional = repository.findById(id);

        if(valeOptional.isEmpty()) {
            return null;
        }

        return valeOptional.get();
	}


	public ValeDto getDtoById(Long id) {
		Optional<Vale> valeOptional = repository.findById(id);

        if(valeOptional.isEmpty()) {
            return null;
        }

        return modelMapper.map(valeOptional.get(), ValeDto.class);
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
