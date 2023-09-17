import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GuessNumberGameGUI {
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 100;
    private static final int MAX_ATTEMPTS = 10;

    private int targetNumber;
    private int attempts;
    private String playerName;  // Store player name with try number
    private int currentTry;
    private ArrayList<String> highScores = new ArrayList<>();

    private JFrame frame;
    private JPanel panel;
    private JTextField guessField;
    private JButton guessButton;
    private JButton topScoresButton;
    private JButton playAgainButton;
    private JButton newPlayerButton;
    private JLabel messageLabel;
    private JLabel attemptsLabel;

    public GuessNumberGameGUI() {
        frame = new JFrame("Guess a Number Game");
        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));

        guessField = new JTextField();
        guessButton = new JButton("Guess");
        topScoresButton = new JButton("Top Scores");
        playAgainButton = new JButton("Play Again");
        newPlayerButton = new JButton("New Player");
        messageLabel = new JLabel("Welcome! Guess the number I'm thinking of between 1-100");
        attemptsLabel = new JLabel("");

        panel.add(messageLabel);
        panel.add(guessField);
        panel.add(guessButton);
        panel.add(attemptsLabel);
        panel.add(topScoresButton);
        panel.add(playAgainButton);
        panel.add(newPlayerButton);

        frame.add(panel);
        frame.setSize(300, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        guessField.setEnabled(false); // Disable input until the player starts a new game
        guessButton.setEnabled(false); // Disable guess button until the player starts a new game
        playAgainButton.setEnabled(false); // Disable Play Again until the game is completed
        newPlayerButton.setEnabled(true); // Enable New Player at the start

        playerName = JOptionPane.showInputDialog("Enter your name:") + "-First Try"; // Initialize playerName
        initializeGame();

        guessButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playGame();
            }
        });

        topScoresButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showTopScoresDialog();
            }
        });

        playAgainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playAgain();
            }
        });

        newPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPlayer();
            }
        });
    }

    private void initializeGame() {
        currentTry = 1;
        attempts = 0;
        targetNumber = new Random().nextInt(MAX_NUMBER) + 1;
        updateAttemptsLabel();
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        playAgainButton.setEnabled(false); // Disable Play Again until the game is completed
    }

    private void playGame() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            if (guess < MIN_NUMBER || guess > MAX_NUMBER) {
                throw new NumberFormatException();
            }

            attempts++;

            if (guess == targetNumber) {
                handleWin();
            } else if (attempts >= MAX_ATTEMPTS) {
                handleLoss();
            } else {
                handleIncorrectGuess(guess);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number between " + MIN_NUMBER + " and " + MAX_NUMBER + ".");
        } finally {
            guessField.setText("");
        }
    }

    private void handleWin() {
        String scoreEntry = playerName + ": " + attempts;
        JOptionPane.showMessageDialog(frame, "Congratulations, " + playerName + "! You guessed the number in " + attempts + " attempts.");
        updateHighScores(scoreEntry);
        guessField.setEnabled(false); // Disable input
        guessButton.setEnabled(false); // Disable guess button
        playAgainButton.setEnabled(true); // Enable Play Again button
        newPlayerButton.setEnabled(true); // Enable New Player button
        currentTry++;
        playerName = playerName.split("-")[0] + "-Try " + currentTry; // Update playerName
    }

    private void handleLoss() {
        String scoreEntry = playerName + ": " + attempts;
        JOptionPane.showMessageDialog(frame, "Sorry, you've reached the maximum number of attempts. The number was " + targetNumber + ".");
        updateHighScores(scoreEntry);
        guessField.setEnabled(false); // Disable input
        guessButton.setEnabled(false); // Disable guess button
        playAgainButton.setEnabled(true); // Enable Play Again button
        newPlayerButton.setEnabled(true); // Enable New Player button
        currentTry++;
        playerName = playerName.split("-")[0] + "-Try " + currentTry; // Update playerName
    }

    private void handleIncorrectGuess(int guess) {
        String message = "Try again. ";
        if (guess < targetNumber) {
            message += "Your guess is too low.";
        } else {
            message += "Your guess is too high.";
        }
        JOptionPane.showMessageDialog(frame, message);
        guessField.requestFocus();
        updateAttemptsLabel();
    }

    private void playAgain() {
        initializeGame();
    }

    private void newPlayer() {
        playerName = JOptionPane.showInputDialog("Enter your name:") + "-First Try"; // Initialize playerName
        initializeGame();
    }

    private void updateAttemptsLabel() {
        attemptsLabel.setText("Attempts: " + attempts + "/" + MAX_ATTEMPTS);
    }

    private void updateHighScores(String scoreEntry) {
        highScores.add(scoreEntry);
        Collections.sort(highScores, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int attempts1 = Integer.parseInt(o1.split(": ")[1]);
                int attempts2 = Integer.parseInt(o2.split(": ")[1]);
                return Integer.compare(attempts1, attempts2);
            }
        });

        if (highScores.size() > 10) {
            highScores.remove(10);
        }
    }



    private void showTopScoresDialog() {
        StringBuilder topScoresText = new StringBuilder("Top 10 Scores:\n");
        for (String score : highScores) {
            topScoresText.append(score).append("\n");
        }

        JTextArea textArea = new JTextArea(topScoresText.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(frame, scrollPane, "Top Scores", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GuessNumberGameGUI();
            }
        });
    }
}










