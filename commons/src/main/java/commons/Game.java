package commons;

import commons.questions.Activity;
import commons.questions.MultipleChoiceQuestion;
import commons.questions.OpenQuestion;
import commons.questions.Question;

import java.util.ArrayList;
import java.util.List;

public class Game {
    /**
     * List of questions that'll be used in this particular instance of the game
     */
    private final List<Question> questions;
    /**
     * The number of questions in that game session. Default 20 will be used if not provided.
     */
    private final int noOfQuestions;
    /**
     * Contains all the activities using which the questions will be made
     */
    private List<Activity> activities;

    /**
     * Constructor for Game that takes in the number of questions in the game and list of activities.
     * It creates a list of questions using those.
     * @param noOfQuestions number of questions in the game
     * @param activities list of activities from which the questions are to be made
     */
    public Game(int noOfQuestions, List<Activity> activities) {
        this.activities = activities;
        this.noOfQuestions = noOfQuestions;
        this.questions = generateQuestions(this, noOfQuestions);
    }

    /**
     * Constructor that takes the list of activities to use to make the game. Using this, it creates a list of 20 (default) questions
     * @param activities list of activities using which the questions are to be made
     */
    public Game(List<Activity> activities) {
        this.activities = activities;
        this.noOfQuestions = 20;
        this.questions = generateQuestions(this, noOfQuestions);
    }

    /**
     * Static method that takes in an instance of Game and no of questions required, and creates a mix of MultipleChoiceQuestion and OpenChoiceQuestion.
     * 4/5th of the game consists of MultipleChoiceQuestion and the remaining 1/5th is OpenQuestion
     * @param game Instance of the game
     * @param noOfQuestions number of questions for that game
     * @return A list of "noOfQuestions" questions. 4/5th of them are Multiple Choice Questions and the remaining are Open Questions
     */
    private static List<Question> generateQuestions(Game game,
                                                    int noOfQuestions) {

        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < noOfQuestions; i++) {
            if (i%4 != 0) {
                questions.add(
                        MultipleChoiceQuestion.
                                generateMultipleChoiceQuestion(
                                        game.getActivities()
                                )
                );
            } else {
                questions.add(OpenQuestion.
                        generateOpenQuestion(
                                game.getActivities()
                        )
                );
            }
        }
        return questions;
    }

    /**
     * Getter for questions
     *
     * @return list containing the questions in the game
     */
    public List<Question> getQuestions() {
        return questions;
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
     * @return String representation of the Game
     */
    @Override
    public String toString() {
        return "Game{" +
                "questions=" + questions +
                ", noOfQuestions=" + noOfQuestions +
                '}';
    }
}
