package br.com.zup.poc.jpa;

import org.springframework.data.repository.CrudRepository;

import br.com.zup.poc.entity.Carro;

public interface CarroRepository extends CrudRepository<Carro, Integer> {

}
