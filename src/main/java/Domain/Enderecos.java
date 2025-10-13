package Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
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

    public Enderecos(int idEndereco,String cep,String cidade,String pais,String estado,String rua,long numero,double latitude,double longitude){
    this.idEndereco=idEndereco;
        this.cep=cep;
        this.cidade=cidade;
        this.estado=estado;
        this.rua=rua;
        this.numero=numero;
        this.latitude=latitude;
        this.longitude=longitude;
        this.pais=pais;
    }

}
