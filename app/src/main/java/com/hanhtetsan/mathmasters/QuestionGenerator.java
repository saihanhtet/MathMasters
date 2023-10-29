package com.hanhtetsan.mathmasters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public class QuestionGenerator {
    public static void main(String[] args) {
        List<QuestionModel> questionModelList = generateQuestions(5);
        for (QuestionModel question : questionModelList) {
            System.out.println(question);
        }
    }
    public static List<QuestionModel> generateQuestions(int numQuestions) {
        List<QuestionModel> questionList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numQuestions; i++) {
            int operand1 = random.nextInt(12) + 1;
            int operand2 = random.nextInt(12) + 1;
            while (operand2 > operand1){
                operand2 = random.nextInt(12) + 1;
            }
            String operator = getRandomOperator(random);
            int correctAnswer;
            switch (operator) {
                case "+":
                    correctAnswer = operand1 + operand2;
                    break;
                case "-":
                    correctAnswer = operand1 - operand2;
                    break;
                case "x":
                    correctAnswer = operand1 * operand2;
                    break;
                case "÷":
                    operand2 = (operand2 == 0) ? 1 : operand2;
                    correctAnswer = operand1 / operand2;
                    break;
                default:
                    throw new IllegalStateException("Invalid operator");
            }

            List<String> answers = generateAnswerChoices(correctAnswer, random);
            // Shuffle answer choices
            Collections.shuffle(answers);
            String questionText = operand1 + " " + operator + " " + operand2;
            String choice1 = answers.get(0);
            String choice2 = answers.get(1);
            String choice3 = answers.get(2);
            String choice4 = answers.get(3);
            String correctChoice = Integer.toString(correctAnswer);
            questionList.add(new QuestionModel(questionText, choice1, choice2, choice3, choice4, correctChoice));
        }

        return questionList;
    }

    private static String getRandomOperator(Random random) {
        String[] operators = {"+", "-", "x", "÷"};
        return operators[random.nextInt(operators.length)];
    }

    private static List<String> generateAnswerChoices(int correctAnswer, Random random) {
        List<String> answers = new ArrayList<>();
        answers.add(Integer.toString(correctAnswer));
        while (answers.size() < 4) {
            int incorrectAnswer = random.nextInt(12) + 1; // Generate a random number between 1 and 12
            String incorrectAnswerStr = Integer.toString(incorrectAnswer);
            if (!answers.contains(incorrectAnswerStr) && incorrectAnswer != correctAnswer) {
                answers.add(incorrectAnswerStr);
            }
        }

        return answers;
    }
}