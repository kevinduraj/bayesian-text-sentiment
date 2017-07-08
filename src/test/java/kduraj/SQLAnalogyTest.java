package kduraj;

import org.junit.Test;

public class SQLAnalogyTest {

    public static final int[] table1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    public static final int[] table2 = { 1, 1, 2, 1, 2, 3, 1, 2, 3, 4, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6 };

    @Test
    public void test() {

        /**
         * This query:
         * 
         * <pre>
         * SELECT COUNT(*) 
         * FROM table_1 AS t1 
         * INNER JOIN table_2 AS t2 ON t1.key_column <> t2.key_column;
         * </pre>
         * 
         * basically boils down to the following code in java
         * 
         * which roughly outputs table1.length * table2.length enties.
         * 
         * In out case that cout be up to 3.1M*700M entries
         */
        for (int t1 : table1)
            for (int t2 : table2)
                if (t1 != t2)
                    System.out.println(t1 + " " + t2);

        /**
         * This query:
         * 
         * <pre>
         * SELECT COUNT(*)
         * FROM table_1
         * WHERE key_column NOT IN (SELECT key_column FROM table_2) ;
         * </pre>
         * 
         * boils down to this java snippet.
         * 
         * Which outputs, at maximum, 3.1M entries in out example
         * 
         */
        for (int t1 : table1) {
            if (isValueInTable(t1, table2))
                continue;

            System.out.println(t1);
        }

        /**
         * Either one might be preferred depending on task in hand. However, if
         * the objective is to find the relative complement of table2 in table1
         * then second is definitely preferred.
         */

    }

    private boolean isValueInTable(int t1, int[] table22) {
        for (int t2 : table22)
            if (t1 == t2)
                return true;

        return false;
    }

}
