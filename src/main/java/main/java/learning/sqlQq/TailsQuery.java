package main.java.learning.sqlQq;

import main.java.systems.SQLQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TailsQuery implements SQLQueries<HashMap.Entry<Integer, Integer>> {

    private final String count = "10";

    @Override
    public String getQuery() {
        return "WITH TovarIDList AS (\n" +
                "SELECT [TovarId] as NeedId,\n" +
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
    public HashMap.Entry<Integer, Integer> getElement(ResultSet executeQuery) throws SQLException {
        return null;
    }
}
