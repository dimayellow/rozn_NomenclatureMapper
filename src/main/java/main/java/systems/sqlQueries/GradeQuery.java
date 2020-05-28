package main.java.systems.sqlQueries;

import main.java.sqlObjects.meta.Grade;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeQuery implements SQLQueries<Grade>{
    @Override
    public String getQuery() {
        return "Select\n" +
                "Grade.GradeId as id,\n" +
                "ISNULL(Grade.Synonyms, '') as syns,\n" +
                "Grade.Title as title\n" +
                "from dbo.Grade as Grade";
    }

    @Override
    public Grade getElement(ResultSet executeQuery) throws SQLException {
        Grade grade = new Grade(executeQuery.getInt("id"));
        grade.addName(executeQuery.getString("title"));
        grade.addNamesFromString(executeQuery.getString("syns"));
        grade.sort();

        return grade;
    }
}
