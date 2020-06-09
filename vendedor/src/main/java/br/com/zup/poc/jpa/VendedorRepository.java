package br.com.zup.poc.jpa;

import org.springframework.data.repository.CrudRepository;

import br.com.zup.poc.entity.Vendedor;

public interface VendedorRepository extends CrudRepository<Vendedor, Integer> {

}
