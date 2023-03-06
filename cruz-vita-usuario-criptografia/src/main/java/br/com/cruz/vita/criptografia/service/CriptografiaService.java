package br.com.cruz.vita.criptografia.service;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class CriptografiaService {
	

	public String criptografarSenha(String password) {

		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPasswordCharArray(password.toCharArray());
		String senhaCriptografada = cripto.encrypt(password);

		return senhaCriptografada;
	}

<<<<<<< HEAD
	
	public String descriptografarSenha(String password) {
		
		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPasswordCharArray(password.toCharArray());
		String senhaDescriptografada = cripto.decrypt(password);
=======
	public String descriptografarSenha(String senhaCriptografada, String chaveCriptografia) {
>>>>>>> 1ba77a03f15f4e3c457b4d98a62507e23423bca4

		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPassword(chaveCriptografia);
		String senhaDescriptografada = cripto.decrypt(senhaCriptografada);
		return senhaDescriptografada;
		
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
