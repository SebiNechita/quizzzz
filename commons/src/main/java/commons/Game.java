package commons;

import commons.questions.Activity;
import commons.questions.MultipleChoiceQuestion;
import commons.questions.OpenQuestion;
import commons.questions.Question;
import packets.ResponsePacket;

import java.util.ArrayList;
import java.util.List;

public class Game extends ResponsePacket {
    /**
     * List of questions that'll be used in this particular instance of the game
     */
    private List<Question> multipleChoiceQuestions;
    private List<Question> openQuestions;
    /**
     * The number of questions in that game session. Default 20 will be used if not provided.
     */
    private int noOfQuestions;
    private int noOfMultipleChoiceQuestions;
    private int noOfOpenQuestions;
    /**
     * Contains all the activities using which the questions will be made
     */
    private List<Activity> activities;

    /**
     * Default constructor for object mappers
     */
    public Game() {
    }

    /**
     * Constructor for Game that takes in the number of questions in the game and list of activities.
     * It creates a list of questions using those.
     * @param noOfQuestions number of questions in the game
     * @param activities list of activities from which the questions are to be made
     */
    public Game(int noOfQuestions, List<Activity> activities) {
        this.activities = activities;
        this.noOfQuestions = noOfQuestions;
        this.noOfMultipleChoiceQuestions = noOfQuestions * 4 / 5;
        this.noOfOpenQuestions = noOfQuestions - noOfMultipleChoiceQuestions;
        this.openQuestions = generateOpenQuestions(this, noOfOpenQuestions);
        this.multipleChoiceQuestions = generateMultipleChoiceQuestions(this, noOfMultipleChoiceQuestions);
    }

    /**
     * Constructor that takes the list of activities to use to make the game. Using this, it creates a list of 20 (default) questions
     * @param activities list of activities using which the questions are to be made
     */
    public Game(List<Activity> activities) {
        this.activities = activities;
        this.noOfQuestions = 20;
        this.noOfMultipleChoiceQuestions = 16;
        this.noOfOpenQuestions = 4;
        this.openQuestions = generateOpenQuestions(this, noOfOpenQuestions);
        this.multipleChoiceQuestions = generateMultipleChoiceQuestions(this, noOfMultipleChoiceQuestions);
    }

    /**
     * Static method that takes in an instance of Game and no of questions required, and creates a mix of MultipleChoiceQuestion and OpenChoiceQuestion.
     * 4/5th of the game consists of MultipleChoiceQuestion and the remaining 1/5th is OpenQuestion
     * @param game Instance of the game
     * @param noOfMultipleChoiceQuestions number of questions for that game
     * @return A list of "noOfQuestions" questions. 4/5th of them are Multiple Choice Questions and the remaining are Open Questions
     */
    private static List<Question> generateMultipleChoiceQuestions(Game game,
                                                    int noOfMultipleChoiceQuestions) {

        List<Question> multipleChoiceQuestions = new ArrayList<>();

        for (int i = 0; i < noOfMultipleChoiceQuestions; i++) {
            multipleChoiceQuestions.add(
                    MultipleChoiceQuestion.
                            generateMultipleChoiceQuestion(
                                    game.getActivities()
                            )
            );
        }
        return multipleChoiceQuestions;
    }

    /**
     * Static method that takes in an instance of Game and no of questions required, and creates a mix of MultipleChoiceQuestion and OpenChoiceQuestion.
     * 4/5th of the game consists of MultipleChoiceQuestion and the remaining 1/5th is OpenQuestion
     * @param game Instance of the game
     * @param noOfOpenQuestions number of questions for that game
     * @return A list of "noOfQuestions" questions. 4/5th of them are Multiple Choice Questions and the remaining are Open Questions
     */
    private static List<Question> generateOpenQuestions(Game game,
                                                                  int noOfOpenQuestions) {
        List<Question> openQuestions = new ArrayList<>();

        for(int i = 0; i < noOfOpenQuestions; ++i) {
            openQuestions.add(OpenQuestion.
                    generateOpenQuestion(
                            game.getActivities()
                    )
            );
        }

        return openQuestions;
    }



    /**
     * Getter for MC questions
     * @return the list multiple choice questions
     */
    public List<Question> getMultipleChoiceQuestions() {
        return multipleChoiceQuestions;
    }

    /**
     * Getter for open questions
     * @return the list open questions
     */
    public List<Question> getOpenQuestions() {
        return openQuestions;
    }

    /**
     * Getter for the number of MC questions
     * @return the number of multiple choice questions
     */
    public int getNoOfMultipleChoiceQuestions() {
        return noOfMultipleChoiceQuestions;
    }

    /**
     * Getter for the number of open questions
     * @return the number of open questions
     */
    public int getNoOfOpenQuestions() {
        return noOfOpenQuestions;
    }

    /**
     * Getter for the number of questions
     *
     * @return number of questions in the game
     */
    public int getNoOfQuestions() {
        return noOfQuestions;
    }

    /**
     * Getter for the list of activities
     *
     * @return the list containing all the activities used to create the questions
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * Setter for activities
     *
     * @param activities List of activities using which the questions for the game are created
     */
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    /**
     * String representation of the Game
     *
     * @return String representation of the Game
     */
    @Override
    public String toString() {
        return "Game{" +
                "multipleChoiceQuestions=" + multipleChoiceQuestions +
                ", openQuestions=" + openQuestions +
                ", noOfQuestions=" + noOfQuestions +
                ", noOfMultipleChoiceQuestions=" + noOfMultipleChoiceQuestions +
                ", noOfOpenQuestions=" + noOfOpenQuestions +
                ", activities=" + activities +
                '}';
    }
}
