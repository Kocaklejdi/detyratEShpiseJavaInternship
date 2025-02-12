import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SurveyManager manager = new SurveyManager();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createSurvey();
                    break;
                case 2:
                    selectAndTakeSurvey();
                    break;
                case 3:
                    selectAndViewResults();
                    break;
                case 4:
                    selectAndManageSurvey();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Survey Management System ===");
        System.out.println("1. Create New Survey");
        System.out.println("2. Take Survey");
        System.out.println("3. View Survey Results");
        System.out.println("4. Manage Existing Survey");
        System.out.println("5. Exit");
    }

    private static Survey selectSurvey(String action) {
        List<Survey> surveys = manager.getAllSurveys();
        if (surveys.isEmpty()) {
            System.out.println("No surveys available.");
            return null;
        }

        System.out.println("\nAvailable Surveys:");
        for (int i = 0; i < surveys.size(); i++) {
            Survey survey = surveys.get(i);
            System.out.printf("%d. %s (Topic: %s)\n", i + 1, survey.getTitle(), survey.getTopic());
        }

        while (true) {
            int choice = getIntInput("Select a survey to " + action + " (0 to cancel): ");
            if (choice == 0) return null;
            if (choice > 0 && choice <= surveys.size()) {
                return surveys.get(choice - 1);
            }
            System.out.println("Invalid selection. Please try again.");
        }
    }

    private static void selectAndTakeSurvey() {
        Survey selectedSurvey = selectSurvey("take");
        if (selectedSurvey == null) return;
        takeSurvey(selectedSurvey);
    }

    private static void selectAndViewResults() {
        Survey selectedSurvey = selectSurvey("view");
        if (selectedSurvey == null) return;
        viewResults(selectedSurvey);
    }

    private static void selectAndManageSurvey() {
        Survey selectedSurvey = selectSurvey("manage");
        if (selectedSurvey == null) return;
        manageSurvey(selectedSurvey);
    }

    private static void createSurvey() {
        System.out.println("\n=== Create New Survey ===");
        System.out.print("Enter survey title: ");
        String title = scanner.nextLine();

        // Check if survey with this title already exists
        if (manager.getSurveyByTitle(title) != null) {
            System.out.println("A survey with this title already exists. Please choose a different title.");
            return;
        }

        System.out.print("Enter survey topic: ");
        String topic = scanner.nextLine();

        System.out.print("Enter survey description: ");
        String description = scanner.nextLine();

        Survey survey = new Survey(title, topic, description);

        System.out.println("\nEnter questions (minimum 10, maximum 40)");
        System.out.println("Enter 'done' when finished");

        int questionCount = 0;
        while (true) {
            System.out.print("Enter question " + (questionCount + 1) + ": ");
            String questionText = scanner.nextLine();

            if (questionText.equalsIgnoreCase("done")) {
                if (questionCount < 10) {
                    System.out.println("Survey must have at least 10 questions. Please continue.");
                    continue;
                }
                break;
            }

            if (questionCount >= 40) {
                System.out.println("Maximum questions (40) reached.");
                break;
            }

            survey.addQuestion(new Question(questionText));
            questionCount++;
        }

        try {
            manager.addSurvey(survey);
            System.out.println("Survey created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void takeSurvey(Survey survey) {
        System.out.println("\n=== Take Survey: " + survey.getTitle() + " ===");
        System.out.println("Topic: " + survey.getTopic());
        System.out.println("Description: " + survey.getDescription());

        // Get candidate information
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();

        Candidate candidate = new Candidate(firstName, lastName, email, phone);
        Map<Question, Answer> answers = new HashMap<>();

        // Take survey
        for (Question question : survey.getQuestions()) {
            System.out.println("\nQuestion: " + question.getQuestionText());
            System.out.println("1. Agree");
            System.out.println("2. Slightly Agree");
            System.out.println("3. Slightly Disagree");
            System.out.println("4. Disagree");
            System.out.println("5. Skip question");

            int choice = getIntInput("Enter your choice (1-5): ");
            Answer answer;
            switch (choice) {
                case 1:
                    answer = Answer.AGREE;
                    break;
                case 2:
                    answer = Answer.SLIGHTLY_AGREE;
                    break;
                case 3:
                    answer = Answer.SLIGHTLY_DISAGREE;
                    break;
                case 4:
                    answer = Answer.DISAGREE;
                    break;
                default:
                    answer = Answer.NO_ANSWER;
            }
            answers.put(question, answer);
        }

        manager.registerSurveyAnswers(survey, candidate, answers);
        System.out.println("Survey completed successfully!");
    }

    private static void viewResults(Survey survey) {
        System.out.println("\n=== Survey Results: " + survey.getTitle() + " ===");
        survey.printResults();

        System.out.println("\nMost given answer: " + survey.getMostGivenAnswer());

        Candidate mostActive = manager.getMostActiveCandidates();
        if (mostActive != null) {
            System.out.println("Most active participant: " + mostActive.getFullName());
        }
    }

    private static void manageSurvey(Survey survey) {
        while (true) {
            System.out.println("\n=== Manage Survey: " + survey.getTitle() + " ===");
            System.out.println("1. Add Question");
            System.out.println("2. Remove Question");
            System.out.println("3. Remove Under-Answered Questions");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    System.out.print("Enter new question: ");
                    String questionText = scanner.nextLine();
                    survey.addQuestion(new Question(questionText));
                    System.out.println("Question added successfully!");
                    break;

                case 2:
                    List<Question> questions = survey.getQuestions();
                    for (int i = 0; i < questions.size(); i++) {
                        System.out.println((i + 1) + ". " + questions.get(i).getQuestionText());
                    }
                    int questionIndex = getIntInput("Enter question number to remove: ") - 1;
                    if (questionIndex >= 0 && questionIndex < questions.size()) {
                        survey.removeQuestion(questions.get(questionIndex));
                        System.out.println("Question removed successfully!");
                    } else {
                        System.out.println("Invalid question number.");
                    }
                    break;

                case 3:
                    survey.removeUnderAnsweredQuestions();
                    System.out.println("Under-answered questions removed!");
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}