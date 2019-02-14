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

## Simple Examples

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

To solve this problem we may be tempted to use brute force and it will solve
the problem for small number of trials.

For instance, if we can do the following:

1. Create a list of strings that holds all possible outcomes.
   ```
   For n = 3:
     FFF FFS FSF FSS
     SFF SFS SSF SSS

   For n = 4:
     FFFF FFFS FFSF FFSS FSFF FSFS FSSF FSSS
     SFFF SFFS SFSF SFSS SSFF SSFS SSSF SSSS
   ```
2. Compute the probability of a successful event (i.e., observing the k-streak).
   ```
   For p = 0.5, n = 4, and k = 3,
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

$ java -cp build/libs/prob-k-streak-in-n-tosses.jar \
    ProbKStreakInNTosses build/resources/main/monsterLine.dat "brute-force"
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

$ java -cp build/libs/prob-k-streak-in-n-tosses.jar \
    ProbKStreakInNTosses build/resources/main/monsterLine.dat "brute-force"
50.00%
59.38%
25.00%
93.75%
```

## Issues with Brute Force

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

## Solution: Recursive Series Solution

***Note: Maybe this can be considered a Dynamic Programming algorithm?***

Let `S(k,n)` be the probability of observing a k-streak success in n Bernoulli
trials.

The probability of observing a k-success streak in n trials can be recursively
decomposed along the first failure. We can compute the probability as follows:

1. The probability of observing k-successes in a row in the first `k` trials;
   that is, the first failure occurs after `k` trials:
   ```
   p^k
   ```
2. Plus the probability of observing the first failure in the 1st trial times
   the probability of observing a k-streak in `n-1` trials:
   ```
   (1-p) * S(k, n - 1)
   ```
3. Plus the probability of observing the first failure in the 2nd trial times
   the probability of observing a k-streak in `n-2` trials:
   ```
   p * (1-p) * S(k, n - 2)
   ```
4. Plus the probability of observing the first failure in the 3nd trial times
   the probability of observing a k-streak in `n-3` trials:
   ```
   p^2 * (1-p) * S(k, n - 3)
   ```
5. ...
6. Plus the probability of observing the first failure in the jth trial times
   the probability of observing a k-streak in `n-j` trials:
   ```
   p^(j-1) * (1-p) * S(k, n - j)
   ```

Repeating until `j=k` since if the first failure occurs after k trials, then
that means we already succeeded and that probability is accounted for in the
first component.

Note that as the problem grows smaller we reach the boundary conditions:

```
S(k, 0) = 0
S(k, n) = 0 for k > n
```

##### CHECK POINT 2

Complete the **CHECK POINT 2** methods in the RecursiveSeriesStrategy class
given in the starter code.

Before completing you will see the following output:

```
$ ./gradlew build -x test

BUILD SUCCESSFUL in 0s

$ ./gradlew test --tests ProbKStreakInNTossesTest.testRecursiveSeriesStrategy

> Task :test FAILED

ProbKStreakInNTossesTest > testRecursiveSeriesStrategy(int, int, double)[1] FAILED
    org.opentest4j.AssertionFailedError at ProbKStreakInNTossesTest.java:39
...
ProbKStreakInNTossesTest > testRecursiveSeriesStrategy(int, int, double)[20] FAILED
    org.opentest4j.AssertionFailedError at ProbKStreakInNTossesTest.java:39

21 tests completed, 19 failed
...
BUILD FAILED in 1s

$ java -cp build/libs/prob-k-streak-in-n-tosses.jar \
    ProbKStreakInNTosses build/resources/main/monsterLine.dat
0.00%
0.00%
0.00%
0.00%
```

If successfully completed, you should obtain the following results:

```
$ ./gradlew build -x test

BUILD SUCCESSFUL in 0s

$ ./gradlew test --tests ProbKStreakInNTossesTest.testRecursiveSeriesStrategy

BUILD SUCCESSFUL in 1s

$ java -cp build/libs/prob-k-streak-in-n-tosses.jar \
    ProbKStreakInNTosses build/resources/main/monsterLine.dat
50.00%
59.38%
25.00%
93.75%
```
