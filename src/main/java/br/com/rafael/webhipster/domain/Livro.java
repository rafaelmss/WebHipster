package br.com.rafael.webhipster.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Livro.
 */
@Entity
@Table(name = "livro")
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "autor", nullable = false)
    private String autor;

    @ManyToOne
    private Editora publicacao;

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

    public Livro nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public Livro autor(String autor) {
        this.autor = autor;
        return this;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Editora getPublicacao() {
        return publicacao;
    }

    public Livro publicacao(Editora editora) {
        this.publicacao = editora;
        return this;
    }

    public void setPublicacao(Editora editora) {
        this.publicacao = editora;
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
        Livro livro = (Livro) o;
        if (livro.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), livro.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Livro{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", autor='" + getAutor() + "'" +
            "}";
    }
}
