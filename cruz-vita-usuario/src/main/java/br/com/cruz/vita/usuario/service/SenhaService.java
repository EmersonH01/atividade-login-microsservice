package br.com.cruz.vita.usuario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.cruz.vita.usuario.model.StatusUsuarioEnum;
import br.com.cruz.vita.usuario.model.UsuarioModel;
import br.com.cruz.vita.usuario.repository.UsuarioRepository;


@Service
public class SenhaService {

	
	@Autowired
	private UsuarioRepository usuarioRepository ;
	
	public String autenticar(UsuarioModel usuario) {
	
	if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
		UsuarioModel usuarioBanco = usuarioRepository.findByEmail(usuario.getEmail()).get();
		UsuarioModel usuarioModel = usuario;
		usuarioModel.setId(usuarioBanco.getId());
		usuarioModel.setStatus(usuarioBanco.getStatus());

		if (usuarioModel.getSenha().equals(usuarioBanco.getSenha())) {
			usuarioModel.setNumTentativasFalhas(0);
			usuarioModel.setId(usuarioBanco.getId());
			usuarioModel.setStatus(usuarioBanco.getStatus());
			usuarioModel.setSenha(usuarioBanco.getSenha());
			usuarioRepository.save(usuarioModel);
			return "Usuário autenticado com sucesso!" + ResponseEntity.status(200).body(null) ;

		} else {
			usuarioBanco = usuarioRepository.findByEmail(usuario.getEmail()).get();
			int tentativasFalhas = usuarioBanco.getNumTentativasFalhas();
			usuarioModel.setNumTentativasFalhas(tentativasFalhas + 1);
			usuarioModel.setStatus(usuarioBanco.getStatus());

			usuarioRepository.save(usuarioModel);
			if (tentativasFalhas + 1 >= 5) {
				usuarioModel.setStatus(StatusUsuarioEnum.bloqueado);
				usuarioRepository.save(usuarioModel);
				return "Usuário bloqueado por muitas tentativas de login!";
			}
			return "Senha incorreta! Tentativa " + (tentativasFalhas + 1) + " de 5.";
		}
	}
	return "Email não existe na nossa base de dados";
}
}
	
	

