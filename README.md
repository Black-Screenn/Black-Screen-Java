# BlackScreen java
Projeto Java de Lendo_CSV.ETL (Extract, Transform, Load) para Processamento e Integração de Dados

Este projeto foi desenvolvido em Java e implementa um pipeline completo de Lendo_CSV.ETL (Extract, Transform, Load), que é um processo essencial para a movimentação e tratamento de dados entre sistemas.
Principais Funcionalidades:

Extração (Extract) de Arquivo CSV: O projeto inicia com a leitura e extração de informações de um arquivo no formato CSV (Comma Separated Values), uma fonte de dados comum em diversas aplicações.

Transformação (Transform) e Comparação de Dados:

As informações extraídas do CSV passam por um processo de transformação para garantir sua qualidade e consistência.

Um ponto crucial da lógica é a comparação de valores: o sistema busca um valor de referência no banco de dados MySQL e o confronta com os dados contidos no arquivo CSV. Isso é fundamental para a validação, enriquecimento ou identificação de discrepâncias nos dados.

Carregamento (Load) e Persistência: Após a extração e o tratamento, o projeto gerencia a conexão com o banco de dados MySQL, onde são utilizados métodos de SELECT para realizar a comparação mencionada.

Tecnologias Utilizadas:

Java (Linguagem Principal): Garante a portabilidade, desempenho e estabilidade do projeto.

MySQL (Banco de Dados): Utilizado para a persistência e recuperação dos dados de referência, com a implementação de métodos de consulta (SELECT).

Project Lombok (Biblioteca de Produtividade): A utilização do Lombok é um diferencial importante, pois ele otimiza o código-fonte através de anotações como @Getter, @Setter e @AllArgsConstructor. Isso elimina a necessidade de escrever o código repetitivo (boilerplate code) de Getters, Setters e Constructors, resultando em um código mais limpo, conciso e altamente escalável.

# Diagrama de Arquitetura
![Diagrama de Arquitetura](src/img/img.png)


Projeto de PI do segundo semestre de Ciências da Computação da faculdade SPTech.

