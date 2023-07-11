package br.com.don.erp.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util {

	public static LocalDate converteLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static String localDateFormatado(LocalDate localDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return formatter.format(localDate);
	}
	
	public static LocalDate converteDataHoraLocalDate(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		return LocalDate.parse(data,formatter);
	}
	
	public static LocalDate converteDataLocalDate(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(data,formatter);
	}
	
	public static String diaDaSemana(DayOfWeek dia) {
		String retorno = dia.toString();
		switch (dia) {
		case SATURDAY :
			retorno = "SÁBADO";
			break;
		case SUNDAY:
			retorno = "DOMINGO";
			break;
		case FRIDAY:
			retorno = "SEXTA-FEIRA";
			break;
		case MONDAY:
			retorno = "SEGUNDA-FEIRA";
			break;
		case THURSDAY:
			retorno = "QUINTA-FEIRA";
			break;
		case TUESDAY:
			retorno = "TERÇA-FEIRA";
			break;
		case WEDNESDAY:
			retorno = "QUARTA-FEIRA";
			break;
		}
		return retorno;
	}
}
