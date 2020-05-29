package main.java.enums;

public enum PartsOfString {
    UNIT_NAME("(г|гр|кг|мл|л|грамм)"),                                                              // Наименование единицы измерения
    COUNT_UNIT_NAME(""),                                                                            // Количество единиц.
    UNIT_WITH_COUNT("\\d+((,|\\.)(\\d)+)?(\\s+)?(г|гр|кг|мл|л|грамм)\\b"),                          // Ед. измерение с количеством
    PACKING("(x|х)+\\d{1,3}"),                                                                      // Упаковка

    SODA("негаз| б/г| б/газ | газированная | газ | сил/газ"),                                       // Газированная
    TEMPERATURE_CONDITIONS("охлажд[а-я]{1,10}|заморож[а-я]{1,10}"),                                 // Температурные условия
    PERCENT("((\\d+(.|,))?\\d%)"),                                                                  // Проценты. Например молоко 3,2%
    TARA("(ж/б)|с/б|(ст/б)|(стекло)|бутылка|стакан|м/у|в вак|пэт|д/пак|в/у|тетра"),                 // Тара
    GRADE("(((1?|перв)|(2?|втор)|(3?|трет)|выс(ш)?)((ый|ой|ий.))?\\s?сорт)|в/с"),                   // Сорт
    BRAND(""),                                                                                      // Бренд
    CATALOG("");

    private final String regEx;

    PartsOfString(String regEx) {
        this.regEx = regEx;
    }

    public String getRegEx() {
        return regEx;
    }



}
