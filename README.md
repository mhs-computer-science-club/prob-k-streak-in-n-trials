# Introduction

In statistics, a **Bernoulli trial** (or **binomial trial**) is a random
experiment with exactly two possible outcomes, **success** and **failure**, in
which the probability of success is the same every time the experiment is
conducted.

See [Bernoulli Trial](https://en.wikipedia.org/wiki/Bernoulli_trial) for more
details.

The toss of a *fair* coin is a common example of a Bernoulli trial where the
probability of observing a *head* is 0.5 or 50% and it doesn't vary from one
trial to the next.

In a coin toss problem the probability of a success (i.e., observing *head*) and
the probability of a failure (i.e., observing *tail*) is the same. However, that
is not always the case in a Bernoulli trial. In general, we denote as:

- `p` - the probability of success
- `q` - the probability of failure

Note that the probability that we either get a success or a failure is 1, since
it spans the full range of possibilities. Hence,

```
p + q = 1
```

or

```
q = 1 - p
```

### Some Simple Examples

1. What is the probability that we get two successes in two Bernoulli trials?

   The possibilities are FF, FS, SF, SS (where S is a success and F is a
   failure). Hence, the probability of two successes is 1 in 4 or .25 if we
   assume a probability of success of 0.5. More specifically, the probability
   can be computed as:
   ```
   P(two successes in two attempts) = p*p = p^2
   For p=0.5, p^2 = 0.25.
   For p=0.75, p^2 = 0.5625
   ```
2. What is the probability that we get two failures in two Bernoulli trials? 
   ```
   P(two failures in two attempts) = q*q = q^2
   For p=0.5, q^2 = 0.25.
   For p=0.75, q^2 = 0.25^2 = 0.0625
   ```
3. What is the probability that we get one and only one success in two Bernoulli
trials?

   Note that the probability of observing FS or SF is 2 in 4 or 0.5.
   ```
   P(one and only one success in two attempts) = p*q + q*p = 2pq
   For p=0.5, 2pq = 0.5.
   For p=0.75, 2pq = 2 * 0.75 * 0.25 = 0.375
   ```

## Problem

For a given number of **Bernoulli trials**, `n`, what is the probability of
observing `k` successes in a row?

Take a look at the competition problem sample handed out. Does it relate to
this problem? Can it be posed as the same problem?

## Solution: Brute Force

To solve this problem we may be tempted to use **brute force** and it will
solve the problem for small number of trials.

For instance, we can do the following:

1. Create a list of strings that holds all possible outcomes.
   ```
   For instance, these are the resulting lists for n = 3 and n = 4.

   For n = 3:
     FFF FFS FSF FSS
     SFF SFS SSF SSS

   For n = 4:
     FFFF FFFS FFSF FFSS FSFF FSFS FSSF FSSS
     SFFF SFFS SFSF SFSS SSFF SSFS SSSF SSSS
   ```
2. Compute the probability of a successful event (i.e., observing the k-streak).
   ```
   For p = 0.5 (i.e., a fair coin), n = 4, and k = 3,
     There are 3 successesful events (FSSS, SSSF, and SSSS).
     P(k=3, n=4) = 3 / 16

   For arbitrary p, n = 4, and k = 3,
     P(k=3, n=4) = qppp + pppq + pppp
   ```

##### CHECK POINT 1

Complete the **CHECK POINT 1** methods in the BruteForceStrategy class given in
the starter code.

Before completing you will see the following output:

```
$ ./gradlew build -x test

BUILD SUCCESSFUL in 0s

$ ./gradlew test --tests ProbKStreakInNTossesTest.testBruteForceStrategy

> Task :test FAILED

ProbKStreakInNTossesTest > testBruteForceStrategy(int, int, double)[1] FAILED
    org.opentest4j.AssertionFailedError at ProbKStreakInNTossesTest.java:23
...
ProbKStreakInNTossesTest > testBruteForceStrategy(int, int, double)[12] FAILED
    org.opentest4j.AssertionFailedError at ProbKStreakInNTossesTest.java:23

12 tests completed, 12 failed
...
BUILD FAILED in 1s

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar \
       -f build/resources/main/monsterLine.dat
0.00%
0.00%
0.00%
0.00%
```

If successfully completed, you should obtain the following results:

```
$ ./gradlew build -x test

BUILD SUCCESSFUL in 0s

$ ./gradlew test --tests ProbKStreakInNTossesTest.testBruteForceStrategy

BUILD SUCCESSFUL in 1s

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar \
       -s brute-force \
       -f build/resources/main/monsterLine.dat
50.00%
59.38%
25.00%
93.75%
```

You can also run a single trial as follows:

```
# Probability of 2 successes in 4 trials:
$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -k 2:4
50.00%

# Probability of 4 successes in 8 trials:
$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -k 4:8
18.75%
```

### Issues with Brute Force

If we constrain the problem to relatively small `n` values (e.g., about 20), the
probability can be computed pretty quickly. However, the solution doesn't scale
very well...

Let's take a look. For a quick estimation we can assume that the list of
combinations alone requires:

```
8 bits per character * n characters * 2^n combinations / 8x10^12 bits in a TB

For n = 20: 8 * 20 * 2^20 / 8e+12 ≃ 0.00002 TB
For n = 30: 8 * 20 * 2^20 / 8e+12 ≃ 0.03 TB
For n = 40: 8 * 20 * 2^20 / 8e+12 ≃ 44 TB
For n = 50: 8 * 20 * 2^20 / 8e+12 ≃ 56 thousand TB
For n = 60: 8 * 20 * 2^20 / 8e+12 ≃ 70 million TB
```

Good luck finding 44 TB of hard drive for your laptop, let alone RAM... ;-)

