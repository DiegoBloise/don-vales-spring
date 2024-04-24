package br.com.don.enums;

public enum TipoColaborador {
	FIXO("Fixo"),
	FREELANCER("Freelancer"),
	ENTREGADOR("Entregador");

	private String descricao;


    TipoColaborador(String descricao) {
        this.descricao = descricao;
    }


    public String getDescricao() {
        return this.descricao;
    }
}