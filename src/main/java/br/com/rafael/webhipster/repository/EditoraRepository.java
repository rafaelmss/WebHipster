package br.com.rafael.webhipster.repository;

import br.com.rafael.webhipster.domain.Editora;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Editora entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EditoraRepository extends JpaRepository<Editora, Long> {

}
