package br.com.cruz.vita.usuario.exception;

public class InvalidCpfException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCpfException(String message) {
        super(message);
    }
}