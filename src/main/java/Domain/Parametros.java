package Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Parametros {

  private int idParametro;
  private long valorParametrizado;
  private int fkComponente;

public Parametros(int idParametro, long valorParametrizado, int fkComponente){
        this.idParametro = idParametro;
        this.valorParametrizado = valorParametrizado;
        this.fkComponente = fkComponente;

}

}
