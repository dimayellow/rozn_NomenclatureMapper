package main.java.managers;

import main.java.sqlCollections.meta.Brands;
import main.java.sqlObjects.meta.Brand;
import main.java.systems.MyProjectSettings;
import main.java.systems.SQLBaseQuery;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnknownBrandsFinder implements Runnable{
    private static UnknownBrandsFinder instance;

    private List<String> unfindBrands = new ArrayList<>();
   // private HashMap<String, String> brandsWithIncompleteCompliance = new HashMap();

    private UnknownBrandsFinder() {
    }

    public static UnknownBrandsFinder getInstance() {
        if (instance == null) {
            instance = new UnknownBrandsFinder();
        }
        return instance;
    }

    public void fillLists() throws SQLException {

        Date startAllDate = new Date();

        SQLBaseQuery sqlBaseQuery = SQLBaseQuery.getInstance();
        List<String> allTitleForecastTovars = sqlBaseQuery.getTitleForecastTovars();
        Brands brands = Brands.getInstance();
        brands.fillIn(true);
        Date endDate = new Date();

        System.out.println("Получение данных из SQL " + (endDate.getTime() - startAllDate.getTime())/(double)1000);

        Date startDate = new Date();
        HashSet<String> titlesInBase = new HashSet<>();
        String findedTitle = "";

        for (String tovarName : allTitleForecastTovars) {
            findedTitle = findTitleInString(tovarName);
            if (!findedTitle.equals("")) titlesInBase.add(findedTitle);
        }

        for (String s : titlesInBase) {
            findTitleInBrands(s, brands);
        }
        endDate = new Date();

        System.out.println("Поиск данных, заполнение списков " + (endDate.getTime() - startDate.getTime())/(double)1000);

        startDate = new Date();
        MyProjectSettings settings = MyProjectSettings.getInstance();

        try (FileOutputStream outputStream = new FileOutputStream(settings.getProjectPath() + "/Files/unfindBrands.txt"))
        {
            for (String unfindBrand : unfindBrands) {
                outputStream.write((unfindBrand + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        endDate = new Date();
        System.out.println("Сохранение файла " + (endDate.getTime() - startAllDate.getTime())/(double)1000);
        System.out.println("Всего уникальных наименований: " + titlesInBase.size());
       // System.out.println("С неполным совпадением: " + brandsWithIncompleteCompliance.size());
        System.out.println("Отсутствуют: " + unfindBrands.size());

    }

    private String findTitleInString(String tovarName) {
        String regEx = "\"([^\"]*)\"";
        String reply = "";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(tovarName);
        while (matcher.find()) {
            reply = tovarName.substring(matcher.start(1), matcher.end(1)).toLowerCase().trim();
        }
        return reply;
    }

    private void findTitleInBrands(String title, Brands brands) {
        boolean find = false;
        for (Brand brand : brands.getList()) {
            for (String brandTitle : brand.getNames()) {
                if (title.equals(brandTitle)) {
                    find = true;
                    break;
                }
            }
            if (find) break;
        }
        if (!find) {
            unfindBrands.add(title);
        }
    }


    public static void main(String[] args) throws SQLException {
        UnknownBrandsFinder unknownBrandsFinder = UnknownBrandsFinder.getInstance();
        unknownBrandsFinder.fillLists();
    }

    @Override
    public void run() {

    }
}
