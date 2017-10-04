package br.com.rafael.webhipster.web.rest;

import br.com.rafael.webhipster.WebHipsterApp;

import br.com.rafael.webhipster.domain.Livro;
import br.com.rafael.webhipster.repository.LivroRepository;
import br.com.rafael.webhipster.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LivroResource REST controller.
 *
 * @see LivroResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebHipsterApp.class)
public class LivroResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTOR = "BBBBBBBBBB";

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLivroMockMvc;

    private Livro livro;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LivroResource livroResource = new LivroResource(livroRepository);
        this.restLivroMockMvc = MockMvcBuilders.standaloneSetup(livroResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livro createEntity(EntityManager em) {
        Livro livro = new Livro()
            .nome(DEFAULT_NOME)
            .autor(DEFAULT_AUTOR);
        return livro;
    }

    @Before
    public void initTest() {
        livro = createEntity(em);
    }

    @Test
    @Transactional
    public void createLivro() throws Exception {
        int databaseSizeBeforeCreate = livroRepository.findAll().size();

        // Create the Livro
        restLivroMockMvc.perform(post("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livro)))
            .andExpect(status().isCreated());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeCreate + 1);
        Livro testLivro = livroList.get(livroList.size() - 1);
        assertThat(testLivro.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testLivro.getAutor()).isEqualTo(DEFAULT_AUTOR);
    }

    @Test
    @Transactional
    public void createLivroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = livroRepository.findAll().size();

        // Create the Livro with an existing ID
        livro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivroMockMvc.perform(post("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livro)))
            .andExpect(status().isBadRequest());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = livroRepository.findAll().size();
        // set the field null
        livro.setNome(null);

        // Create the Livro, which fails.

        restLivroMockMvc.perform(post("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livro)))
            .andExpect(status().isBadRequest());

        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAutorIsRequired() throws Exception {
        int databaseSizeBeforeTest = livroRepository.findAll().size();
        // set the field null
        livro.setAutor(null);

        // Create the Livro, which fails.

        restLivroMockMvc.perform(post("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livro)))
            .andExpect(status().isBadRequest());

        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLivros() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        // Get all the livroList
        restLivroMockMvc.perform(get("/api/livros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR.toString())));
    }

    @Test
    @Transactional
    public void getLivro() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        // Get the livro
        restLivroMockMvc.perform(get("/api/livros/{id}", livro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(livro.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.autor").value(DEFAULT_AUTOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLivro() throws Exception {
        // Get the livro
        restLivroMockMvc.perform(get("/api/livros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLivro() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);
        int databaseSizeBeforeUpdate = livroRepository.findAll().size();

        // Update the livro
        Livro updatedLivro = livroRepository.findOne(livro.getId());
        updatedLivro
            .nome(UPDATED_NOME)
            .autor(UPDATED_AUTOR);

        restLivroMockMvc.perform(put("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLivro)))
            .andExpect(status().isOk());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
        Livro testLivro = livroList.get(livroList.size() - 1);
        assertThat(testLivro.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLivro.getAutor()).isEqualTo(UPDATED_AUTOR);
    }

    @Test
    @Transactional
    public void updateNonExistingLivro() throws Exception {
        int databaseSizeBeforeUpdate = livroRepository.findAll().size();

        // Create the Livro

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLivroMockMvc.perform(put("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livro)))
            .andExpect(status().isCreated());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLivro() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);
        int databaseSizeBeforeDelete = livroRepository.findAll().size();

        // Get the livro
        restLivroMockMvc.perform(delete("/api/livros/{id}", livro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Livro.class);
        Livro livro1 = new Livro();
        livro1.setId(1L);
        Livro livro2 = new Livro();
        livro2.setId(livro1.getId());
        assertThat(livro1).isEqualTo(livro2);
        livro2.setId(2L);
        assertThat(livro1).isNotEqualTo(livro2);
        livro1.setId(null);
        assertThat(livro1).isNotEqualTo(livro2);
    }
}
