package main;

import action.Command;
import action.Query;
import action.Recommend;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        for (ActionInputData action : input.getCommands()) {
            switch (action.getActionType()) {
                case "command":
                    switch (action.getType()) {
                        case "favorite":
                            // noinspection unchecked

                            arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                                                Command.favorite(action.getUsername(),
                                                action.getTitle(), input.getMyVideoDB(), input.getMyUserDB())));
                            break;
                        case "view":
                            // noinspection unchecked

                            arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                                                Command.view(action.getUsername(),
                                                action.getTitle(), input.getMyVideoDB(), input.getMyUserDB())));
                            break;
                        case "rating":
                            if (action.getSeasonNumber() == 0) {
                                // No season given => rate movie
                                // noinspection unchecked

                                arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                                                    Command.rateMovie(action.getUsername(), action.getTitle(),
                                                    action.getGrade(), input.getMyVideoDB(), input.getMyUserDB())));
                            } else {
                                // Season given => rate show
                                // noinspection unchecked

                                arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                                                    Command.rateSeason(action.getUsername(), action.getTitle(),
                                                    action.getSeasonNumber(), action.getGrade(),
                                                    input.getMyVideoDB(), input.getMyUserDB())));
                            }
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + action.getType());
                    }
                    break;
                case "query":
                    switch (action.getObjectType()) {
                        case "actors":
                            switch (action.getCriteria()) {
                                case "average":
                                    // noinspection unchecked

                                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                                                    ("Query result: " + Query.Actors.average(action.getNumber(),
                                                    action.getSortType(), input.getMyVideoDB(), input.getMyActorDB()))));
                                    break;
                                case "awards":
                                    // noinspection unchecked

                                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                                                    ("Query result: " + Query.Actors.awards(action.getSortType(),
                                                    action.getFilters().get(Constants.FILTER_AWARDS_INDEX),
                                                    input.getMyActorDB()))));
                                    break;
                                case "filter_description":
                                    // noinspection unchecked

                                    arrayResult.add((fileWriter.writeFile(action.getActionId(), null,
                                                    ("Query result: " + Query.Actors.description(action.getSortType(),
                                                    action.getFilters().get(Constants.FILTER_WORDS_INDEX),
                                                    input.getMyActorDB())))));
                                    break;
                            }
                            break;
                        case "users":
                            // noinspection unchecked

                            arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                                            ("Query result: " + Query.Users.noRatings(action.getNumber(),
                                            action.getSortType(), input.getMyUserDB()))));
                            break;
                        default:
                            // case "movies" + case "shows"
                            switch (action.getCriteria()) {
                                case "ratings":
                                    break;
                                case "longest":
                                    break;
                                case "favorite":
                                    // noinspection unchecked

                                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                                            ("Query result: " + Query.Videos.favorite(action.getNumber(),
                                                    action.getSortType(), action.getObjectType(),
                                                    action.getFilters().get(Constants.FILTER_YEAR_INDEX),
                                                    action.getFilters().get(Constants.FILTER_GENRE_INDEX),
                                                    input.getMyVideoDB()))));
                                    break;
                                case "most_viewed":
                                    // noinspection unchecked

                                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                                            ("Query result: " + Query.Videos.mostViewed(action.getNumber(),
                                                    action.getSortType(), action.getObjectType(),
                                                    action.getFilters().get(Constants.FILTER_YEAR_INDEX),
                                                    action.getFilters().get(Constants.FILTER_GENRE_INDEX),
                                                    input.getMyVideoDB()))));
                                    break;
                            }
                    }
                    break;
                case "recommendation":
                    switch (action.getType()) {
                        case "standard":
                            break;
                        case "best_unseen":
                            // noinspection unchecked

                            arrayResult.add((fileWriter.writeFile(action.getActionId(), null,
                                            Recommend.Basic.bestUnseen(action.getUsername(),
                                            input.getMyUserDB(), input.getMyVideoDB()))));
                            break;
                        case "popular":
                            break;
                        case "favorite":
                            break;
                    }
                    break;
            }
        }

        fileWriter.closeJSON(arrayResult);
    }
}