Now this isn't very exact and it certainly doesn't account for the many other
things consuming memory--like the recursive calls that you may be using to
build the combination list... However, it gives you a good picture of why your
computer will start making noises and heat up when you give it a trial with `n`
larger than 20.

Note that we could get creative and not store a list of combinations and
compute the probability as we go and discard the combination (avoiding
recursive algorithms) and use a more compact representation (e.g., a long value
can hold 2^63 bits and each bit can represent a Bernoulli trial). However, the
sheer number of computations is too much for even a reasonably small number of
`n`.

Try progressively increasing `n` and see how the time exponentially increases:

```
$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s brute-force -k 4:10
24.51%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s brute-force -k 4:15
37.23%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s brute-force -k 4:20
47.80%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s brute-force -k 4:21
49.69%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s brute-force -k 4:22
51.51%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s brute-force -k 4:23
53.27%
...
```

At what point does it start to take longer than 1 minute?

## Solution: Recursive Solution

Let `S(k,n)` be the probability of observing a k-streak success in n Bernoulli
trials.

We want to find a **recursive solution** where we express the solution of
`S(k,n)` in terms of a smaller one. We can achieve this if we break up the
probability terms along the first failure.

For instance, the probability of observing the first failure in the jth trial
can be computed as:

```
For j > k: p^k

For j <= k:
- observing the first failure in the 1st trial p^0 * (1-p) * S(k,n-1)
- observing the first failure in the 2nd trial p^1 * (1-p) * S(k,n-2)
- observing the first failure in the 3rd trial p^2 * (1-p) * S(k,n-3)
- observing the first failure in the 4th trial p^3 * (1-p) * S(k,n-4)
- observing the first failure in the 5th trial p^4 * (1-p) * S(k,n-5)
- ...
- observing the first failure in the jth trial p^(j-1) * (1-p) * S(k,n-j)
```

Hence, we can compute `S(k,n)` as the sum of these terms:

```
S(k,n) = p^k + SUM({p^(j-1) * (1-p) * S(k,n-j)} for j = 1 to k)
```

Note that as the problem grows smaller we reach the boundary condition:

```
S(k,n) = 0 for k > n

In terms of the jth term:
  S(k,n-j) = 0 for k > n-j
  -- or --
  S(k,n-j) = 0 for j > n-k
```

We can use a conditional expression to bail out of the recursion if the
boundary condition is met or we can simply limit the iteration in the math
formula directly:

```
S(k,n) = p^k + SUM({p^(j-1) * (1-p) * S(k,n-j)} for j = 1 to min(k,n-k))
```

##### CHECK POINT 2

Complete the **CHECK POINT 2** methods in the RecursiveStrategy class given in
the starter code.

There should be failing tests which should be corrected after successfully
completing the check point. To run and test do:

```
$ ./gradlew test --tests ProbKStreakInNTossesTest.testRecursiveStrategy
...
$ java -jar build/libs/prob-k-streak-in-n-tosses.jar \
       -s recursive \
       -f build/resources/main/monsterLine.dat
50.00%
59.38%
25.00%
93.75%
```

Try again to progressively increase `n`:

```
$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s recursive -k 4:10
24.51%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s recursive -k 4:15
37.23%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s recursive -k 4:20
47.80%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s recursive -k 4:21
49.69%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s recursive -k 4:22
51.51%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s recursive -k 4:23
53.27%
...
```

At what point does it start to take longer than 1 minute? How does it compare
to the brute force strategy?

## Dynamic Programming

