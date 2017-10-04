package br.com.rafael.webhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rafael.webhipster.domain.Editora;

import br.com.rafael.webhipster.repository.EditoraRepository;
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
 * REST controller for managing Editora.
 */
@RestController
@RequestMapping("/api")
public class EditoraResource {

    private final Logger log = LoggerFactory.getLogger(EditoraResource.class);

    private static final String ENTITY_NAME = "editora";

    private final EditoraRepository editoraRepository;

    public EditoraResource(EditoraRepository editoraRepository) {
        this.editoraRepository = editoraRepository;
    }

    /**
     * POST  /editoras : Create a new editora.
     *
     * @param editora the editora to create
     * @return the ResponseEntity with status 201 (Created) and with body the new editora, or with status 400 (Bad Request) if the editora has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/editoras")
    @Timed
    public ResponseEntity<Editora> createEditora(@Valid @RequestBody Editora editora) throws URISyntaxException {
        log.debug("REST request to save Editora : {}", editora);
        if (editora.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new editora cannot already have an ID")).body(null);
        }
        Editora result = editoraRepository.save(editora);
        return ResponseEntity.created(new URI("/api/editoras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /editoras : Updates an existing editora.
     *
     * @param editora the editora to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated editora,
     * or with status 400 (Bad Request) if the editora is not valid,
     * or with status 500 (Internal Server Error) if the editora couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/editoras")
    @Timed
    public ResponseEntity<Editora> updateEditora(@Valid @RequestBody Editora editora) throws URISyntaxException {
        log.debug("REST request to update Editora : {}", editora);
        if (editora.getId() == null) {
            return createEditora(editora);
        }
        Editora result = editoraRepository.save(editora);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, editora.getId().toString()))
            .body(result);
    }

    /**
     * GET  /editoras : get all the editoras.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of editoras in body
     */
    @GetMapping("/editoras")
    @Timed
    public ResponseEntity<List<Editora>> getAllEditoras(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Editoras");
        Page<Editora> page = editoraRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/editoras");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /editoras/:id : get the "id" editora.
     *
     * @param id the id of the editora to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the editora, or with status 404 (Not Found)
     */
    @GetMapping("/editoras/{id}")
    @Timed
    public ResponseEntity<Editora> getEditora(@PathVariable Long id) {
        log.debug("REST request to get Editora : {}", id);
        Editora editora = editoraRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(editora));
    }

    /**
     * DELETE  /editoras/:id : delete the "id" editora.
     *
     * @param id the id of the editora to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/editoras/{id}")
    @Timed
    public ResponseEntity<Void> deleteEditora(@PathVariable Long id) {
        log.debug("REST request to delete Editora : {}", id);
        editoraRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
