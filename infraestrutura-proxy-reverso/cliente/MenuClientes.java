
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class MenuClientes {

    // Endereço da API através do Nginx (VM1)
    // O Nginx encaminha as requisições para a API na VM2
    private static final String URL =
            "http://192.168.56.10/clientes";

    // Cliente HTTP responsável por enviar as requisições
    private static final HttpClient clienteHttp =
            HttpClient.newHttpClient();

    private static final Scanner scanner =
            new Scanner(System.in);

    public static void main(String[] args) {

        int opcao;

        do {
            System.out.println("\n======================================");
            System.out.println("     SISTEMA DE GERENCIAMENTO");
            System.out.println("             DE CLIENTES");
            System.out.println("======================================");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Buscar cliente por ID");
            System.out.println("4 - Atualizar cliente");
            System.out.println("5 - Excluir cliente");
            System.out.println("0 - Sair");
            System.out.println("======================================");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1 -> cadastrarCliente();
                    case 2 -> listarClientes();
                    case 3 -> buscarCliente();
                    case 4 -> atualizarCliente();
                    case 5 -> excluirCliente();
                    case 0 -> System.out.println(
                            "\nEncerrando o sistema..."
                    );
                    default -> System.out.println(
                            "\nOpcao invalida!"
                    );
                }

            } catch (NumberFormatException e) {
                opcao = -1;
                System.out.println("\nDigite uma opcao valida!");
            }

        } while (opcao != 0);

        scanner.close();
    }

    // 1 - Utiliza o método HTTP POST para enviar um novo cliente à API
    private static void cadastrarCliente() {

        System.out.println("\n--- CADASTRO DE CLIENTE ---");

        String nome = lerCampo("Nome: ");
        String email = lerCampo("Email: ");
        String telefone = lerCampo("Telefone: ");

        String json = criarJson(nome, email, telefone);

        enviarRequisicao("POST", URL, json);
    }

    // 2 - Utiliza o método HTTP GET para buscar todos os clientes cadastrados
    private static void listarClientes() {

        System.out.println("\n--- LISTA DE CLIENTES ---");

        enviarRequisicao("GET", URL, null);
    }

    // 3 - Utiliza GET para consultar um cliente específico pelo seu identificador
    private static void buscarCliente() {

        System.out.println("\n--- BUSCAR CLIENTE ---");

        long id = lerId();

        enviarRequisicao("GET", URL + "/" + id, null);
    }

    // 4 - Utiliza o método HTTP PUT para atualizar os dados de um cliente
    private static void atualizarCliente() {

        System.out.println("\n--- ATUALIZAR CLIENTE ---");

        long id = lerId();

        String nome = lerCampo("Novo nome: ");
        String email = lerCampo("Novo email: ");
        String telefone = lerCampo("Novo telefone: ");

        String json = criarJson(nome, email, telefone);

        enviarRequisicao("PUT", URL + "/" + id, json);
    }

    // 5 - Utiliza o método HTTP DELETE para excluir um cliente
    private static void excluirCliente() {

        System.out.println("\n--- EXCLUIR CLIENTE ---");

        long id = lerId();

        System.out.print(
                "Confirma a exclusao do cliente " + id + "? (s/n): "
        );

        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("s")) {
            enviarRequisicao("DELETE", URL + "/" + id, null);
        } else {
            System.out.println("\nExclusao cancelada.");
        }
    }

    // MÉTODO CENTRAL DE COMUNICAÇÃO COM A API
    // Responsável por enviar as requisições HTTP e receber as respostas
    private static void enviarRequisicao(
            String metodo, String endereco, String json) {

        try {
            // Cria uma requisição para o endereço informado
            HttpRequest.Builder requisicao = HttpRequest.newBuilder()
                    .uri(URI.create(endereco));

            switch (metodo) {
                case "GET" ->
                        requisicao.GET();

                case "DELETE" ->
                        requisicao.DELETE();

                case "POST" ->
                        requisicao.header(
                                "Content-Type", "application/json"
                        ).POST(
                                HttpRequest.BodyPublishers.ofString(json)
                        );

                case "PUT" ->
                        requisicao.header(
                                "Content-Type", "application/json"
                        ).PUT(
                                HttpRequest.BodyPublishers.ofString(json)
                        );
            }

            // Envia a requisição e aguarda a resposta da API
            HttpResponse<String> resposta = clienteHttp.send(
                    requisicao.build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            // Verifica se a operação foi realizada com sucesso
            if (resposta.statusCode() >= 200
                    && resposta.statusCode() < 300) {

                switch (metodo) {
                    case "POST" ->
                            System.out.println(
                                    "\nCliente cadastrado com sucesso!"
                            );

                    case "PUT" ->
                            System.out.println(
                                    "\nCliente atualizado com sucesso!"
                            );

                    case "DELETE" ->
                            System.out.println(
                                    "\nCliente excluido com sucesso!"
                            );

                    case "GET" -> {

    String corpo = resposta.body();

    if (corpo != null && !corpo.equals("[]")
            && !corpo.isBlank()) {

        // Retira os colchetes da lista retornada pela API
        corpo = corpo.replace("[", "")
                     .replace("]", "");

        // Separa os clientes quando houver mais de um
        corpo = corpo.replace("},{", "\n\n");

        // Organiza os campos de cada cliente
        corpo = corpo.replace("{", "")
                     .replace("}", "")
                     .replace(",", "\n")
                     .replace("\"", "")
                     .replace(":", ": ");

        System.out.println("\n" + corpo);

    } else {
        System.out.println("\nNenhum cliente cadastrado.");
    }
}
                }

            } else {
                // Exibe uma mensagem simples caso ocorra algum erro
                System.out.println(
                        "\nNao foi possivel realizar a operacao."
                );
            }

        } catch (InterruptedException e) {
            // Trata uma possível interrupção durante a comunicação
            Thread.currentThread().interrupt();
            System.out.println("\nRequisicao interrompida.");

        } catch (Exception e) {
            // Trata erros de conexão ou outros problemas na requisição
            System.out.println("\nErro ao acessar a API: "
                    + e.getMessage());
            System.out.println(
                    "Verifique se as VMs e os servicos estao funcionando."
            );
        }
    }

    // MÉTODO PARA CRIAR O JSON
    // Organiza os dados do cliente no formato exigido pela API REST
    private static String criarJson(
            String nome, String email, String telefone) {

        return "{"
                + "\"nome\":\"" + escaparJson(nome) + "\","
                + "\"email\":\"" + escaparJson(email) + "\","
                + "\"telefone\":\"" + escaparJson(telefone) + "\""
                + "}";
    }

    // Trata caracteres especiais para que não prejudiquem a estrutura JSON
    private static String escaparJson(String valor) {

        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    // MÉTODO PARA LER OS CAMPOS
    // Solicita um valor e impede que o usuário deixe o campo vazio
    private static String lerCampo(String mensagem) {

        String valor;

        do {
            System.out.print(mensagem);
            valor = scanner.nextLine().trim();

            if (valor.isBlank()) {
                System.out.println(
                        "Este campo nao pode ficar vazio."
                );
            }

        } while (valor.isBlank());

        return valor;
    }

    // MÉTODO PARA LER O ID
    // Garante que o identificador informado seja um número positivo
    private static long lerId() {

        while (true) {
            System.out.print("Informe o ID do cliente: ");

            try {
                long id = Long.parseLong(scanner.nextLine());

                if (id > 0) {
                    return id;
                }

                System.out.println("O ID deve ser maior que zero.");

            } catch (NumberFormatException e) {
                // Impede que o programa continue com um ID inválido
                System.out.println("Informe um ID numerico valido.");
            }
        }
    }
}

