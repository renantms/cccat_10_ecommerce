package br.com.cccat10.ecommerce;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CpfValidatorImplTest {

    private final CpfValidator cpfValidator = new CpfValidatorImpl();

    @Test
    void shouldValidateThatCpfIsValid() {
         String cpf = "11144477735";

         Assertions.assertDoesNotThrow(() -> cpfValidator.validate(cpf));
    }

    @Test
    void shouldValidateThatCpfIsInvalid() {
        String cpf = "11144477705";

        Assertions.assertThrows(InvalidCpfException.class, () -> cpfValidator.validate(cpf),
                InvalidCpfException.INCORRECT_VERIFIER_DIGIT);
    }

    @Test
    void shouldValidateThatCpfWithIncorrectNumberOfDigitsIsInvalid() {
        String cpf = "111444777051";

        Assertions.assertThrows(InvalidCpfException.class, () -> cpfValidator.validate(cpf),
                InvalidCpfException.INCORRECT_QUANTITY_OF_DIGITS);
    }

    @Test
    void shouldValidateThatCpfWithMaskIsInvalid() {
        String cpf = "111.444.777-35";

        Assertions.assertThrows(InvalidCpfException.class, () -> cpfValidator.validate(cpf),
                InvalidCpfException.INCORRECT_QUANTITY_OF_DIGITS);
    }

}
