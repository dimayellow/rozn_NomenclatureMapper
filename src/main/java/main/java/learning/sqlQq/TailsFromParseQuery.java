package main.java.learning.sqlQq;

import main.java.systems.SQLQueries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TailsFromParseQuery implements SQLQueries<String> {

    private final String count = "10";

    @Override
    public String getQuery() {
        return "use rozn;" +
                "WITH TovarIDList AS (\n" +
                "SELECT top 100 [TovarId] as NeedId,\n" +
                "count([ParseTovarId]) as countPTId\n" +
                "  FROM [rozn].[dbo].[ForecastTovarParse] as ftp\n" +
                "  group by [TovarId]\n" +
                "  having count([ParseTovarId]) >= " + count + "\n" +
                "  )\n" +
                "SELECT [TovarId],\n" +
                "    [Tails]  \n" +
                "  FROM TovarIDList left join [dbo].[ForecastTovarParse] on TovarIDList.NeedId = [TovarId]\n" +
                "  order by [TovarId]";
    }

    @Override
    public String getElement(ResultSet executeQuery) throws SQLException {
        return null;
    }
}
