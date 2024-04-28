package br.com.don.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.don.model.Freelancer;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {

}
