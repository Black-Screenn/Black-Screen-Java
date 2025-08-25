package screen.black;

public class warning {
    public static void verificarAviso(String nome, double valor) {
        if (valor > 70.0) {
            System.out.println("AVISO: " + nome + " está acima de 70% (" + valor + "%)");
        }
    }

    public static void mocking() {
        cpuMetrics cpu1 = new cpuMetrics();
        cpu1.percent = 20.0;
        cpuMetrics cpu2 = new cpuMetrics();
        cpu2.percent = 75.0;

        ramMetrics ram1 = new ramMetrics();
        ram1.percent = 40.0;
        ramMetrics ram2 = new ramMetrics();
        ram2.percent = 81.2;

        diskMetrics disk1 = new diskMetrics();
        disk1.usage = 45.5;
        diskMetrics disk2 = new diskMetrics();
        disk2.usage = 71.4;

        verificarAviso("cpu1", cpu1.percent);
        verificarAviso("cpu2", cpu2.percent);

        verificarAviso("ram1", ram1.percent);
        verificarAviso("ram2", ram2.percent);

        verificarAviso("disk1", disk1.usage);
        verificarAviso("disk2   ", disk2.usage);

    }

    public static void main(String[] args){
        mocking();
    }
}
