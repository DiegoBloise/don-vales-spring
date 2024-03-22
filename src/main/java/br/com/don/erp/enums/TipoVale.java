package br.com.don.erp.enums;

public enum TipoVale {
	DINHEIRO("Dinheiro"),
	SALDO("Saldo");

	private String descricao;


    TipoVale(String descricao) {
        this.descricao = descricao;
    }


    public String getDescricao() {
        return this.descricao;
    }
}
