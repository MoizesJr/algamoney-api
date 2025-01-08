package com.algamoney.api.algamoney_api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.algamoney.api.algamoney_api.event.RecursoCriadoEvent;
import com.algamoney.api.algamoney_api.model.Categoria;
import com.algamoney.api.algamoney_api.repository.CategoriaRepository;
import com.algamoney.api.algamoney_api.service.CategoriaService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

  @Autowired
  private CategoriaService categoriaService;
  @Autowired
  private CategoriaRepository categoriaRepository;

  @GetMapping
  public List<Categoria> listar() {
    return categoriaRepository.findAll();
  }

  @Autowired
  private ApplicationEventPublisher publisher;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
    Categoria categoriaSalva = categoriaRepository.save(categoria);
    publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
    return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
    Optional<Categoria> categorias = categoriaRepository.findById(codigo);

    return categorias.isPresent() ? ResponseEntity.ok(categorias.get()) : ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{codigo}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable Long codigo) {
    if (!categoriaRepository.existsById(codigo)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria com codigo: " + codigo + " não encontrada");
    }
    categoriaRepository.deleteById(codigo);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<Categoria> atualizar(@PathVariable Long codigo, @Valid @RequestBody Categoria categoria) {
    Categoria categoriaSalva = categoriaService.atualizar(codigo, categoria);
    return ResponseEntity.ok(categoriaSalva);
  }
}
