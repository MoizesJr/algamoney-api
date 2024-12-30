package com.algamoney.api.algamoney_api.event.listener;

import java.net.URI;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algamoney.api.algamoney_api.event.RecursoCriadoEvent;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {

  @Override
  public void onApplicationEvent(RecursoCriadoEvent recursoCriadoEvent) {
    HttpServletResponse response = recursoCriadoEvent.getResponse();
    Long codigo = recursoCriadoEvent.getCodigo();

    adicionarHeaderLocation(response, codigo);
  }

  private void adicionarHeaderLocation(HttpServletResponse response, Long codigo) {
    // vai criar no headers, a location (codigo para consultas rapidas)
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
        .buildAndExpand(codigo).toUri();
    response.setHeader("location", uri.toASCIIString());
  }
}
