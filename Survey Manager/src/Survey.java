import java.util.*;
import java.util.stream.Collectors;

class Survey {
    private String title;
    private String topic;
    private String description;
    private List<Question> questions;
    private final Set<Candidate> candidates;

    public Survey(String title, String topic, String description) {
        this.title = title;
        this.topic = topic;
        this.description = description;
        this.questions = new ArrayList<>();
        this.candidates = new HashSet<>();
    }

    // Add a question to the survey
    public void addQuestion(Question question) {
        if (!questions.contains(question)) {
            questions.add(question);
        }
    }

    // Remove a question from the survey
    public void removeQuestion(Question question) {
        questions.remove(question);
    }

    // Validate the survey
    public boolean isValid() {
        if (questions.size() < 10 || questions.size() > 40) {
            return false;
        }

        Set<String> uniqueQuestions = questions.stream()
                .map(Question::getQuestionText)
                .collect(Collectors.toSet());

        return uniqueQuestions.size() == questions.size();
    }

    // Find the most given answer in the survey
    public Answer getMostGivenAnswer() {
        Map<Answer, Integer> answerCounts = new HashMap<>();

        for (Question question : questions) {
            for (Answer answer : question.getAnswers().values()) {
                answerCounts.merge(answer, 1, Integer::sum);
            }
        }

        return answerCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Print survey results
    public void printResults() {
        System.out.println("Survey Results for: " + title);
        for (Question question : questions) {
            System.out.println("\nQuestion: " + question.getQuestionText());
            Map<Answer, Integer> answerCounts = new HashMap<>();

            for (Answer answer : question.getAnswers().values()) {
                if (answerCounts.containsKey(answer)) {
                    // If we've seen this answer before, increment its count
                    answerCounts.put(answer, answerCounts.get(answer) + 1);
                } else {
                    // If this is a new answer, add it with count of 1
                    answerCounts.put(answer, 1);
                }
            }
            for (Answer answer : Answer.values()) {
                System.out.printf("%s: %d\n", answer, answerCounts.getOrDefault(answer, 0));
            }
        }
    }

    // Find answers given by a specific candidate
    public Map<String, Answer> getCandidateAnswers(Candidate candidate) {
        Map<String, Answer> candidateAnswers = new HashMap<>();

        for (Question question : questions) {
            Answer answer = question.getAnswers().get(candidate);
            candidateAnswers.put(question.getQuestionText(), answer != null ? answer : Answer.NO_ANSWER);
        }

        return candidateAnswers;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void removeUnderAnsweredQuestions() {
        int minimumAnswers = candidates.size() / 2;

        // Create an iterator to safely remove items while iterating
        Iterator<Question> iterator = questions.iterator();

        while (iterator.hasNext()) {
            Question question = iterator.next();
            if (question.getAnswers().size() < minimumAnswers) {
                iterator.remove();
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}