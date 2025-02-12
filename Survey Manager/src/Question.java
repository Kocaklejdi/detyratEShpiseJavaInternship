import java.util.*;

class Question {
    private String questionText;
    private Map<Candidate, Answer> answers;

    public Question(String questionText) {
        this.questionText = questionText;
        this.answers = new HashMap<>();
    }


    public Map<Candidate, Answer> getAnswers() {
        return answers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void addAnswer(Candidate candidate, Answer value) {
        answers.put(candidate, value);
    }
}