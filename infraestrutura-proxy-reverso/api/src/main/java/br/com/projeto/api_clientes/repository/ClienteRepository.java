package br.com.projeto.api_clientes.repository;

import br.com.projeto.api_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

