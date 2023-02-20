package br.com.cccat10.ecommerce.exception;

public class InvalidQuantityException extends RuntimeException {

    public static final String ERROR_MESSAGE = "Número inválido para a quantidade de items";

    public InvalidQuantityException() {
        super(ERROR_MESSAGE);
    }
}
