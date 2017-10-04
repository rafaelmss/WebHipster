package br.com.rafael.webhipster.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Musica.
 */
@Entity
@Table(name = "musica")
public class Musica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "lancamento", nullable = false)
    private LocalDate lancamento;

    @NotNull
    @Column(name = "artista", nullable = false)
    private String artista;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Musica nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getLancamento() {
        return lancamento;
    }

    public Musica lancamento(LocalDate lancamento) {
        this.lancamento = lancamento;
        return this;
    }

    public void setLancamento(LocalDate lancamento) {
        this.lancamento = lancamento;
    }

    public String getArtista() {
        return artista;
    }

    public Musica artista(String artista) {
        this.artista = artista;
        return this;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Musica musica = (Musica) o;
        if (musica.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), musica.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Musica{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", lancamento='" + getLancamento() + "'" +
            ", artista='" + getArtista() + "'" +
            "}";
    }
}
