package Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Componentes {

  private int idComponente;
  private String nomeComponente;
  private int fkComputador;

    public Componentes(int idComponente,String nomeComponente, int fkComputador){
    this.idComponente=idComponente;
    this.nomeComponente=nomeComponente;
    this.fkComputador=fkComputador;
    }


}
