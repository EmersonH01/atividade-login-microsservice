package br.com.cruz.vita.criptografia.service;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class CriptografiaService {
<<<<<<< HEAD
	
=======

	/*
	 * public String criptografarSenha(String password) { String hashedPassword =
	 * BCrypt.hashpw(password, BCrypt.gensalt()); return hashedPassword; }
	 */
>>>>>>> refs/heads/release-homologacao

	
	private final String chaveSeguranca = "projeto-atrasado";
	
	public String criptografarSenha(String password) {

		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPasswordCharArray(chaveSeguranca.toCharArray());
		String senhaCriptografada = cripto.encrypt(password);

		return senhaCriptografada;
	}

<<<<<<< HEAD
<<<<<<< HEAD
	
	public String descriptografarSenha(String password) {
		
		BasicTextEncryptor cripto = new BasicTextEncryptor();
		cripto.setPasswordCharArray(password.toCharArray());
		String senhaDescriptografada = cripto.decrypt(password);
=======
	public String descriptografarSenha(String senhaCriptografada, String chaveCriptografia) {
>>>>>>> 1ba77a03f15f4e3c457b4d98a62507e23423bca4
=======
	public String descriptografar(String passwordCripto) {
>>>>>>> refs/heads/release-homologacao

		BasicTextEncryptor cripto = new BasicTextEncryptor();
<<<<<<< HEAD
		cripto.setPassword(chaveCriptografia);
		String senhaDescriptografada = cripto.decrypt(senhaCriptografada);
		return senhaDescriptografada;
		
=======
		cripto.setPasswordCharArray(chaveSeguranca.toCharArray());
		String senhaDecriptografada = cripto.decrypt(passwordCripto);

		return senhaDecriptografada;

>>>>>>> refs/heads/release-homologacao
	}

}
