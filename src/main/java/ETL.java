    import Lendo_CSV.LerCsv;
    import bancoDeDadosConf.ConexcaoBD;
    import bancoDeDadosConf.ScriptSQL;

    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;

    public class ETL {

         LerCsv lerCsv;
         ScriptSQL scriptSQL;
         ConexcaoBD conexcaoBD;
         ScriptSQL ScriptSQL;

        public String csvMaquina = "src/df_capturaMaquina.csv";
        public String csvProcesso = "src/df_capturaProcesso.csv";

        public class ResultadoCsv {
            private Double mediana;
            private String[] cabecalhos;
            private List<Double> valores;

            public ResultadoCsv(Double mediana, String[] cabecalhos, List<Double> valores) {
                this.mediana = mediana;
                this.cabecalhos = cabecalhos;
                this.valores = valores;
            }

            public Double getMediana() { return mediana; }
            public String[] getCabecalhos() { return cabecalhos; }
            public List<Double> getValores() { return valores; }
        }
        public  class ResultadoComparacao {
            private String nomeComponente;
            private Double valorCsv;
            private Long valorBanco;
            private Boolean alertaGerado;
            private String mensagem;

            public ResultadoComparacao(String nomeComponente, Double valorCsv, Long valorBanco,
                                       Boolean alertaGerado, String mensagem) {
                this.nomeComponente = nomeComponente;
                this.valorCsv = valorCsv;
                this.valorBanco = valorBanco;
                this.alertaGerado = alertaGerado;
                this.mensagem = mensagem;
            }

        }
        public ResultadoCsv tratandoCsv(LerCsv lerCsv, String nomeColuna) {
            List<String[]> dados = lerCsv.leituraCsv(csvMaquina);

            if(csvMaquina == null||csvMaquina.equals("")){
                System.out.println("Csv vazio ou nulo");
                return null;
            }
            String[] cabecalhos = dados.get(0);
            int indiceColunaDesejada = -1;
            for (int i =0; i < cabecalhos.length; i++) {
                        if(cabecalhos[i].equals(nomeColuna)){
                            indiceColunaDesejada = i;
                            break;
                }
            }
            List<Double> valores = new ArrayList<>();
            for (int i = 1; i < dados.size(); i++) {
                String[] linha = dados.get(i);

                if (indiceColunaDesejada < linha.length) {
                    try {
                        double valor = Double.parseDouble(linha[indiceColunaDesejada].trim());
                        valores.add(valor);
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido na linha " + i + ": " + linha[indiceColunaDesejada]);
                    }
                }
            }
                Double mediana = calcularMediana(valores);
            return new ResultadoCsv(mediana, cabecalhos, valores);
        }
        public Double calcularMediana(List<Double> valores) {
            if (valores.isEmpty()) {
                return null;
            }
            Collections.sort(valores);
            int tamanho = valores.size();
            if (tamanho % 2 == 0) {
                return (valores.get(tamanho / 2 - 1) + valores.get(tamanho / 2)) / 2.0;
            } else {
                return valores.get(tamanho / 2);

            }
        }
        public int compararValoresprocessarEGerarCsvAlerta(LerCsv lerCsv, ScriptSQL scriptSQL, String nomeComponente, String csvAlerta, String Id_Usuario) throws SQLException {

            List<String[]> dadosOriginais = lerCsv.leituraCsv(csvMaquina);
            Integer idComputadorWrapper = scriptSQL.buscarPorUsuario(Integer.parseInt(Id_Usuario));

            if (idComputadorWrapper == null) {
                System.err.println("ERRO: ID do computador não encontrado para o usuário: " + Id_Usuario);
                return 0;
            }
            int idComputador = idComputadorWrapper;
            Long valorBanco = scriptSQL.buscarParametroPorNome(nomeComponente, idComputador);

            if (valorBanco == null) {
                return 0;
            }

            String[] cabecalhos = dadosOriginais.get(0);
            int indiceColuna = -1;
            System.out.println(Arrays.toString(cabecalhos));


            for (int i = 0; i < cabecalhos.length; i++) {
                String cabecalhoLimpo = cabecalhos[i].trim().replace("\"", "");
                if (cabecalhoLimpo.equalsIgnoreCase(nomeComponente)) {
                    indiceColuna = i;
                    System.out.println(indiceColuna);
                    break;
                }

            }
            List<String[]> linhasAlerta = new ArrayList<>();
            linhasAlerta.add(cabecalhos);
            int contador = 0;

            for (int i = 1; i < dadosOriginais.size(); i++) {
                String[] linha = dadosOriginais.get(i);

                try {
                    String valorString = linha[indiceColuna];
                    String valorLimpo = valorString
                            .trim()
                            .replace("\"", "")
                            .replace(",", ".");
                    double valor = Double.parseDouble(valorLimpo);
                    if (valor > valorBanco //min ou max no banco
                     ) {
                        linhasAlerta.add(linha);
                        contador++;
                        System.out.println(valor);
                        System.out.println(valorBanco);
                        System.out.println(linhasAlerta.size());
                        System.out.println(linha);

                    }
                } catch (NumberFormatException e) {
                }
            }
            if (contador > 0) {
                LerCsv.escreverCsv(csvAlerta, linhasAlerta);
            }

            return contador;

        }


    }