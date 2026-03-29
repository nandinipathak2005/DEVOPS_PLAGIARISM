import java.util.stream.IntStream;

public class PrimeCheck {

    public static boolean verify(int n) {
        if (n <= 1) return false;

        return IntStream.range(2, n)
                .noneMatch(i -> n % i == 0);
    }

    public static void main(String[] args) {
        System.out.println(verify(29));
    }
}
