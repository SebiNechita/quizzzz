package commons.questions;

import com.fasterxml.jackson.annotation.JsonSubTypes;


@JsonSubTypes({
    @JsonSubTypes.Type(value = OpenQuestion.class, name = "OpenQuestion"),
    @JsonSubTypes.Type(value = MultipleChoiceQuestion.class, name = "MultipleChoiceQuestion")
})
public class Question {

    /**
     * String representation of the Question
     */
    private String question;
    /**
     * Default Constructor for object mappers
     */
    public Question() {
    }

    /**
     * Constructor for Question
     * @param question
     */
    public Question(String question) {
        this.question = question;
    }

    /**
     * Getter for question
     * @return question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Setter for question
     * @param question String representation of the question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * String representation of Question
     * @return String representation of Question
     */
    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                '}';
    }
}
