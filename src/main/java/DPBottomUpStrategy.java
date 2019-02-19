import java.util.TreeMap;

/**
 * Dynamic programming bottom-up solution.
 *
 * <p>
 * S(k,n) = p^k + SUM({p^(j-1) * (1-p) * S(k,n-j)} for j = 1 to min(k,n-k))
 */
public class DPBottomUpStrategy implements Strategy
{
    private static final double p = 0.5;
    private static final double q = 1.0 - p;

    @Override
    public double getProbKInNTosses(final int k, final int n)
    {
        final TreeMap<Integer, Double> cache = new TreeMap<>();

        // TODO - CHECK POINT 4 : Iterate for m=k..n and store in cache.
        for (int m = k; m <= n; ++m) {
            cache.put(m, getProbKInNTosses(k, m, cache));
        }

        // TODO - CHECK POINT 4 : What do we return?
        return 0.0;
    }

    /**
     * Returns the probability [0..1] that we encounter k successes in a row
     * when performing n independent Bernoulli trials.
     *
     * <p>
     * Note that a map with probabilities for smaller terms is expected to be
     * passed in. That is, the map or cache must have values for S(k,m) for m
     * from k to n-1.
     *
     * <p>
     * TODO - CHECK POINT 4
     *
     * @param k The number of successes in a streak required.
     * @param n The number of independent Bernoulli trials.
     * @param cache The results map with intermediate computations stored.
     *
     * @return The probability of k successes in a row when performing n trials.
     */
    private double getProbKInNTosses(final int k,
                                     final int n,
                                     final TreeMap<Integer, Double> cache)
    {
        return 0.0;
    }
}
