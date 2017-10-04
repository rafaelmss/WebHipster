package br.com.rafael.webhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rafael.webhipster.domain.Livro;

import br.com.rafael.webhipster.repository.LivroRepository;
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
 * REST controller for managing Livro.
 */
@RestController
@RequestMapping("/api")
public class LivroResource {

    private final Logger log = LoggerFactory.getLogger(LivroResource.class);

    private static final String ENTITY_NAME = "livro";

    private final LivroRepository livroRepository;

    public LivroResource(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    /**
     * POST  /livros : Create a new livro.
     *
     * @param livro the livro to create
     * @return the ResponseEntity with status 201 (Created) and with body the new livro, or with status 400 (Bad Request) if the livro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/livros")
    @Timed
    public ResponseEntity<Livro> createLivro(@Valid @RequestBody Livro livro) throws URISyntaxException {
        log.debug("REST request to save Livro : {}", livro);
        if (livro.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new livro cannot already have an ID")).body(null);
        }
        Livro result = livroRepository.save(livro);
        return ResponseEntity.created(new URI("/api/livros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /livros : Updates an existing livro.
     *
     * @param livro the livro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated livro,
     * or with status 400 (Bad Request) if the livro is not valid,
     * or with status 500 (Internal Server Error) if the livro couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/livros")
    @Timed
    public ResponseEntity<Livro> updateLivro(@Valid @RequestBody Livro livro) throws URISyntaxException {
        log.debug("REST request to update Livro : {}", livro);
        if (livro.getId() == null) {
            return createLivro(livro);
        }
        Livro result = livroRepository.save(livro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, livro.getId().toString()))
            .body(result);
    }

    /**
     * GET  /livros : get all the livros.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of livros in body
     */
    @GetMapping("/livros")
    @Timed
    public ResponseEntity<List<Livro>> getAllLivros(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Livros");
        Page<Livro> page = livroRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/livros");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /livros/:id : get the "id" livro.
     *
     * @param id the id of the livro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the livro, or with status 404 (Not Found)
     */
    @GetMapping("/livros/{id}")
    @Timed
    public ResponseEntity<Livro> getLivro(@PathVariable Long id) {
        log.debug("REST request to get Livro : {}", id);
        Livro livro = livroRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(livro));
    }

    /**
     * DELETE  /livros/:id : delete the "id" livro.
     *
     * @param id the id of the livro to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/livros/{id}")
    @Timed
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id) {
        log.debug("REST request to delete Livro : {}", id);
        livroRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
