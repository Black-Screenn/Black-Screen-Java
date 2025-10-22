package com.sptech.school.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Enderecos {

  private int idEndereco;
  private String cep;
  private String pais;
  private String cidade;
  private String estado;
  private String rua;
  private long numero;
  private double latitude;
  private double longitude;



}
