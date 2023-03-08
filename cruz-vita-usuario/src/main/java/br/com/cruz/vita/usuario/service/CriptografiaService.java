package br.com.cruz.vita.usuario.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "cruz-vita-usuario-criptografia", url = "http://localhost:8080")
public interface CriptografiaService {

	@PostMapping("/encrypt")
	public String encrypt(String password);

	@GetMapping("/decrypt/{passwordencrypto}")
    public String decrypt(@PathVariable("passwordencrypto") String passwordencrypto);

}
