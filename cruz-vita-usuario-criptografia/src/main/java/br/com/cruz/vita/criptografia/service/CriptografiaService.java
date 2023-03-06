package br.com.cruz.vita.criptografia.service;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CriptografiaService {

//	public String criptografarSenha(String password) {
//		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//		return hashedPassword;	
//	}

	@Autowired 
	private Environment environment;
	
	@Value("${jasypt.encryptor.password}")
	private String minhaChave;


	public String criptografar(String password) {

		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPasswordCharArray(password.toCharArray());
		String senhaCriptografada = cripto.encrypt(password);

		return senhaCriptografada;
	}

	public String descriptografar(String criptografado) {

		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	    textEncryptor.setPassword(environment.getProperty(minhaChave));
	    return textEncryptor.decrypt(criptografado);

	}

	/*
	 * public String criptografarSenha(String password) {
	 * 
	 * BasicTextEncryptor cripto = new BasicTextEncryptor();
	 * cripto.setPasswordCharArray(password.toCharArray()); String
	 * senhaCriptografada = cripto.encrypt(password);
	 * 
	 * return senhaCriptografada; }
	 * 
	 * public String descriptografarSenha(String password) {
	 * 
	 * BasicTextEncryptor cripto = new BasicTextEncryptor();
	 * cripto.setPasswordCharArray(password.toCharArray()); String
	 * senhaDescriptografada = cripto.decrypt(password);
	 * 
	 * return senhaDescriptografada;
	 * 
	 * }
	 */
}
