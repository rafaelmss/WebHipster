package br.com.rafael.webhipster.web.rest;

import br.com.rafael.webhipster.WebHipsterApp;

import br.com.rafael.webhipster.domain.Filme;
import br.com.rafael.webhipster.repository.FilmeRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FilmeResource REST controller.
 *
 * @see FilmeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebHipsterApp.class)
public class FilmeResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LANCAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LANCAMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private FilmeRepository filmeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFilmeMockMvc;

    private Filme filme;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FilmeResource filmeResource = new FilmeResource(filmeRepository);
        this.restFilmeMockMvc = MockMvcBuilders.standaloneSetup(filmeResource)
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
    public static Filme createEntity(EntityManager em) {
        Filme filme = new Filme()
            .nome(DEFAULT_NOME)
            .lancamento(DEFAULT_LANCAMENTO)
            .descricao(DEFAULT_DESCRICAO);
        return filme;
    }

    @Before
    public void initTest() {
        filme = createEntity(em);
    }

    @Test
    @Transactional
    public void createFilme() throws Exception {
        int databaseSizeBeforeCreate = filmeRepository.findAll().size();

        // Create the Filme
        restFilmeMockMvc.perform(post("/api/filmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filme)))
            .andExpect(status().isCreated());

        // Validate the Filme in the database
        List<Filme> filmeList = filmeRepository.findAll();
        assertThat(filmeList).hasSize(databaseSizeBeforeCreate + 1);
        Filme testFilme = filmeList.get(filmeList.size() - 1);
        assertThat(testFilme.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFilme.getLancamento()).isEqualTo(DEFAULT_LANCAMENTO);
        assertThat(testFilme.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createFilmeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = filmeRepository.findAll().size();

        // Create the Filme with an existing ID
        filme.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilmeMockMvc.perform(post("/api/filmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filme)))
            .andExpect(status().isBadRequest());

        // Validate the Filme in the database
        List<Filme> filmeList = filmeRepository.findAll();
        assertThat(filmeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmeRepository.findAll().size();
        // set the field null
        filme.setNome(null);

        // Create the Filme, which fails.

        restFilmeMockMvc.perform(post("/api/filmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filme)))
            .andExpect(status().isBadRequest());

        List<Filme> filmeList = filmeRepository.findAll();
        assertThat(filmeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLancamentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmeRepository.findAll().size();
        // set the field null
        filme.setLancamento(null);

        // Create the Filme, which fails.

        restFilmeMockMvc.perform(post("/api/filmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filme)))
            .andExpect(status().isBadRequest());

        List<Filme> filmeList = filmeRepository.findAll();
        assertThat(filmeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFilmes() throws Exception {
        // Initialize the database
        filmeRepository.saveAndFlush(filme);

        // Get all the filmeList
        restFilmeMockMvc.perform(get("/api/filmes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filme.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].lancamento").value(hasItem(DEFAULT_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getFilme() throws Exception {
        // Initialize the database
        filmeRepository.saveAndFlush(filme);

        // Get the filme
        restFilmeMockMvc.perform(get("/api/filmes/{id}", filme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(filme.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.lancamento").value(DEFAULT_LANCAMENTO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFilme() throws Exception {
        // Get the filme
        restFilmeMockMvc.perform(get("/api/filmes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFilme() throws Exception {
        // Initialize the database
        filmeRepository.saveAndFlush(filme);
        int databaseSizeBeforeUpdate = filmeRepository.findAll().size();

        // Update the filme
        Filme updatedFilme = filmeRepository.findOne(filme.getId());
        updatedFilme
            .nome(UPDATED_NOME)
            .lancamento(UPDATED_LANCAMENTO)
            .descricao(UPDATED_DESCRICAO);

        restFilmeMockMvc.perform(put("/api/filmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFilme)))
            .andExpect(status().isOk());

        // Validate the Filme in the database
        List<Filme> filmeList = filmeRepository.findAll();
        assertThat(filmeList).hasSize(databaseSizeBeforeUpdate);
        Filme testFilme = filmeList.get(filmeList.size() - 1);
        assertThat(testFilme.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFilme.getLancamento()).isEqualTo(UPDATED_LANCAMENTO);
        assertThat(testFilme.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingFilme() throws Exception {
        int databaseSizeBeforeUpdate = filmeRepository.findAll().size();

        // Create the Filme

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFilmeMockMvc.perform(put("/api/filmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filme)))
            .andExpect(status().isCreated());

        // Validate the Filme in the database
        List<Filme> filmeList = filmeRepository.findAll();
        assertThat(filmeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFilme() throws Exception {
        // Initialize the database
        filmeRepository.saveAndFlush(filme);
        int databaseSizeBeforeDelete = filmeRepository.findAll().size();

        // Get the filme
        restFilmeMockMvc.perform(delete("/api/filmes/{id}", filme.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Filme> filmeList = filmeRepository.findAll();
        assertThat(filmeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filme.class);
        Filme filme1 = new Filme();
        filme1.setId(1L);
        Filme filme2 = new Filme();
        filme2.setId(filme1.getId());
        assertThat(filme1).isEqualTo(filme2);
        filme2.setId(2L);
        assertThat(filme1).isNotEqualTo(filme2);
        filme1.setId(null);
        assertThat(filme1).isNotEqualTo(filme2);
    }
}
