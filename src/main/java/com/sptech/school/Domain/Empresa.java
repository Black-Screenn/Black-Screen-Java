package com.sptech.school.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Empresa {

  private int idEmpresa;
  private String nome;
  private String cnpj;
  private String razaoSocial;
  private String telefone;
  private int fkEndereco;


}
