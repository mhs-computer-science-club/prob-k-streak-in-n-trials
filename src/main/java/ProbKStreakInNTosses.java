import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProbKStreakInNTosses
{
    private final Strategy strategy;

    private ProbKStreakInNTosses(final Strategy strategy)
    {
        this.strategy = strategy;
    }

    public static void main(final String[] args)
    {
        final Strategy strategy = args.length == 2 && args[1].equals("brute-force")
                ? new BruteForceStrategy()
                : new RecursiveSeriesStrategy();

        final ProbKStreakInNTosses probKStreakInNTosses = new ProbKStreakInNTosses(strategy);

        probKStreakInNTosses.runWithInputData(args[0]);
    }

    private void runWithInputData(final String inputDataFileName)
    {
        try (final Scanner in = new Scanner(new File(inputDataFileName)))
        {
            final int numberOfTests = in.nextInt();

            for (int index = 0; index < numberOfTests; ++index) {
                final double probKInNTosses =
                        strategy.getProbKInNTosses(in.nextInt(), in.nextInt());
                System.out.println(String.format("%.2f%%", probKInNTosses * 100));
            }
        }
        catch (final FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
