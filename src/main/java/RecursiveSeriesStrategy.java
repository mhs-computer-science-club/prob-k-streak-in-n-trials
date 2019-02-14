import java.util.Map;
import java.util.TreeMap;

public class RecursiveSeriesStrategy implements Strategy
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
     * TODO - CHECK POINT 2
     *
     * @param k The number of successes in a streak required.
     * @param n The number of independent Bernoulli trials.
     * @param probs The results map to store intermediate computations.
     *
     * @return The probability of k successes in a row when performing n trials.
     */
    private double getProbKInNTosses(final int k,
                                     final int n,
                                     final Map<Integer, Double> probs)
    {
        if (k > n)
        {
            return 0.0;
        }
        else
        {
            double sumFrom1Tok = 0.0;

            for (int j = 1; j <= k; ++j)
            {
                //final double kInNMinusJ;
                //
                //if (probs.get(n - j) == null)
                //{
                //    kInNMinusJ = getProbKInNTosses(k, n - j, probs);
                //    probs.put(n-j, kInNMinusJ);
                //}
                //else
                //{
                //    kInNMinusJ = probs.get(n - j);
                //}
                //
                //sumFrom1Tok += Math.pow(p, j - 1) * q * kInNMinusJ;

                final double probKInNMinusJ = probs.computeIfAbsent(
                        n - j,
                        key -> getProbKInNTosses(k, key, probs));
                sumFrom1Tok += Math.pow(p, j - 1) * q * probKInNMinusJ;
            }
            return Math.pow(p, k) + sumFrom1Tok;
        }
    }
}
