package br.com.don.util;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Acerto implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocalDate data;

	private Integer qtdeEntregasDia;

	private Integer qtdeIFood;

	private Double valorValeDia;

	private Double valorDiaria;

	private final String QUEBRALINHA ="\n";

	private final String TRACEJADO = "--------------------------------";


	public Double getValorDiaria() {
		boolean isFimDeSemana = false;
		if(data.getDayOfWeek().equals(DayOfWeek.FRIDAY) ||
			data.getDayOfWeek().equals(DayOfWeek.SATURDAY)||
			data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			isFimDeSemana = true;
		}

		if(isFimDeSemana) {
			if(qtdeEntregasDia > 40) {
				this.valorDiaria = 50.00;
			}else if(qtdeEntregasDia > 30) {
				this.valorDiaria = 45.00;
			}else if(qtdeEntregasDia > 20) {
				this.valorDiaria = 40.00;
			}else if(qtdeEntregasDia > 9) {
				this.valorDiaria = 35.00;
			}else {
				this.valorDiaria = 0.00;
			}
		}else {
			if(qtdeEntregasDia > 9) {
				this.valorDiaria = 35.00;
			}else {
				this.valorDiaria = 0.00;
			}
		}

		return this.valorDiaria;
	}


	public String getDataFormatada() {
		return Util.dataSemanaFormatada(data);
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
