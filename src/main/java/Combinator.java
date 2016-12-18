import java.util.Arrays;

/**
 * Created by arkonix on 12/18/16.
 */
public class Combinator {

    private static int combinationsCount(int n, int k) {
        if (k > n)
            throw new IllegalArgumentException();
        return factorial(n) / (factorial(n - k) * factorial(k));
    }

    private static int factorial(int n) {
        int res = 1;
        while (n > 1) res *= n--;

        return res;
    }


    /**
     * Algorithmic calculation of combinations
     */
    private static int algCombinationCount(int n, int k) {
        if (k > n)
            throw new IllegalArgumentException();
        String str = "";

        if (k == n) return 1;
        if (k == 1) return n;


        assert (n > k);
        int r1 = algCombinationCount(n - 1, k - 1);
        int r2 = algCombinationCount(n - 1, k);

        int res = r1 + r2;
        return res;
    }


    public static <T> T[][] findCombinations(T[] array, int k) {
        if (k > array.length)
            throw new IllegalArgumentException();

        int n = array.length;
        T[][] res;

        if (k == 1) {
            res = (T[][]) new Object[n][1];
            for (int i = 0; i < n; i++) {
                res[i][0] = array[i];
            }

            return res;
        }

        if (k == n) {
            return (T[][]) new Object[][]{array};
        }

        T[] subSet = Arrays.copyOfRange(array, 1, array.length);

        T[][] subRes1 = findCombinations(subSet, k - 1);
        T[][] subRes2 = findCombinations(subSet, k);

        T[][] combined = combine(array[0], subRes1);


        return merge(combined, subRes2);
    }

    private static <T> T[][] combine(T t, T[][] array) {
        int len = array[1].length;

        T[][] res = (T[][]) new Object[array.length][];

        for (int i = 0; i < array.length; i++) {
            T[] combined = (T[]) new Object[len + 1];
            combined[0] = t;

            System.arraycopy(array[i], 0, combined, 1, len);
            res[i] = combined;
        }

        return res;
    }

    private static <T> T[][] merge(T[][] a, T[][] b) {
        int len = a.length + b.length;
        T[][] merged = (T[][]) new Object[len][];

        for (int i = 0; i < a.length; i++) {
            merged[i] = a[i];
        }

        for (int i = 0; i < b.length; i++) {
            merged[i + a.length] = b[i];
        }

        return merged;
    }

    public static void main(String[] args) {
//        int res = combinationsCount(5, 2);

        String[] array = new String[]{"a", "b", "c", "z", "x"};


        Object[][] res = findCombinations(array, 3);
        System.out.println(Arrays.deepToString(res));
        int n = 5;
        int k = 3;

        String[][] a = new String[][]{
                new String[]{"a", "b"},
                new String[]{"a", "x"},
        };

        String[][] b = new String[][]{
                new String[]{"b", "i"},
                new String[]{"u", "p"},
        };


//        System.out.println(Arrays.deepToString(combine("hovno", b)));
    }

}
