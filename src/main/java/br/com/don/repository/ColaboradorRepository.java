package br.com.don.repository;

import java.util.List;

import br.com.don.model.Colaborador;
import jakarta.inject.Named;

@Named
public class ColaboradorRepository extends GenericRepository<Colaborador, Long> {

    public List<Colaborador> listarPorNome() {
        String jpql = "SELECT e FROM Colaborador e ORDER BY e.nome";
        return findDTOs(jpql, Colaborador.class);
    }

}
