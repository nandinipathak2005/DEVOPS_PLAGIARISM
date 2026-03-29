public class PrimeCheck {

    public static boolean validate(int num) {
        if (num <= 1) return false;

        int i = 2;
        boolean flag = true;

        while (i <= num / 2) {
            if (num % i == 0) {
                flag = false;
                break;
            }
            i++;
        }

        return flag;
    }

    public static void main(String[] args) {
        int n = 29;

        if (validate(n)) {
            System.out.println("Prime");
        } else {
            System.out.println("Not Prime");
        }
    }
}
