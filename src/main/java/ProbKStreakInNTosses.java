import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ProbKStreakInNTosses
{
    private static final CommandLineParser PARSER = new DefaultParser();
    private static final HelpFormatter HELP_FORMATTER = new HelpFormatter();

    private static final Map<String, Strategy> STRATEGIES = new HashMap<>(4);
    static {
        STRATEGIES.put("brute-force", new BruteForceStrategy());
        STRATEGIES.put("recursive", new RecursiveStrategy());
        STRATEGIES.put("dp_memoization", new DPTopDownStrategy());
        STRATEGIES.put("dp_bottom_up", new DPBottomUpStrategy());
    }

    private final Strategy strategy;

    private ProbKStreakInNTosses(final Strategy strategy)
    {
        this.strategy = strategy;
    }

    public static void main(final String[] args)
    {
        final OptionGroup helpOptions = new OptionGroup();
        helpOptions.addOption(Option.builder("h").longOpt("help")
                                      .desc("print this help message")
                                      .build());

        final Options options = new Options();

        options.addOptionGroup(helpOptions);

        final Option strategyOption = Option.builder("s").longOpt("strategy")
                .desc("select the strategy to use: " + STRATEGIES.keySet())
                .hasArg()
                .argName("STRATEGY")
                .build();
        options.addOption(strategyOption);

        final OptionGroup input = new OptionGroup();
        input.addOption(Option.builder("f").longOpt("file")
                                  .desc("specify the input file to use")
                                  .hasArg()
                                  .argName("FILE")
                                  .build());

        input.addOption(Option.builder("k")
                                  .desc("k successes in a row in n trials")
                                  .hasArg()
                                  .argName("K:N")
                                  .build());
        input.setRequired(true);
        options.addOptionGroup(input);

        try {
            if (hasHelpOptions(helpOptions, args)) {
                printHelp(options);
                return;
            }

            final CommandLine cmd = PARSER.parse(options, args);

            if (cmd.hasOption('h')) {
                printHelp(options);
                return;
            }

            final ProbKStreakInNTosses process = new ProbKStreakInNTosses(getStrategy(cmd));

            final String inputFileName = cmd.getOptionValue('f');
            if (inputFileName != null) {
                process.runWithInputData(inputFileName);
            }
            else {
                final String value = cmd.getOptionValue('k');
                final Matcher matcher = Pattern.compile("^(\\d+):(\\d+)$").matcher(value);
                if (!matcher.matches()) {
                    throw new ParseException(String.format("Invalid k: '%s'", value));
                }
                process.runTrial(Integer.valueOf(matcher.group(1)),
                                 Integer.valueOf(matcher.group(2)));
            }
        }
        catch (final ParseException e) {
            System.out.println(e.getMessage());
            printHelp(options);
        }
    }

    private static boolean hasHelpOptions(final OptionGroup helpOptions,
                                          final String[] args)
            throws ParseException
    {
        final Options options = new Options().addOptionGroup(helpOptions);
        final CommandLine commandLine = PARSER.parse(options, args, true);
        return commandLine.getOptions().length > 0;
    }

    private static void printHelp(final Options options) {
        HELP_FORMATTER.printHelp("java -jar prob-k-streak-in-n-tosses.jar",
                                 options,
                                 true);
    }

    private static Strategy getStrategy(final CommandLine cmd)
            throws ParseException
    {
        final String strategy = Optional
                .ofNullable(cmd.getOptionValue('s'))
                .orElse("brute-force");

        if (!STRATEGIES.containsKey(strategy)) {
            throw new ParseException(String.format("Invalid strategy: '%s'", strategy));
        }

        return STRATEGIES.get(strategy);
    }

    private void runWithInputData(final String inputDataFileName)
    {
        try (final Scanner in = new Scanner(new File(inputDataFileName)))
        {
            final int numberOfTests = in.nextInt();

            for (int index = 0; index < numberOfTests; ++index) {
                runTrial(in.nextInt(), in.nextInt());
            }
        }
        catch (final FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void runTrial(final int k, final int n) {
        final double probKInNTosses = strategy.getProbKInNTosses(k, n);
        System.out.println(String.format("%.2f%%", probKInNTosses * 100));
    }
}
