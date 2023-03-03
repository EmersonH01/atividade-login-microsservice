package br.com.cruz.vita.criptografia.service;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class CriptografiaService {
	

	public String criptografarSenha(String password) {
		
		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPasswordCharArray(password.toCharArray());
		String senhaCriptografada = cripto.encrypt(password);

		return senhaCriptografada;
	}

	public String descriptografarSenha(String password) {
		
		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPasswordCharArray(password.toCharArray());
		String senhaDescriptografada = cripto.decrypt(password);

		return senhaDescriptografada;
		
		
		

	}

}
