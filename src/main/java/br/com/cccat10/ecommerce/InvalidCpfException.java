package br.com.cccat10.ecommerce;

public class InvalidCpfException extends RuntimeException {
    public static final String INCORRECT_QUANTITY_OF_DIGITS = "CPF com quantidade incorreta de dígitos";
    public static final String INCORRECT_VERIFIER_DIGIT = "CPF possui dígitos verificadores incorretos";

    public InvalidCpfException(String message) {
        super(message);
    }
}
