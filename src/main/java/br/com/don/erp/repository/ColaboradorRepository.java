package br.com.don.erp.repository;

import java.util.List;

import javax.inject.Named;

import br.com.don.erp.model.Colaborador;

@Named
public class ColaboradorRepository extends GenericRepository<Colaborador, Long> {

    public List<Colaborador> listarPorNome() {
        String jpql = "SELECT e FROM Colaborador e ORDER BY e.nome";
        return findDTOs(jpql, Colaborador.class);
    }

}
