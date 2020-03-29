package com.olrox.quiz;

import com.olrox.quiz.entity.QuizQuestion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
class QuestionsParser {

    @Disabled("Site already parsed")
    @Test
    public void parseSite() throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        for (int i = 1; i < 297; i++) {

            String htmlPage = restTemplate
                    .getForEntity("https://baza-otvetov.ru/categories/view/1/" + i * 10, String.class)
                    .getBody();

            Document document = Jsoup.parse(htmlPage);

            ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
            FileWriter fileWriter = new FileWriter("parsed-questions0.txt", true);

            Elements elementsWithTooltip = document.body().getElementsByAttributeValue("class", "tooltip");

            for (Element element : elementsWithTooltip) {
                QuizQuestion quizQuestion = new QuizQuestion();
                Element elementWithAnchor = element.getElementsByTag("a").first();
                String question = elementWithAnchor.text();
                quizQuestion.setQuestion(question);

                Element elementWithWrongAnswers = element
                        .getElementsByClass("q-list__quiz-answers")
                        .first();
                if (elementWithWrongAnswers == null) {
                    continue;
                }
                String wrongAnswersString = elementWithWrongAnswers.text();
                String[] wrongAnswersArray = wrongAnswersString
                        .replace("Ответы для викторин: ", "")
                        .split(", ");
                quizQuestion.setWrongAnswersJson(Arrays.asList(wrongAnswersArray));

                Element correctAnswer = element.getElementsByTag("td").last();
                quizQuestion.setCorrectAnswer(correctAnswer.text());

                quizQuestion.setApproved(true);

                JSONObject jsonObject = new JSONObject().put("question", question)
                        .put("correctAnswer", correctAnswer.text())
                        .put("wrongAnswers", new JSONArray(Arrays.asList(wrongAnswersArray)));

                System.out.println(jsonObject.toString());
                fileWriter.append(jsonObject.toString()).append("\n");
                fileWriter.flush();
            }
        }
    }
}