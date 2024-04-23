package br.com.don.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.don.models.Freelancer;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {

}
