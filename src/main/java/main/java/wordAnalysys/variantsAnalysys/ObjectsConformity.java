/* Класс для нахождения количества вхождений каких либо объектов в части распарсеной строки.
*  Предпологаются следующие варианты, которые могут быть: по одному объекту на каждый класс, наследующийся от SQLBaseObject
*  1 экземпляр на слова из остатка строки, и по 1му экземпляру на каждый элемент из PartsOfString, если он присутствует в исходной строке.
*  Переменная count отвечает за количество строк, которые были проанализированы.
 */

package main.java.wordAnalysys.variantsAnalysys;

import java.util.HashMap;

public class ObjectsConformity {

    private int count = 0;
    private HashMap<Object, Integer> conformityMap = new HashMap<>();
    private final Class<?> currentClass;

    public ObjectsConformity(Class<?> currentClass) {
        this.currentClass = currentClass;
    }
    /* Возвращаент частоту текущего значения в conformityMap относительно общего количества строк
    *  Ответ равен проценту умноженному на 100
     */
    public int getPercentageOfCompliance(Object value) {

        int reply = 0;

        if (value.getClass() == currentClass) {
            if (conformityMap.containsKey(value)) {
                int valueCount = conformityMap.get(value);
                reply = percentageFromValue(valueCount);
            }
        }

        return 0;
    }

    public HashMap<Object, Integer> getPercentageMap() {

        HashMap<Object, Integer> percentageMap = new HashMap<>();

        conformityMap.forEach((k, v) -> {
            percentageMap.put(k, percentageFromValue(v));
        });

        return percentageMap;
    }

    public void addCount() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public void addValue(Object value) {
        if (value.getClass() == currentClass) {
            conformityMap.put(value, conformityMap.computeIfAbsent(value, (k) -> 0) + 1);
        }
    }

    // private HashMap<SQLBaseObject, Integer>
    private int percentageFromValue(int value) {
        int reply = 0;
        if (count != 0) {
            reply = (100 * value) / count;
        }
        return reply;
    }

}
