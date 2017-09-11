package ru.asfarus.test;

import ru.asfarus.test.app.Controller;
import ru.asfarus.test.app.SnakeModel;
import ru.asfarus.test.app.View;
import ru.asfarus.test.controller.SnakeController;
import ru.asfarus.test.model.Field;
import ru.asfarus.test.view.SnakeViewImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    private static final String DEFAULT_PROPERTIES_FILE_NAME = "snake.properties";
    private static final String RESOURCE_PROPERTIES_FILE_NAME = "/snake.properties";

    private static final String HEIGHT = "height";
    private static final String WIDTH = "width";
    private static final String SNAKE_LENGTH = "snake.length";
    private static final String SNAKE_SLEEP_DELAY_MS = "snake.sleepDelayMs";
    private static final String FROGS_COUNT = "frogsCount";
    private static final String FROG_SLEEP_DELAY_MS = "frog.sleepDelayMs";
    private static final String FROG_BEST_DIRECTION_PROBABLY = "frog.bestDirectionProbably";
    private static final String PROPERTIES_FILE_NAME = "file";
    private static final String HELP = "help";

    private static final String[] NAMES_OF_ARGUMENTS = {PROPERTIES_FILE_NAME, HEIGHT, WIDTH, SNAKE_LENGTH, SNAKE_SLEEP_DELAY_MS, FROGS_COUNT, FROG_SLEEP_DELAY_MS, FROG_BEST_DIRECTION_PROBABLY};
    private static final String HELP_TXT = "/help.txt";

    private Properties prop;
    private Properties resourceProperties;

    private Main() {
    }

    private void init(String[] args) throws IOException {
        if (args.length > 0 && HELP.equals(args[0])) {
            help();
            return;
        }

        prop = new Properties();
        FileInputStream fr;
        try {
            fr = new FileInputStream(prop.getProperty(PROPERTIES_FILE_NAME, DEFAULT_PROPERTIES_FILE_NAME));
            prop.load(fr);
        } catch (IOException ex) {
            System.out.printf("Не удалось загрузить настройки из файла %s.%n", prop.getProperty(PROPERTIES_FILE_NAME, DEFAULT_PROPERTIES_FILE_NAME));
        }
        fillPropertiesFromArgs(args, prop);

        int height = getPositiveInt(HEIGHT, 5, 100, "высота поля должна быть в промежутке от %d до %d%n");
        int width = getPositiveInt(WIDTH, 5, 100, "ширина поля должна быть в промежутке от %d до %d%n");
        int snakeLen = getPositiveInt(SNAKE_LENGTH, 1, 100, "длина змейки должна быть в промежутке от %d до %d%n");
        int snakeSleepDelayMs = getPositiveInt(SNAKE_SLEEP_DELAY_MS, 100, 10000, "количество мс. между ходами змейки должно быть в промежутке от %d до %d%n");
        int frogsCount = getPositiveInt(FROGS_COUNT, 1, height * width - 1, "количество лягушек не должно превышать количество клеток на поле - 1 для змеи%n");
        int frogSleepDelayMs = getPositiveInt(FROG_SLEEP_DELAY_MS, snakeSleepDelayMs * 2, 30000, "количество мс. между ходами лягушек должна быть минимум в 2 раза больше чем у змейки (>=%d), но не больше %d%n");
        float bestDirectionProbably = getProbably(FROG_BEST_DIRECTION_PROBABLY);

        SnakeModel model = new Field(width, height, snakeLen, snakeSleepDelayMs, frogsCount, frogSleepDelayMs, bestDirectionProbably);
        View view = new SnakeViewImpl(width, height);
        Controller controller = new SnakeController(model, view);
        controller.init();
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.init(args);
    }

    private void fillPropertiesFromArgs(String[] args, Properties properties) {
        for (String namesOfArgument : NAMES_OF_ARGUMENTS) {
            for (int i = 0; i < args.length - 1; i += 2) {
                if (args[i].equals(namesOfArgument)) {
                    properties.put(namesOfArgument, args[i + 1]);
                    break;
                }
            }
        }
    }

    private void help() throws IOException {
        InputStream helpMsgStream = null;
        try {
            helpMsgStream = getClass().getResourceAsStream(HELP_TXT);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = helpMsgStream.read(buffer)) > 0) {
                System.out.write(buffer, 0, read);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (helpMsgStream != null) {
                helpMsgStream.close();
            }
        }
    }

    private int getPositiveInt(String propName, int min, int max, String notInRangeMsg) throws IOException {
        String val = prop.getProperty(propName);
        if (val == null) {
            System.out.println("не задан параметр " + propName);
        }else {
            int num;
            try {
                num = Integer.valueOf(val);
                if (min <= num && num <= max){
                    return num;
                }
                System.out.printf(notInRangeMsg, min, max);
            } catch (NumberFormatException ex) {
                System.out.println("неверный формат параметра " + propName);
            }
        }
        System.out.println(propName + ": взято значение по умолчанию");
        return Integer.valueOf(loadResourceProperties().getProperty(propName));
    }

    private float getProbably(String propName) throws IOException {
        String val = prop.getProperty(propName);
        if (val == null) {
            System.out.println("не задан параметр " + propName);
        }else {
            float num;
            try {
                num = Float.valueOf(val);
                if (num >= 0.3 && num <= 0.95) {
                    return num;
                }
                System.out.println(propName + " вероятность должна быть от 0.3 до 0.95");
            } catch (NumberFormatException ex) {
                System.out.println("неверный формат параметра " + propName);
            }
        }
        System.out.println(propName + ": взято значение по умолчанию");
        return Float.valueOf(loadResourceProperties().getProperty(propName));
    }

    private Properties loadResourceProperties() throws IOException {
        if (resourceProperties == null) {
            resourceProperties = new Properties();
            InputStream inputStream = null;
            try {
                inputStream = getClass().getResourceAsStream(RESOURCE_PROPERTIES_FILE_NAME);
                resourceProperties.load(inputStream);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return resourceProperties;
    }
}
