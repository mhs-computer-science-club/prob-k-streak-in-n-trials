public interface Strategy
{
    /**
     * Returns the probability [0..1] that we encounter k successes in a row
     * when performing n independent Bernoulli trials.
     *
     * @param k The number of successes in a streak required.
     * @param n The number of independent Bernoulli trials.
     *
     * @return The probability of k successes in a row when performing n trials.
     */
    double getProbKInNTosses(int k, int n);
}
