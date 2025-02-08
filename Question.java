package Onlinequiz;

public class Question {
    private String questionText;
    private String[] options;
    private int correctOption;
    private String difficulty;

    public Question(String questionText, String[] options, int correctOption, String difficulty) {
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
        this.difficulty = difficulty;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    // This method matches the getCorrectAnswerIndex method name
    public int getCorrectAnswerIndex() {
        return correctOption;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public boolean isCorrectAnswer(int option) {
        return option == correctOption;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

	public void setOptions(String[] options2) {
		// TODO Auto-generated method stub
		
	}
}
