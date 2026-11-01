
package br.com.projeto.api_clientes.service;

import br.com.projeto.api_clientes.model.Cliente;
import br.com.projeto.api_clientes.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> atualizar(Long id, Cliente dadosAtualizados) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(dadosAtualizados.getNome());
                    cliente.setEmail(dadosAtualizados.getEmail());
                    cliente.setTelefone(dadosAtualizados.getTelefone());

                    return clienteRepository.save(cliente);
                });
    }

    public boolean excluir(Long id) {
        if (!clienteRepository.existsById(id)) {
            return false;
        }

        clienteRepository.deleteById(id);
        return true;
    }
}
