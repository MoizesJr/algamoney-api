package com.algamoney.api.algamoney_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algamoney.api.algamoney_api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
