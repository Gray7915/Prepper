package com.data;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class JdbiDAOFactory {
    private static final String jdbcURL = "jdbc:sqlite:prepper.sqlite";
    private static final Jdbi jdbi = Jdbi.create(jdbcURL)
            .installPlugin(new SqlObjectPlugin());
    public static PaperDAO getPaperDAO() {
        return jdbi.onDemand(PaperDAO.class);
    }

    public static QuestionDAO getQuestionDAO() {
        return jdbi.onDemand(QuestionDAO.class);
    }

    public static QuestionSetDAO getQuestionSetDAO() {
        return jdbi.onDemand(QuestionSetDAO.class);
    }

    public static FlashQuestionDAO getFlashQuestionDAO() {
        return jdbi.onDemand(FlashQuestionDAO.class);
    }
    public static ShortAnswerQuestionDAO getShortAnswerQuestionDAO () {
        return jdbi.onDemand(ShortAnswerQuestionDAO.class);
    }

}