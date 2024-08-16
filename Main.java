package MultyThread.Volatile;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final int SHORT_WORDS_COUNT = 100_000;
    public static final AtomicInteger longThree = new AtomicInteger(0);
    public static final AtomicInteger longFour = new AtomicInteger(0);
    public static final AtomicInteger longFive = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[SHORT_WORDS_COUNT];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
            //System.out.println(texts[i]);
        }

        Thread threadThree = new Thread(() -> {
            for (String text : texts) {
                if (isPoliandr(text) && !isOneLetter(text) && !isAlphabeth(text)) {
                    counterLong(text);
                }
            }
        });
        threadThree.start();

        Thread threadFour = new Thread(() -> {
            for (String text : texts) {
                if (!isPoliandr(text) && isOneLetter(text) && !isAlphabeth(text)) {
                    counterLong(text);
                }
            }
        });
        threadFour.start();

        Thread threadFive = new Thread(() -> {
            for (String text : texts) {
                if (!isPoliandr(text) && !isOneLetter(text) && isAlphabeth(text)) {
                    counterLong(text);
                }
            }
        });
        threadFive.start();

        threadThree.join();
        threadFour.join();
        threadFive.join();

        System.out.printf("Красивых слов с длиной 3: %d шт\n", longThree.get());
        System.out.printf("Красивых слов с длиной 4: %d шт\n", longFour.get());
        System.out.printf("Красивых слов с длиной 5: %d шт\n", longFive.get());
    }

    public static void counterLong(String text) {
        switch (text.length()) {
            case 3:
                longThree.incrementAndGet();
                break;
            case 4:
                longFour.incrementAndGet();
                break;
            case 5:
                longFive.incrementAndGet();
                break;
        }
    }

    public static boolean isPoliandr(String text) {
        text.equals((new StringBuilder(text)).reverse().toString());
        return true;
    }

    public static boolean isOneLetter(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphabeth(String text) {
        char[] ch = text.toCharArray();
        Arrays.sort(ch);
        String res = new String(ch);
        if (!text.equals(res)) {
            return false;
        }
        return true;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
