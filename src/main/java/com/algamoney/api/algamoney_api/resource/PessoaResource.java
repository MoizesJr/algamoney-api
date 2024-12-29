package com.algamoney.api.algamoney_api.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algamoney.api.algamoney_api.model.Pessoa;
import com.algamoney.api.algamoney_api.repository.PessoaRepository;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

  @Autowired
  private PessoaRepository pessoaRepository;

  @GetMapping
  public List<Pessoa> listar() {
    return pessoaRepository.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Pessoa> criar(@RequestBody Pessoa pessoa, HttpServletResponse response) {
    Pessoa pessoaSalva = pessoaRepository.save(pessoa);
    // vai criar no headers, a location (codigo para consultas rapidas)
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
        .buildAndExpand(pessoaSalva.getCodigo()).toUri();
    response.setHeader("location", uri.toASCIIString());

    return new ResponseEntity<>(pessoaSalva, HttpStatus.CREATED);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<Pessoa> buscarPeloCod(@PathVariable Long codigo) {
    Optional<Pessoa> pessoas = pessoaRepository.findById(codigo);

    return pessoas.isPresent() ? ResponseEntity.ok(pessoas.get()) : ResponseEntity.notFound().build();
  }

}