Although the recursive solution is better than the brute force approach, it
still doesn't scale very well. To improve on this we can use **dynamic
programming (DP)**, which in general uses a **time-memory trade-off** to
increase the performance of a recursive algorithm. For instance, an
exponential-time solution may be transformed into a polynomial-time solution.

There are two DP techniques:

- *Top-down with memoization*: Optimizes the recursive solution by saving the
  result of each sub-problem--typically in an array or hash table--to avoid the
  recursion when the value has already been computed.

- *Bottom-up*: Essentially unrolls the recursion by solving the sub-problems in
  order of size, such that when a particular sub-problem is seen for the first
  time all the smaller sub-problems it relies on have been computed already;
  hence, eliminating the need for stack recursion.

## Solution: DP Top-Down with Memoization

In this solution, we will pass a `TreeMap` into the recursive call to cache
computations and use them if they have already been previously computed.

##### CHECK POINT 3

Complete the **CHECK POINT 3** methods in the DPTopDownStrategy class given in
the starter code.

The following recursive loop of the RecursiveStrategy:

```java
for (int j = 1; j <= Math.min(k, n-k); ++j) {
    sum += Math.pow(p, j - 1) * q * getProbKInNTosses(k, n - j);
}
```

will be transformed to something like:

```java
for (int j = 1; j <= Math.min(k, n-k); ++j)
{
    final double subTerm;

    if (cache.containsKey(n - j))
    {
        subTerm = cache.get(n - j);
    }
    else
    {
        subTerm = getProbKInNTosses(k, n - j, cache);
        cache.put(n - j, subTerm);
    }

    sum += Math.pow(p, j - 1) * q * subTerm;
}
```

There should be failing tests which should be corrected after successfully
completing the check point. To run and test do:

```
$ ./gradlew test --tests ProbKStreakInNTossesTest.testDPTopDownStrategy
...
$ java -jar build/libs/prob-k-streak-in-n-tosses.jar \
       -s dp_memoization \
       -f build/resources/main/monsterLine.dat
50.00%
59.38%
25.00%
93.75%
```

Try again to progressively increase `n`:

```
$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s dp_memoization -k 4:100
97.27%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s dp_memoization -k 4:1000
100.00%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s dp_memoization -k 4:4500
100.00%
...
```

At what point does it start to take longer than 1 minute? How does it compare
to the other strategies?

## Solution: DP Bottom-Up

In this solution, we aim to solve problems in order from smaller to bigger.

The smallest `S(k,m)` term that we have to compute is for `n=k` since for
smaller `n` the term is 0. Hence, we can replace the recursion with an
iteration that computes and caches all `S(k,m)` values for `m = k to n`. Each
term with increasing `m` will only depend on a terms with smaller `m`.

##### CHECK POINT 4

Complete the **CHECK POINT 4** methods in the DPBottomUpStrategy class given in
the starter code.

The recursive loop can now be changed to an iteration that only depends on
calling the cache:

```java
for (int j = 1; j <= Math.min(k, n-k); ++j) {
    sum += Math.pow(p, j - 1) * q * cache.get(n - j);
}
```

There should be failing tests which should be corrected after successfully
completing the check point. To run and test do:

```
$ ./gradlew test --tests ProbKStreakInNTossesTest.testDPBottomUpStrategy
...
$ java -jar build/libs/prob-k-streak-in-n-tosses.jar \
       -s dp_bottom_up \
       -f build/resources/main/monsterLine.dat
50.00%
59.38%
25.00%
93.75%
```

Try again to progressively increase `n`:

```
$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s dp_bottom_up -k 4:100
97.27%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s dp_bottom_up -k 4:1000
100.00%

$ java -jar build/libs/prob-k-streak-in-n-tosses.jar -s dp_bottom_up -k 4:1000000
100.00%
...
```

At what point does it start to take longer than 1 minute? How does it compare
to the other strategies?

## Closing Thoughts

Note that DP does not always improve performance. For example, if a recursive
solution never requires the solution of a sub-problem more than once, then with
a memoized approach we are increasing the space-complexity without reaping any
benefit in time-complexity since we still recurse the same number of times.

Both approaches generally lead to the same asymptotic complexity, but the
bottom-up approach usually leads to much better constant factors since there is
no recursion (i.e., reduced overhead on the call stack). However, it is not
always easy to find the smaller sub-problems to solve.

As a rule of thumb, you can try to approach the solution of recursive problems
in the following steps:

1. Find a *recursive solution*.
2. If it doesn't perform well for the bounds of your problem, try to optimize
   the solution with a *DP top-down with memoization* strategy.
3. If it still doesn't perform well for the bounds of your problem, try to
   optimize the solution with a *DP bottom-up* strategy.
