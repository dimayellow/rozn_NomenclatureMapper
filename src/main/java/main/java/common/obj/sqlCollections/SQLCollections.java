package main.java.common.obj.sqlCollections;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public abstract class SQLCollections<S> {

    protected List<S> list = new LinkedList<>();

    public List<S> getList() {return list;}

    public void add(S collectionItem) { list.add(collectionItem); }

    public boolean isFilled() {
        return list.size() > 0;
    }

    public void fillIn() throws SQLException {
        list.clear();
    }

    public void fillIn(boolean needFillingCheck) throws SQLException {
        if (needFillingCheck && !isFilled()) {
            fillIn();
        }
    }

}
