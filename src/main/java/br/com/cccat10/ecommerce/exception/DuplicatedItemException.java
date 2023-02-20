package br.com.cccat10.ecommerce.exception;

public class DuplicatedItemException extends RuntimeException {

    public static final String ERROR_MESSAGE = "Não é possível ter dois items para o mesmo produto";

    public DuplicatedItemException() {
        super(ERROR_MESSAGE);
    }

}
