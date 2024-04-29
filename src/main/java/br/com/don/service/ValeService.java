package br.com.don.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dto.ValeDto;
import br.com.don.enums.TipoVale;
import br.com.don.model.Colaborador;
import br.com.don.model.Entregador;
import br.com.don.model.Vale;
import br.com.don.repository.ValeRepository;

@Service
public class ValeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ValeRepository repository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ColaboradorService colaboradorService;


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
		LocalTime horaAtual = LocalTime.now();
		LocalDate dataAtual = LocalDate.now();

		if (horaAtual.isBefore(LocalTime.of(1, 20))) {
			vale.setData(dataAtual.minusDays(1));
		} else {
			vale.setData(dataAtual.minusDays(1));
		}

		vale = repository.save(vale);

		atualizarValesByColaboradorId(vale.getColaborador().getId());

		return vale;
	}


	public ValeDto salvarVale(ValeDto valeDto) {
		Vale vale = new Vale();

        if (valeDto.getId() != null) {
            vale = this.getById(valeDto.getId());
        }

		modelMapper.map(valeDto, vale.getClass());

		vale = this.salvarVale(vale);

		modelMapper.map(vale, valeDto.getClass());

		return valeDto;
	}



	public List<Vale> buscarPorColaborador(Long id){
		return repository.findAllByColaboradorId(id);
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
		atualizarValesByColaboradorId(vale.getColaborador().getId());
		repository.delete(vale);
	}


	public void deletarVale(ValeDto valeDto) {
		repository.deleteById(valeDto.getId());
		atualizarValesByColaboradorId(valeDto.getColaboradorId());
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


	public BigDecimal totalDoColaborador(Long id) {
		BigDecimal total = new BigDecimal(0);

		List<Vale> vales = this.buscarPorColaborador(id);

		for (Vale vale : vales) {
			total = vale.getValor().add(total);
		}

		return total;
	}


	public void atualizarValesByColaboradorId(Long id) {
		Colaborador colaborador = colaboradorService.getById(id);
		colaborador.setTotalVales(this.totalDoColaborador(colaborador.getId()));
		colaboradorService.salvarColaborador(colaborador);
	}
}
