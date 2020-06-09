package br.com.zup.poc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Venda {

    @ApiModelProperty(value = "Identificação interna única da venda")
    @Id
    @GeneratedValue
    private Integer id;
    
	@ManyToOne
	@JoinColumn(name="carroId")
    private Carro carro;
    
	
    @ApiModelProperty(value = "Nome do vendedor")
    @NotNull
	private String nomeVendedor;
    
    @ApiModelProperty(value = "Id do Vendedor")
    @NotNull
	private Integer idVendedor;
}
