import java.util.Map;
import java.util.TreeMap;

/**
 * Dynamic programming top-down with memoization solution.
 *
 * <p>
 * S(k,n) = p^k + SUM({p^(j-1) * (1-p) * S(k,n-j)} for j = 1 to min(k,n-k))
 */
public class DPTopDownStrategy implements Strategy
{
    private static final double p = 0.5;
    private static final double q = 1.0 - p;

    @Override
    public double getProbKInNTosses(final int k, final int n)
    {
        return getProbKInNTosses(k, n, new TreeMap<>());
    }

    /**
     * Returns the probability [0..1] that we encounter k successes in a row
     * when performing n independent Bernoulli trials.
     *
     * <p>
     * Note that a map is passed around, such that when we get a result from
     * one of the intermediate recursions we save it and when asked for that
     * result again we look it up instead of repeating the recursion. Notice
     * that a term such as S(k, n-4) is needed for S(k, n-3), S(k, n-2), etc.
     *
     * <p>
     * TODO - CHECK POINT 3
     *
     * @param k The number of successes in a streak required.
     * @param n The number of independent Bernoulli trials.
     * @param cache The results map to store intermediate computations.
     *
     * @return The probability of k successes in a row when performing n trials.
     */
    private double getProbKInNTosses(final int k,
                                     final int n,
                                     final Map<Integer, Double> cache)
    {
        return 0.0;
    }
}
