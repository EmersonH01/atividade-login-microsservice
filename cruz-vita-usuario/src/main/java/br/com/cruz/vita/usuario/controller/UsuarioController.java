package br.com.cruz.vita.usuario.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cruz.vita.usuario.dto.ResponseUsuarioDTO;
import br.com.cruz.vita.usuario.dto.UsuarioDTO;
import br.com.cruz.vita.usuario.model.UsuarioModel;
import br.com.cruz.vita.usuario.service.CriptografiaService;
import br.com.cruz.vita.usuario.service.SenhaService;
import br.com.cruz.vita.usuario.service.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuario")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private SenhaService senhaService;

	@Value("${ambiente.deploy}")
	private String profile;

	@PostMapping("/login")
	public ResponseEntity<String> autenticar(@RequestBody UsuarioModel usuario) {
		return ResponseEntity.status(401).body(senhaService.autenticar(usuario));
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<ResponseUsuarioDTO>> listarUsuarios() {
		infoAmbiente();
		return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarUsuario());
	}

	@GetMapping("/listar/desativados")
	public ResponseEntity<List<ResponseUsuarioDTO>> buscarDesativado() {
		infoAmbiente();
		return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarDesativados());
	}

	@GetMapping("/listar/ativados")
	public ResponseEntity<List<ResponseUsuarioDTO>> buscarAtivos() {
		infoAmbiente();
		return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarAtivados());
	}

	@GetMapping("/buscar/{email}")
	public ResponseEntity<String> buscarPorEmail(@PathVariable @Valid String email) {
		infoAmbiente();

		return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarPorEmail(email));
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<String> criarUsuario(@RequestBody @Valid UsuarioDTO usuario) {
		infoAmbiente();
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarUsuario(usuario));
	}

	@PostMapping("/criarlote")
	public ResponseEntity<String> criarUsuarioLote(@RequestBody @Valid List<UsuarioDTO> usuario) {
		infoAmbiente();
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarPorLote(usuario).getBody());
	}

	@PutMapping("/atualizar/{email}")
	public ResponseEntity<String> atualizarUsuario(@RequestBody @Valid UsuarioDTO usuario, @PathVariable String email) {
		infoAmbiente();
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.atualizarViaEmail(usuario, email));
	}

	@DeleteMapping("/deletar/{email}")
	public ResponseEntity<String> deletarEmail(@PathVariable @Valid String email) {
		infoAmbiente();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarioService.deletarPorEmail(email));
	}

	public void infoAmbiente() {
		log.info("O ambiente é: " + profile);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
		usuarioService.excluirUsuario(id);
		return ResponseEntity.status(204).build();
	}

}
