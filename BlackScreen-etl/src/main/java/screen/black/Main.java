package screen.black;

public class Main {
    public static void main(String[] args) {
        Captura captura1 = new Captura("2025-08-28 09:58","CPU",70,85);
        Captura captura2 = new Captura("2025-08-28 09:58","Memória",92,90);
        Captura captura3 = new Captura("2025-08-28 09:58","Disco",60,95);

        System.out.println("--- Registros ---");
        System.out.println(captura1.imprimir());
        System.out.println(captura2.imprimir());
        System.out.println(captura3.imprimir());

        System.out.println("\n--- Alertas ---");
        if(captura1.ultrapassouLimite()){
            System.out.println("! ALERTA: " + captura1.componente + " acima do limite (" + captura1.limite + " )! Valor: " + captura1.valor);
        }

        if(captura2.ultrapassouLimite()){
            System.out.println("! ALERTA: " + captura2.componente + " acima do limite (" + captura2.limite + " )! Valor: " + captura2.valor);
        }

        if(captura3.ultrapassouLimite()){
            System.out.println("! ALERTA: " + captura3.componente + " acima do limite (" + captura3.limite + " )! Valor: " + captura3.valor);
        }
    }
}
