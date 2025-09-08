package screen.black;

public class Captura {
    String dataHora;
    String componente;
    double valor;
    double limite;

    public Captura(String dataHora, String componente, double valor, double limite) {
        this.dataHora = dataHora;
        this.componente = componente;
        this.valor = valor;
        this.limite = limite;
    }

    public boolean ultrapassouLimite(){
        return valor > limite;
    }

    public String imprimir(){
        return dataHora + " | " + componente + " | " + valor;
    }
}
