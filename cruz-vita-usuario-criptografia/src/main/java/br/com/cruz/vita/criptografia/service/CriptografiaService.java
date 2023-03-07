package br.com.cruz.vita.criptografia.service;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class CriptografiaService {

	/*
	 * public String criptografarSenha(String password) { String hashedPassword =
	 * BCrypt.hashpw(password, BCrypt.gensalt()); return hashedPassword; }
	 */

	private final String chaveSeguranca = "projeto-atrasado";

	public String criptografarSenha(String password) {

		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPasswordCharArray(chaveSeguranca.toCharArray());
		String senhaCriptografada = cripto.encrypt(password);

		return senhaCriptografada;
	}

	public String descriptografar(String passwordCripto) {

		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPasswordCharArray(chaveSeguranca.toCharArray());
		String senhaDecriptografada = cripto.decrypt(passwordCripto);

		return senhaDecriptografada;

	}

}
