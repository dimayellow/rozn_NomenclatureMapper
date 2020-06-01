package main.java.filling.sqlQq.select;

import main.java.common.obj.sqlObjects.meta.Container;
import main.java.systems.SQLQueries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContainerQuery implements SQLQueries<Container> {

    @Override
    public String getQuery() {
        return "Select\n" +
                "Container.TaraId as id,\n" +
                "isnull(Container.Synonyms, '') as syns,\n" +
                "Container.Title as title\n" +
                "from dbo.Tara as Container";
    }

    @Override
    public Container getElement(ResultSet executeQuery) throws SQLException {
        Container tara = new Container(executeQuery.getInt("id"));
        tara.addName(executeQuery.getString("title"));
        tara.addNamesFromString(executeQuery.getString("syns"));
        tara.sort();
        return tara;
    }
}
