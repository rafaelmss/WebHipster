package br.com.rafael.webhipster.repository;

import br.com.rafael.webhipster.domain.Musica;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Musica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MusicaRepository extends JpaRepository<Musica, Long> {

}
