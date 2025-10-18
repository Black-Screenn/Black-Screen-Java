package com.sptech.school.Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Usuario {

  private int idUsuario;
  private String nome;
  private String email;
  private String senha;
  private int fkEmpresa;

public Usuario(int idUsuario, String nome, String email, String senha, int fkEmpresa){
    this.idUsuario = idUsuario;
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.fkEmpresa = fkEmpresa;
}

}
