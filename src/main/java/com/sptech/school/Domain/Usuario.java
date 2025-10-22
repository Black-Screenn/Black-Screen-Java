package com.sptech.school.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Usuario {

  private int idUsuario;
  private String nome;
  private String email;
  private String senha;
  private int fkEmpresa;



}
