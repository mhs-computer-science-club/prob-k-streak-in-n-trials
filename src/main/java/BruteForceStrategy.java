import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BruteForceStrategy implements Strategy
{
    private final double p = 0.5;
    private final double q = 1.0 - p;

    @Override
    public double getProbKInNTosses(final int k, final int n)
    {
        final List<String> combinations = createCombinations(n);

        double success = 0.0;
        for (final String combination : combinations) {
            if (isSuccess(k, combination)) {
                success += computeProbability(combination);
            }
        }

        return success;

        // Alternative if p = q = 0.5
        //double success = 0.0;
        //for (final String combination : combinations) {
        //    if (isSuccess(k, combination)) {
        //        ++success;
        //    }
        //}
        //
        //return success / combinations.size();
    }

    /**
     * Computes the probability of a given combination.
     *
     * <p>
     * Combinations are encoded as strings of characters 'S' and 'F' denoting
     * success and failure, respectively. Hence, 'SFS' represents a success,
     * followed by a failure, followed by another success. The probability for
     * this example is:
     *
     * <pre>
     * P('SFS') = p * q * p
     * </pre>
     *
     * TODO - CHECK POINT 1
     *
     * @param combination The S/F encoded string of representing the Bernoulli
     *        trial outcome.
     *
     * @return The probability of observing this outcome.
     */
    private double computeProbability(final String combination)
    {
        double probability = 1.0;
        for (int index = 0; index < combination.length(); ++index) {
            probability *= combination.charAt(index) == 'S' ? p : q;
        }
        return probability;
    }

    /**
     * TODO - CHECK POINT 1
     *
     * @param k The number of successes in a streak required.
     * @param combination The S/F encoded string of representing the Bernoulli
     *        trial outcome.
     *
     * @return True if a k-streak of successes is observed in the trial; false
     *         otherwise.
     */
    private boolean isSuccess(final int k, final String combination)
    {
        // Alternative with regular expressions match.
        //return combination.matches(".*[S]{" + k + ",}.*");
        int longestStreak = 0;
        for (int index = 0; index < combination.length(); ++index) {
            if (combination.charAt(index) == 'S') {
                ++longestStreak;
                if (longestStreak >= k) {
                    return true;
                }
            }
            else {
                longestStreak = 0;
            }
        }
        return false;
    }

    /**
     * Creates a list of strings holding all the possible combinations for an
     * n-trial experiment.
     *
     * <p>
     * TODO - CHECK POINT 1
     *
     * @param n The number of independent Bernoulli trials.
     *
     * @return The list containing all 2^n combinations.
     */
    private List<String> createCombinations(final int n)
    {
        if (n <= 0) {
            return Collections.emptyList();
        }
        else if (n == 1) {
            return Arrays.asList("S", "F");
        }
        else {
            final List<String> combinations = createCombinations(n - 1);

            final List<String> newCombinations =
                    new ArrayList<>(combinations.size() * 2);
            for (final String combination : combinations) {
                newCombinations.add("S" + combination);
                newCombinations.add("F" + combination);
            }

            return newCombinations;
        }
    }
}
