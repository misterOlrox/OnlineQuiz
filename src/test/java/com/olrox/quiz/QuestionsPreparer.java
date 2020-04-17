package com.olrox.quiz;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.entity.Role;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.WrongAnswer;
import com.olrox.quiz.service.QuizQuestionService;
import com.olrox.quiz.service.QuizQuestionThemeService;
import com.olrox.quiz.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
class QuestionsPreparer {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private QuizQuestionService quizQuestionService;

    @Autowired
    private QuizQuestionThemeService quizQuestionThemeService;

    @Disabled("Site already parsed")
    @Test
    public void parseSite() throws IOException {
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

                quizQuestion.setWrongAnswers(
                        Arrays
                                .stream(wrongAnswersArray)
                                .map(x -> new WrongAnswer(x, quizQuestion))
                                .collect(Collectors.toList())
                );

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

    @Disabled("Database already filled")
    @Test
    public void addQuestionsToDatabase() throws IOException {
        User admin = userService.findAdmin().orElseGet(() -> {
            var alt = userService.signUp("admin", "admin");
            userService.updateRoles(alt, Set.of(Role.ADMIN, Role.USER));
            return alt;
        });

        FileReader fileReader = new FileReader("parsed-questions0.txt");
        BufferedReader reader = new BufferedReader(fileReader);
        String line = reader.readLine();
        QuizQuestionTheme quizQuestionTheme = new QuizQuestionTheme();
        quizQuestionTheme.setThemeName("Вопросы с baza-otvetov.ru");
        quizQuestionThemeService.add(quizQuestionTheme);

        while (line != null) {
            JSONObject jsonObject = new JSONObject(line);

            quizQuestionService.addQuestion(
                    admin,
                    jsonObject.getString("question"),
                    jsonObject.getString("correctAnswer"),
                    WrongAnswer.from(jsonObject.getJSONArray("wrongAnswers").toList()),
                    true,
                    quizQuestionTheme
            );

            line = reader.readLine();
        }
    }
}