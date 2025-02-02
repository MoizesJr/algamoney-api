package com.algamoney.api.algamoney_api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private MessageSource messageSource;

  @Override
  @Nullable
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {

    String mensagemUsuario = messageSource.getMessage("mesagem.invalida", null, LocaleContextHolder.getLocale());
    String mensagemDesenvolvedor = ex.getCause().toString();

    List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
    return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);

  }

  @Override
  @Nullable
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {

    List<Erro> erros = criarListaDeErros(ex.getBindingResult());
    return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({ EmptyResultDataAccessException.class })
  public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
      WebRequest request) {
    String mensagemUsuario = messageSource.getMessage("recurso.não-encontrado", null, LocaleContextHolder.getLocale());
    String mensagemDesenvolvedor = ex.toString();
    List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
    return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  private List<Erro> criarListaDeErros(BindingResult bindingResult) {
    List<Erro> erros = new ArrayList<>();

    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
      String mensagemDesenvolvedor = fieldError.toString();
      erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
    }

    return erros;
  }

  public static class Erro {

    private String mensagemUsuario;
    private String mensagemDesenvolvedor;

    public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
      this.mensagemDesenvolvedor = mensagemDesenvolvedor;
      this.mensagemUsuario = mensagemUsuario;
    }

    public String getMensagemUsuario() {
      return mensagemUsuario;
    }

    public String getMensagemDesenvolvedor() {
      return mensagemDesenvolvedor;
    }

  }
}
