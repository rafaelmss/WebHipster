package br.com.rafael.webhipster.repository;

import br.com.rafael.webhipster.domain.Filme;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Filme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {

}
