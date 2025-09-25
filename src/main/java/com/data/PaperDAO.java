package com.data;

import com.domain.Paper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Collection;

public interface PaperDAO {

    @SqlUpdate("INSERT INTO Paper (PaperCode) VALUES (:paperCode)")
    void createPaper(@BindBean Paper paper);

    @SqlUpdate("DELETE FROM Paper WHERE PaperCode = :paperCode")
    void deletePaper(@BindBean Paper paper);

    @SqlQuery("SELECT * FROM Paper")
    @RegisterBeanMapper(Paper.class)
    Collection<Paper> getPapers();
}
