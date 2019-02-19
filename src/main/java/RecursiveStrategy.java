/**
 * Recursive solution.
 *
 * <p>
 * S(k,n) = p^k + SUM({p^(j-1) * (1-p) * S(k,n-j)} for j = 1 to min(k,n-k))
 */
public class RecursiveStrategy implements Strategy
{
    private static final double p = 0.5;
    private static final double q = 1.0 - p;

    /**
     * Returns the probability [0..1] that we encounter k successes in a row
     * when performing n independent Bernoulli trials.
     *
     * <p>
     * TODO - CHECK POINT 2
     *
     * @param k The number of successes in a streak required.
     * @param n The number of independent Bernoulli trials.
     *
     * @return The probability of k successes in a row when performing n trials.
     */
    @Override
    public double getProbKInNTosses(final int k, final int n)
    {
        double sum = Math.pow(p, k);

        for (int j = 1; j <= Math.min(k, n-k); ++j) {
            sum += Math.pow(p, j - 1) * q * getProbKInNTosses(k, n - j);
        }

        return sum;
    }
}
