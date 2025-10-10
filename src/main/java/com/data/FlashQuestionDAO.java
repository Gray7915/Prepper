package com.data;

import com.domain.FlashQuestion;
import com.domain.Question;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Collection;

public interface FlashQuestionDAO {

    @SqlUpdate("INSERT INTO FlashQuestion(Question, Image, Answer, PaperCode, QuestionSetCode, Category) " +
            "VALUES (:question, :image, :answer, :paperCode, :questionSetCode, :category)")
    void insertFlashQuestion(@BindBean FlashQuestion question);

    @SqlUpdate("UPDATE FlashQuestion SET " +
            "Question = coalesce(:question, Question), " +
            "Image = coalesce(:image, Image), " +
            "Answer = coalesce(:answer, Answer), " +
            "Category = coalesce(:category, Category) " +
            "WHERE ID = :ID")
    void updateFlashQuestion(@BindBean FlashQuestion question);

    @SqlUpdate("DELETE FROM FlashQuestion WHERE ID = :ID")
    void deleteFlashQuestion(@BindBean FlashQuestion question);

    @SqlQuery("SELECT * FROM FlashQuestion WHERE PaperCode = :paperCode AND QuestionSetCode = :questionSetCode")
    @RegisterBeanMapper(FlashQuestion.class)
    Collection<FlashQuestion> getFlashQuestionForSet(@Bind("paperCode") String paperCode,
                                                     @Bind("questionSetCode") String questionSetCode);

    @SqlQuery("SELECT * FROM FlashQuestion WHERE PaperCode = :paperCode")
    @RegisterBeanMapper(FlashQuestion.class)
    Collection<FlashQuestion> getFlashQuestionSetsForPaper(@Bind("paperCode") String paperCode);

    @SqlQuery("SELECT DISTINCT Category FROM FlashQuestion WHERE PaperCode = :paperCode AND QuestionSetCode = :questionSetCode")
    Collection<String> getFlashQuestionCategories(@Bind("paperCode") String paperCode,
                                                  @Bind("questionSetCode") String questionSetCode);
}