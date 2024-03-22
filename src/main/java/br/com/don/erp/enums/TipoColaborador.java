package br.com.don.erp.enums;

public enum TipoColaborador {
	FIXO("Fixo"),
	FREE("Freelancer"),
	ENTREGADOR("Entregador");

	private String descricao;


    TipoColaborador(String descricao) {
        this.descricao = descricao;
    }


    public String getDescricao() {
        return this.descricao;
    }
}