package com.data;

import com.domain.QuestionSet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Collection;

public interface QuestionSetDAO {

    @SqlUpdate("INSERT INTO QuestionSet(PaperCode, QuestionSetCode) VALUES(:paperCode, :questionSetCode)")
    void SaveQuestionSet(@BindBean QuestionSet questionSet);

    @SqlUpdate("DELETE FROM QuestionSet WHERE QuestionSetCode = :questionSetCode AND PaperCode = :PaperCode")
    void DeleteQuestionSet(@BindBean QuestionSet QuestionSet);

    @SqlQuery("SELECT * FROM QuestionSet WHERE PaperCode = :paperCode")
    @RegisterBeanMapper(QuestionSet.class)
    Collection<QuestionSet> getQuestionSetsForPaper(@Bind("paperCode") String paperCode);

    @SqlQuery("SELECT * FROM QuestionSet")
    @RegisterBeanMapper(QuestionSet.class)
    Collection<QuestionSet> getQuestionSets();

    @SqlUpdate("UPDATE QuestionSet SET PreviousScore = :previousScore, AverageScore = :averageScore, AttemptCount = :attemptCount "
    + "WHERE PaperCode = :paperCode AND QuestionSetCode = :questionSetCode")
    void addScoreandOverall(@BindBean  QuestionSet questionSet);


}
