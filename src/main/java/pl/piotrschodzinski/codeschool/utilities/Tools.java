package pl.piotrschodzinski.codeschool.utilities;

import java.util.Scanner;

public class Tools {
    static Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        String result = scanner.nextLine();
        return result;
    }

    public static int getInt(String prompt) {
        System.out.println(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Type valid integer value: ");
            scanner.next();
        }
        int result = scanner.nextInt();
        scanner.nextLine();
        return result;
    }


    public static long getLong(String prompt) {
        System.out.println(prompt);
        while (!scanner.hasNextLong()) {
            System.out.print("Type valid integer value: ");
            scanner.next();
        }
        long result = scanner.nextLong();
        scanner.nextLine();
        return result;
    }

}
