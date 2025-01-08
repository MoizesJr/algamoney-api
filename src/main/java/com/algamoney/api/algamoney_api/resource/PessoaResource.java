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
import com.algamoney.api.algamoney_api.model.Pessoa;
import com.algamoney.api.algamoney_api.repository.PessoaRepository;
import com.algamoney.api.algamoney_api.service.PessoaService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

  @Autowired
  private PessoaService pessoaService;
  @Autowired
  private PessoaRepository pessoaRepository;

  @GetMapping
  public List<Pessoa> listar() {
    return pessoaRepository.findAll();
  }

  @Autowired
  private ApplicationEventPublisher publisher;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
    Pessoa pessoaSalva = pessoaRepository.save(pessoa);
    publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
    return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<Pessoa> buscarPeloCod(@PathVariable Long codigo) {
    Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);

    return pessoa.isPresent() ? ResponseEntity.ok(pessoa.get()) : ResponseEntity.notFound().build();
  }

  // PESQUISAR MANEIRAS DE DEIXAR O CODIGO LIMPO
  @DeleteMapping("/{codigo}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable Long codigo) {
    if (!pessoaRepository.existsById(codigo)) {
      // Lança uma exceção ResponseStatusException com o código de status 404
      // (NOT_FOUND)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com codigo: " + codigo + " não encontrada");
    }

    pessoaRepository.deleteById(codigo);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
    Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
    return ResponseEntity.ok(pessoaSalva);
  }
}
