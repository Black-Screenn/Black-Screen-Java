package com.sptech.school.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Computador {

  private int idComputador;
  private String nomeMaquina;
  private int fkEnderecoMaquina;
  private int fkEmpresa;




}
