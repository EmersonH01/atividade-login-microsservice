package br.com.cruz.vita.usuario.model;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Valid
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, name = "usuario", nullable = false)
	@NotBlank(message = "Este campo é obrigatório!")
	//@Email(message = "Insira um e-email válido!")
	private String email;
	
	@NotBlank(message = "Este campo é obrigatório!")
	private String senha;
	
	@NotBlank(message = "Este campo é obrigatório!")
	//@CPF
	private String cpf; 

	@Column(name = "tentativa_login")
	private Integer tentativaLogin;

	@Column(name = "data_inclusao")
	private LocalDateTime dataInclusao;

	@Column(name = "data_ultimo_login")
	private LocalDateTime dataUltimoLogin;

	@Column(name = "data_exclusao")
	private LocalDateTime dataExclusao;

	@Enumerated(EnumType.STRING)
	private StatusUsuarioEnum status;	

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pessoa")
	private Long idPessoa;
	
	
	
	

}
