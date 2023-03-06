package br.com.cruz.vita.criptografia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cruz.vita.criptografia.service.CriptografiaService;

@RestController
public class CriptografiaController {

	@Autowired
	private CriptografiaService criptografiaService;


//	@PostMapping("/encrypt")
//	public ResponseEntity<String> encryptPassword(@RequestBody String password) {
//
//		return ResponseEntity.status(HttpStatus.ACCEPTED).body(criptografiaService.criptografarSenha(password));
//	}

	@PostMapping("/encrypt")
	public ResponseEntity<String> encryptPassword(@RequestBody String password) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(criptografiaService.criptografarSenha(password));
	}
	
	@GetMapping("/decrypt/{passwordencrypto}")
	public ResponseEntity<String> decryptPassword(String passwordencrypto ) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(criptografiaService.descriptografar(passwordencrypto));
	}
	
	@PostMapping("/teste")
	public String testeDeMesa(@RequestBody String senha) {
		
		String decryptPassword = criptografiaService.descriptografar(senha);
		
		return decryptPassword;
		
	}

}
