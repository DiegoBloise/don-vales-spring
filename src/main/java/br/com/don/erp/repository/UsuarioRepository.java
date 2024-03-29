package br.com.don.erp.repository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.don.erp.model.Usuario;


@Named
public class UsuarioRepository extends GenericRepository<Usuario, Long>{

    @Inject
    private EntityManager entityManager;

    public Usuario findByUsername(String username) {
        String jpql = "SELECT u FROM Usuario u WHERE u.username = :username";
        try {
            return entityManager.createQuery(jpql, Usuario.class)
                .setParameter("username", username)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
