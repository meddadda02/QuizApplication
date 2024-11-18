import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuizApplication extends JFrame {
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int timerCount = 10;
    private Timer timer;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JLabel timerLabel;
    private JButton submitButton;

    public QuizApplication() {
        setupQuestions();

        setTitle("Quiz Application");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(5, 1));
        questionLabel = new JLabel("", JLabel.CENTER);
        questionPanel.add(questionLabel);

        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            buttonGroup.add(optionButtons[i]);
            questionPanel.add(optionButtons[i]);
        }
        add(questionPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        timerLabel = new JLabel("Time Left: 10");
        bottomPanel.add(timerLabel);

        submitButton = new JButton("Submit");
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> submitAnswer());

        loadQuestion();

        startTimer();
    }

    private void setupQuestions() {
        questions.add(new Question("What is the capital of Morocco?",
                new String[]{"Marrakech", "Rabat", "Tanger", "Casablanca"}, 1));
        questions.add(new Question("Which programming language is known as 'Java'?",
                new String[]{"Java", "Python", "JavaScript", "C++"}, 0));
        questions.add(new Question("What is 5 + 4?",
                new String[]{"5", "7", "8", "9"}, 3));
        questions.add(new Question("Does Java use pointers like C or C++?",
                new String[]{"yes", "no","",""}, 1));
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestion());

            String[] options = currentQuestion.getOptions();
            for (int i = 0; i < 4; i++) {
                if (i < options.length) {
                    optionButtons[i].setText(options[i]);
                } else {
                    optionButtons[i].setEnabled(false);
                }
                optionButtons[i].setSelected(false);
            }
            timerCount = 10;
            timerLabel.setText("Time Left: " + timerCount);
        } else {
            showResult();
        }
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            timerCount--;
            timerLabel.setText("Time Left: " + timerCount);
            if (timerCount <= 0) {
                submitAnswer();
            }
        });
        timer.start();
    }

    private void submitAnswer() {
        timer.stop();
        Question currentQuestion = questions.get(currentQuestionIndex);
        int selectedOption = -1;

        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected()) {
                selectedOption = i;
                break;
            }
        }

        if (selectedOption == currentQuestion.getCorrectOption()) {
            score++;
        }

        currentQuestionIndex++;
        loadQuestion();
        if (currentQuestionIndex < questions.size()) {
            startTimer();
        }
    }

    private void showResult() {
        StringBuilder result = new StringBuilder("Quiz Over!\n\nYour Score: " + score + "/" + questions.size() + "\n\n");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            result.append("Q").append(i + 1).append(": ").append(q.getQuestion()).append("\n");
            result.append("Correct Answer: ").append(q.getOptions()[q.getCorrectOption()]).append("\n\n");
        }

        JOptionPane.showMessageDialog(this, result.toString(), "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizApplication().setVisible(true));
    }
}