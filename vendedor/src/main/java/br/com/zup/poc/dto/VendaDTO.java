package br.com.zup.poc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VendaDTO {

    private Integer id;
    private CarroDTO carro;
	private String nomeVendedor;
	private Integer idVendedor;
}
