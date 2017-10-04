package br.com.rafael.webhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rafael.webhipster.domain.Musica;

import br.com.rafael.webhipster.repository.MusicaRepository;
import br.com.rafael.webhipster.web.rest.util.HeaderUtil;
import br.com.rafael.webhipster.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Musica.
 */
@RestController
@RequestMapping("/api")
public class MusicaResource {

    private final Logger log = LoggerFactory.getLogger(MusicaResource.class);

    private static final String ENTITY_NAME = "musica";

    private final MusicaRepository musicaRepository;

    public MusicaResource(MusicaRepository musicaRepository) {
        this.musicaRepository = musicaRepository;
    }

    /**
     * POST  /musicas : Create a new musica.
     *
     * @param musica the musica to create
     * @return the ResponseEntity with status 201 (Created) and with body the new musica, or with status 400 (Bad Request) if the musica has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/musicas")
    @Timed
    public ResponseEntity<Musica> createMusica(@Valid @RequestBody Musica musica) throws URISyntaxException {
        log.debug("REST request to save Musica : {}", musica);
        if (musica.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new musica cannot already have an ID")).body(null);
        }
        Musica result = musicaRepository.save(musica);
        return ResponseEntity.created(new URI("/api/musicas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /musicas : Updates an existing musica.
     *
     * @param musica the musica to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated musica,
     * or with status 400 (Bad Request) if the musica is not valid,
     * or with status 500 (Internal Server Error) if the musica couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/musicas")
    @Timed
    public ResponseEntity<Musica> updateMusica(@Valid @RequestBody Musica musica) throws URISyntaxException {
        log.debug("REST request to update Musica : {}", musica);
        if (musica.getId() == null) {
            return createMusica(musica);
        }
        Musica result = musicaRepository.save(musica);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, musica.getId().toString()))
            .body(result);
    }

    /**
     * GET  /musicas : get all the musicas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of musicas in body
     */
    @GetMapping("/musicas")
    @Timed
    public ResponseEntity<List<Musica>> getAllMusicas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Musicas");
        Page<Musica> page = musicaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/musicas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /musicas/:id : get the "id" musica.
     *
     * @param id the id of the musica to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the musica, or with status 404 (Not Found)
     */
    @GetMapping("/musicas/{id}")
    @Timed
    public ResponseEntity<Musica> getMusica(@PathVariable Long id) {
        log.debug("REST request to get Musica : {}", id);
        Musica musica = musicaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(musica));
    }

    /**
     * DELETE  /musicas/:id : delete the "id" musica.
     *
     * @param id the id of the musica to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/musicas/{id}")
    @Timed
    public ResponseEntity<Void> deleteMusica(@PathVariable Long id) {
        log.debug("REST request to delete Musica : {}", id);
        musicaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
