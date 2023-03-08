package br.com.cruz.vita.usuario.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.text.MaskFormatter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.cruz.vita.usuario.dto.ResponseUsuarioDTO;
import br.com.cruz.vita.usuario.dto.UsuarioDTO;
import br.com.cruz.vita.usuario.exception.InvalidCpfException;
import br.com.cruz.vita.usuario.model.StatusUsuarioEnum;
import br.com.cruz.vita.usuario.model.UsuarioModel;
import br.com.cruz.vita.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CriptografiaService cryptoService;

	@Autowired
	private ModelMapper modelMapper;

	/* login do usuario */
	public ResponseEntity<String> login(String email, String senha) {
	    if (!verificarEmail(email)) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail não existente em nosso sistema!");
	    }

	    if (!verificarSenha(senha)) {
	        UsuarioModel usuario = null;
	        int tentativas = usuario.getTentativaLogin() + 1;
	        usuario.setTentativaLogin(tentativas);

	        if (tentativas >= 5) {
	            usuario.setStatus(StatusUsuarioEnum.bloqueado);
	        }

	        usuarioRepository.save(usuario);

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida! Tentativa " + tentativas + " de 5.");
	    }

	    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Usuário logado");
	}







	
	/*
	 * if (optionalUsuario.isPresent()) { UsuarioModel usuarioBanco =
	 * optionalUsuario.get(); String emailDescriptografado =
	 * cryptoService.decrypt(usuarioBanco.getEmail());
	 * 
	 * if (emailDescriptografado.equals(email) &&
	 * usuarioBanco.getSenha().equals(senha)) { usuarioBanco.setTentativaLogin(0);
	 * usuarioRepository.save(usuarioBanco); return
	 * ResponseEntity.ok("Usuário autenticado com sucesso!"); } else { Integer
	 * tentativasFalhas = usuarioBanco.getTentativaLogin() + 1;
	 * usuarioBanco.setTentativaLogin(tentativasFalhas);
	 * usuarioRepository.save(usuarioBanco); if (tentativasFalhas >= 5) {
	 * usuarioBanco.setStatus(StatusUsuarioEnum.bloqueado);
	 * usuarioRepository.save(usuarioBanco); return
	 * ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	 * .body("Usuário bloqueado por muitas tentativas de login!"); } return
	 * ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	 * .body("Email ou senha incorretos! Tentativa " + tentativasFalhas + " de 5.");
	 * } } return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
	 * body("Email não existe na nossa base de dados");
	 */

	public List<UsuarioModel> listarUsuariosDescriptografados() {
		List<UsuarioModel> usuarios = usuarioRepository.findAll();
		for (UsuarioModel usuario : usuarios) {
			String emailDescriptografado = cryptoService.decrypt(usuario.getEmail());
			usuario.setEmail(emailDescriptografado);
		}
		return usuarios;
	}

	/* listar todos os usuarios através do metodo findAll da JpaRepository */
	public List<ResponseUsuarioDTO> listarUsuario() {
		List<UsuarioModel> lista = usuarioRepository.findAll();
		List<ResponseUsuarioDTO> listaResposta = lista.stream()
				.map(user -> modelMapper.map(user, ResponseUsuarioDTO.class)).collect(Collectors.toList());
		return listaResposta;
	}

	/* Busca todos usuarios por email que é passado na Url */
	public String buscarPorEmail(String email) {
		if (verificarEmail(email)) {
			return "usuario não encontrado";
		} else {
			return "Email possui cadastro vinculado com o cpf: " + usuarioRepository.findByEmail(email).get().getCpf();
		}
	}

	/* busca usuarios desativados pela data de exclusao */
	public List<ResponseUsuarioDTO> buscarDesativados() {

		List<UsuarioModel> lista = usuarioRepository.findByDataExclusao();
		List<ResponseUsuarioDTO> listaResposta = lista.stream()
				.map(user -> modelMapper.map(user, ResponseUsuarioDTO.class)).collect(Collectors.toList());

		return listaResposta;
	}

	/* busca usuarios desativados pela data de ativos */
	public List<ResponseUsuarioDTO> buscarAtivados() {

		Stream<UsuarioModel> streamUsuarios = usuarioRepository.findByDataInclusao().stream();
		List<ResponseUsuarioDTO> listaResposta = streamUsuarios
				.map(user -> modelMapper.map(user, ResponseUsuarioDTO.class)).collect(Collectors.toList());
		return listaResposta;
	}

	/* cadastra um novo usuario */
	public ResponseEntity<String> cadastrarUsuario(UsuarioDTO usuario) throws InvalidCpfException {
		if (!verificarEmail(usuario.getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail já existente em nosso sistema!");
		}
		if (!verificarSeCPFJaExiste(usuario.getCpf())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("CPF já existente em nosso sistema!");
		}

		if (!validarCPF(usuario.getCpf())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("CPF inválido!");
		}

		UsuarioModel usuarioNovo = modelMapper.map(usuario, UsuarioModel.class);
		usuarioNovo.setDataInclusao(LocalDateTime.now());
		usuarioNovo.setCpf(formatarCpf(usuario.getCpf()));
		usuarioNovo.setSenha(cryptoService.encrypt(usuario.getSenha()));
		usuarioNovo.setEmail(cryptoService.encrypt(usuario.getEmail()));

		usuarioRepository.save(usuarioNovo);

		return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
	}

	/* cadastra uma lista de usuarios */
	public ResponseEntity<String> cadastrarPorLote(List<UsuarioDTO> usuarios) throws InvalidCpfException {
		List<UsuarioModel> usuariosModel = new ArrayList<>();

		for (UsuarioDTO usuarioDTO : usuarios) {
			if (verificarEmail(usuarioDTO.getEmail()) || verificarSeCPFJaExiste(usuarioDTO.getCpf())
					|| !validarCPF(usuarioDTO.getCpf())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("Não foi possível cadastrar o usuário com CPF: " + formatarCpf(usuarioDTO.getCpf()));
			}

			UsuarioModel usuarioModel = modelMapper.map(usuarioDTO, UsuarioModel.class);
			usuarioModel.setDataInclusao(LocalDateTime.now());
			usuarioModel.setCpf(formatarCpf(usuarioDTO.getCpf()));
			String hashedPassword = cryptoService.encrypt(usuarioDTO.getSenha());
			usuarioModel.setSenha(hashedPassword);
			String hashedEmail = cryptoService.encrypt(usuarioDTO.getEmail());
			usuarioModel.setEmail(hashedEmail);

			usuariosModel.add(usuarioModel);
		}

		usuarioRepository.saveAll(usuariosModel);

		return ResponseEntity.status(HttpStatus.CREATED).body("Lote cadastrado com sucesso");
	}

	/* verifica se o CPF já é existente no banco de dados */
	public Boolean verificarSeCPFJaExiste(String cpf) throws InvalidCpfException {
		return !usuarioRepository.findByCpf(formatarCpf(cpf)).isPresent();
	}

	/* formatar cpf */
	public String formatarCpf(String cpf) throws InvalidCpfException {
		cpf = cpf.replaceAll("\\D", "");
		if (cpf == null || cpf.length() != 11) {
			throw new InvalidCpfException("CPF inválido");
		}

		try {
			MaskFormatter mask = new MaskFormatter("###.###.###-##");
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(cpf);
		} catch (ParseException ep) {
			ep.printStackTrace();
			throw new InvalidCpfException("Erro ao formatar CPF");
		}
	}

	/* valida se o cpf é verdadeiro ou falso */
	public boolean validarCPF(String cpf) {
		CPFValidator cpfValidator = new CPFValidator();
		try {
			cpfValidator.assertValid(cpf);
			return true;
		} catch (InvalidStateException e) {
			return false;
		}
	}

	/* verifica se o email existe em nosso banco de dados */
	public boolean verificarEmail(String email) {
		List<UsuarioModel> usuarios = usuarioRepository.findAll();
		for (UsuarioModel usuario : usuarios) {
			String emailDescriptografado = cryptoService.decrypt(usuario.getEmail());
			if (emailDescriptografado.equals(email)) {
				return true;
			}
		}
		return false;
	}

	/* verifica se a senha existe em nosso banco de dados */
	public boolean verificarSenha(String senha) {
		List<UsuarioModel> usuarios = usuarioRepository.findAll();
		for (UsuarioModel usuario : usuarios) {
			String senhaDescriptografado = cryptoService.decrypt(usuario.getSenha());
			if (senhaDescriptografado.equals(senha)) {
				return true;
			}
		}
		return false;
	}

	/* atualiza o usuario através do email passado e retorna uma mensagem */
	public String atualizarViaEmail(UsuarioDTO usuario, String email) {
		Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(email);
		UsuarioModel usuarioModel = usuarioOptional
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

		usuarioModel.setEmail(cryptoService.encrypt(usuario.getEmail()));
		usuarioModel.setSenha(cryptoService.encrypt(usuario.getSenha()));

		usuarioRepository.save(usuarioModel);

		final String mensagemSucesso = "Usuário vinculado ao CPF %s atualizado com sucesso.";
		return String.format(mensagemSucesso, usuario.getCpf());
	}

	/* exclusão logica de usuario */
	public String deletarPorEmail(String email) throws InvalidCpfException {
		Optional<UsuarioModel> optionalUsuario = usuarioRepository.findByEmail(email);

		if (optionalUsuario.isEmpty()) {
			throw new IllegalArgumentException("Usuário com o email " + email + " não encontrado.");
		}

		UsuarioModel usuario = optionalUsuario.get();
		usuario.setDataExclusao(LocalDateTime.now());
		usuarioRepository.save(usuario);

		return "Usuário vinculado ao CPF " + formatarCpf(usuario.getCpf()) + " deletado com sucesso!";
	}

	/* exclui usuario por id */
	public Boolean excluirUsuario(Long id) {
		usuarioRepository.deleteById(id);
		return true;
	}

	/*
	 * public String autenticar(String email, String senha) {
	 * 
	 * UsuarioModel usuario = usuarioRepository.findByEmail(email);
	 * 
	 * if (usuario == null) { return "Email não existe na nossa base de dados"; }
	 * 
	 * if (usuario.isBloqueado()) { return
	 * "Usuário bloqueado por muitas tentativas de login!"; }
	 * 
	 * if (verificarSenha(senha)) { resetarTentativas(usuario); return
	 * "Usuário autenticado com sucesso!"; } else { incrementarTentativas(usuario);
	 * if (usuario.getTentativaLogin() >= 5) { bloquearUsuario(usuario); return
	 * "Usuário bloqueado por muitas tentativas de login!"; } return
	 * "Senha incorreta! Tentativa " + usuario.getTentativaLogin() + " de 5."; } }
	 */

	private void resetarTentativas(UsuarioModel usuario) {
		usuario.setTentativaLogin(0);
		usuarioRepository.save(usuario);
	}

	private void incrementarTentativas(UsuarioModel usuario) {
		usuario.setTentativaLogin(usuario.getTentativaLogin() + 1);
		usuarioRepository.save(usuario);
	}

	private void bloquearUsuario(UsuarioModel usuario) {
		usuario.setStatus(StatusUsuarioEnum.bloqueado);
		usuarioRepository.save(usuario);
	}

}
