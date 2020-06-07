package br.com.zup.poc.jpa;

import org.springframework.data.repository.CrudRepository;

import br.com.zup.poc.entity.Venda;

public interface VendaRepository extends CrudRepository<Venda, Integer> {

}
