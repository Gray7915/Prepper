package com.data;

import com.domain.FlashQuestion;
import com.domain.QuestionSet;
import com.domain.ShortAnswerQuestion;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Collection;

public interface ShortAnswerQuestionDAO {
    @SqlUpdate("INSERT INTO ShortAnswer(QuestionSetCode, PaperCode, Question, Answer) VALUES(:questionSetCode, :paperCode, :question, :answer)")
    void SaveShortAnswerQuestion(@BindBean ShortAnswerQuestion question);

    @SqlQuery("SELECT * FROM ShortAnswer WHERE PaperCode = :paperCode AND QuestionSetCode = :questionSetCode")
    @RegisterBeanMapper(ShortAnswerQuestion.class)
    Collection<ShortAnswerQuestion> getShortAnswerQuestionSet(@Bind("paperCode") String paperCode,
                                                           @Bind("questionSetCode") String questionSetCode);
}

