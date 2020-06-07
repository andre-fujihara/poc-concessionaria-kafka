package br.com.zup.poc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
    
    @ManyToOne
    @JoinColumn(name="vendedorId")
    private Vendedor vendedor;
}
