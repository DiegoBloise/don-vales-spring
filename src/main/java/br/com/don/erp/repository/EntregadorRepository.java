package br.com.don.erp.repository;

import java.util.List;

import javax.inject.Named;

import br.com.don.erp.model.Entregador;

@Named
public class EntregadorRepository extends GenericRepository<Entregador, Long> {

    public List<Entregador> listarPorNome() {
        String jpql = "SELECT e FROM Entregador e ORDER BY e.nome";
        return findDTOs(jpql, Entregador.class);
    }

}
