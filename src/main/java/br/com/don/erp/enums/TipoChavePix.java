package br.com.don.erp.enums;

public enum TipoChavePix {
	TELEFONE("Número de telefone celular"),
    CPF("CPF"),
    CNPJ("CNPJ"),
    EMAIL("Email"),
	ALEATORIA("Chave aleatória");

	private String descricao;


    TipoChavePix(String descricao) {
        this.descricao = descricao;
    }


    public String getDescricao() {
        return this.descricao;
    }
}
