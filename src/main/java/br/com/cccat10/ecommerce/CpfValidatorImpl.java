package br.com.cccat10.ecommerce;

import org.springframework.stereotype.Service;

@Service
public class CpfValidatorImpl implements CpfValidator {

    private static final int NUMBER_OF_DIGITS = 11;

    @Override
    public void validate(final String cpf) {
        if (cpf.length() != NUMBER_OF_DIGITS) {
            throw new InvalidCpfException(InvalidCpfException.INCORRECT_QUANTITY_OF_DIGITS);
        }
        int sumFirstVerifierDigit = 0;
        int sumSecondVerifierDigit = 0;

        for (int digitPosition = 1; digitPosition < NUMBER_OF_DIGITS - 1; digitPosition++) {
            int digit = Integer.parseInt(cpf.substring(digitPosition - 1, digitPosition));

            sumFirstVerifierDigit += (NUMBER_OF_DIGITS - digitPosition) * digit;
            sumSecondVerifierDigit += (NUMBER_OF_DIGITS + 1 - digitPosition) * digit;
        }
        int expectedFirstVerifierDigit = calculateVerifierDigit(sumFirstVerifierDigit);

        sumSecondVerifierDigit += 2 * expectedFirstVerifierDigit;
        int expectedSecondVerifierDigit = calculateVerifierDigit(sumSecondVerifierDigit);

        int actualFirstVerifierDigit = Integer.parseInt(cpf.substring(NUMBER_OF_DIGITS - 2, NUMBER_OF_DIGITS - 1));
        int actualSecondVerifierDigit = Integer.parseInt(cpf.substring(NUMBER_OF_DIGITS - 1, NUMBER_OF_DIGITS));

        if (expectedFirstVerifierDigit != actualFirstVerifierDigit ||
                expectedSecondVerifierDigit != actualSecondVerifierDigit) {
            throw new InvalidCpfException(InvalidCpfException.INCORRECT_VERIFIER_DIGIT);
        }
    }

    private int calculateVerifierDigit(int sumVerifierDigit) {
        int digitRemainder = sumVerifierDigit % NUMBER_OF_DIGITS;
        return digitRemainder < 2 ? 0 : NUMBER_OF_DIGITS - digitRemainder;
    }

}
