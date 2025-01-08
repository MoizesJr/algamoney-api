package com.algamoney.api.algamoney_api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.algamoney.api.algamoney_api.model.Categoria;
import com.algamoney.api.algamoney_api.repository.CategoriaRepository;

public class CategoriaService {

  @Autowired
  private CategoriaRepository categoriaRepository;

  public Categoria atualizar(Long codigo, Categoria categoria) {
    Categoria categoriaSalva = categoriaRepository.findById(codigo).orElseThrow();
    if (categoriaSalva == null) {
      throw new EmptyResultDataAccessException(1);
    }
    BeanUtils.copyProperties(categoria, categoriaSalva, "codigo");
    return categoriaRepository.save(categoriaSalva);
  }
}
