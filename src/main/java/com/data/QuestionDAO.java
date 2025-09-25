package com.data;

import com.domain.Question;
import com.domain.QuestionSet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Collection;

public interface QuestionDAO {

    @SqlUpdate("INSERT INTO Question(Question, Image, AnswerA, AnswerB, AnswerC, AnswerD, PaperCode, QuestionSetCode, Answer)"
    + "VALUES (:question, :image, :a, :b, :c, :d, :paperCode, :questionSetCode, :answer)")
    void insertQuestion(@BindBean Question question);

    @SqlUpdate("UPDATE Question SET " +
            "Question = :question, Image = :image, " +
            "AnswerA = :a, AnswerB = :b, AnswerC = :c, AnswerD = :d, Answer = :answer," +
            " PaperCode = :paperCode, QuestionSetCode = :QuestionSetCode " +
            "WHERE PaperCode = :paperCode AND QuestionSetCode = :QuestionSetCode AND Question = :Question")
    void updateQuestion(@BindBean Question question);

    @SqlUpdate("DELETE FROM Question WHERE PaperCode = :paperCode AND QuestionSetCode = :QuestionSetCode AND Question = :Question")
    void deleteQuestion(@BindBean Question question);

    @SqlQuery("select * from Question where PaperCode = :paperCode AND QuestionSetCode = :questionSetCode")
    @RegisterBeanMapper(Question.class)
    Collection<Question> getQuestionsForSet(@Bind("paperCode") String paperCode, @Bind("questionSetCode") String questionSetCode);
    @SqlQuery("select * from Question where PaperCode = :paperCode")
    @RegisterBeanMapper(Question.class)
    Collection<Question> getQuestionSetsForPaper(@Bind("paperCode") String paperCode);
}
