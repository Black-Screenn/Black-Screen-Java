package Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LogAcessoComputador {

  private int idComputador;
  private int idUsuario;

public LogAcessoComputador(int idComputador, int idUsuario){
    this.idComputador = idComputador;
    this.idUsuario = idUsuario;
}



}
