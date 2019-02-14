import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProbKStreakInNTossesTest
{
    @ParameterizedTest
    @MethodSource("probKInNProviderSmall")
    void testBruteForceStrategy(final int k, final int n, final double expected)
    {
        final Strategy strategy = new BruteForceStrategy();

        final double probKInNTosses = assertTimeoutPreemptively(
                Duration.ofSeconds(2),
                () -> strategy.getProbKInNTosses(k, n));

        assertEquals(
                expected,
                Math.round(probKInNTosses * 10000) / 100.0,
                String.format("Probability of %s streak in %s tosses.", k, n));
    }

    @ParameterizedTest
    @MethodSource({"probKInNProviderSmall", "probKInNProviderLarge"})
    void testRecursiveSeriesStrategy(final int k, final int n, final double expected)
    {
        final Strategy strategy = new RecursiveSeriesStrategy();

        final double probKInNTosses = assertTimeoutPreemptively(
                Duration.ofSeconds(2),
                () -> strategy.getProbKInNTosses(k, n));

        assertEquals(
                expected,
                Math.round(probKInNTosses * 10000) / 100.0,
                String.format("Probability of %s streak in %s tosses.", k, n));
    }

    private static Stream<Arguments> probKInNProviderSmall()
    {
        return Stream.of(
                Arguments.of(1, 1, 50.00),
                Arguments.of(1, 2, 75.00),
                Arguments.of(2, 2, 25.00),
                Arguments.of(1, 3, 87.50),
                Arguments.of(2, 3, 37.50),
                Arguments.of(3, 3, 12.50),
                Arguments.of(1, 4, 93.75),
                Arguments.of(2, 4, 50.00),
                Arguments.of(3, 4, 18.75),
                Arguments.of(4, 4,  6.25),
                Arguments.of(2, 5, 59.38),
                Arguments.of(3, 5, 25.00));
    }

    private static Stream<Arguments> probKInNProviderLarge()
    {
        return Stream.of(
                Arguments.of(4, 23, 53.27),
                Arguments.of(63, 63, 0.00),
                Arguments.of(1, 63, 100.00),
                Arguments.of(7, 40, 13.10),
                Arguments.of(2, 42, 99.98),
                Arguments.of(2, 47, 99.99),
                Arguments.of(2, 48, 100.00),
                Arguments.of(18, 63, 0.01),
                Arguments.of(19, 63, 0.00));
    }
}
