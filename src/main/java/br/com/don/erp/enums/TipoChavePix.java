package br.com.don.erp.enums;

public enum TipoChavePix {
	CELULAR("Celular"),
    CPF("CPF"),
    CNPJ("CNPJ"),
    EMAIL("E-mail"),
	ALEATORIA("Aleatória");

	private String descricao;


    TipoChavePix(String descricao) {
        this.descricao = descricao;
    }


    public String getDescricao() {
        return this.descricao;
    }
}
