package br.com.zup.poc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Vendedor {
	
    @ApiModelProperty(value = "Identificação interna única do vendedor")
    @Id
    @GeneratedValue
    private Integer id;

    @ApiModelProperty(value = "Nome do vendedor")
    @NotNull
    private String nome;

}
