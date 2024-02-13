package br.com.don.erp.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

import br.com.don.erp.util.Util;
import lombok.Data;

@Data
public class Acerto {

	private LocalDate data;
	
	private Integer qtdeEntregasDia;
	
	private Integer qtdeIFood;
	
	private BigDecimal valorValeDia;
	
	private BigDecimal valorDiaria;
	
	private final String QUEBRALINHA ="\n";
	
	private final String TRACEJADO = "--------------------------------";
	
	public BigDecimal getValorDiaria() {
		boolean isFimDeSemana = false;
		if(data.getDayOfWeek().equals(DayOfWeek.FRIDAY) ||
			data.getDayOfWeek().equals(DayOfWeek.SATURDAY)||
			data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			isFimDeSemana = true;
		}
		
		if(isFimDeSemana) {
			if(qtdeEntregasDia > 40) {
				this.valorDiaria = new BigDecimal("50.00");
			}else if(qtdeEntregasDia > 30) {
				this.valorDiaria = new BigDecimal("45.00");
			}else if(qtdeEntregasDia > 20) {
				this.valorDiaria = new BigDecimal("40.00");
			}else if(qtdeEntregasDia > 9) {
				this.valorDiaria = new BigDecimal("35.00");
			}else {
				this.valorDiaria = new BigDecimal("0.00");
			}
		}else {
			if(qtdeEntregasDia > 9) {
				this.valorDiaria = new BigDecimal("35.00");
			}else {
				this.valorDiaria = new BigDecimal("0.00");
			}
		}
		
		return this.valorDiaria;
	}
	
	public String getDataFormatada() {
		return Util.localDateFormatado(this.data).concat(System.lineSeparator()).concat(Util.diaDaSemana(data.getDayOfWeek()));
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer()
		.append("*").append(Util.diaDaSemana(data.getDayOfWeek()))
		.append(" ")
		.append(Util.localDateFormatado(this.data)).append("*")
		.append(QUEBRALINHA)
		.append("Qtde de entregas: ").append(this.qtdeEntregasDia)
		.append(QUEBRALINHA)
		.append("Valor da Diária: R$ ").append(getValorDiaria())
		.append(QUEBRALINHA)
		.append("Qtde de IFood: ").append(this.qtdeIFood)
		.append(QUEBRALINHA)
		.append("Vale: R$ ").append(this.valorValeDia)
		.append(QUEBRALINHA)
		.append(TRACEJADO)
		.append(QUEBRALINHA);
		
		return buffer.toString();
		
	}
}
