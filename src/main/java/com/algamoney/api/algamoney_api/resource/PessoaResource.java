package com.algamoney.api.algamoney_api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algamoney.api.algamoney_api.event.RecursoCriadoEvent;
import com.algamoney.api.algamoney_api.model.Pessoa;
import com.algamoney.api.algamoney_api.repository.PessoaRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

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
    Optional<Pessoa> pessoas = pessoaRepository.findById(codigo);

    return pessoas.isPresent() ? ResponseEntity.ok(pessoas.get()) : ResponseEntity.notFound().build();
  }

}
