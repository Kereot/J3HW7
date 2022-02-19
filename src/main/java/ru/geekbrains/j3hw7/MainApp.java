package ru.geekbrains.j3hw7;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainApp {
    public static void main(String[] args) {
        start(Cat.class);
    }

    public static void start (Class<Cat> catClass) {
        int bs = 0;
        int as = 0;
        int highestPriority = 0; // Just for fun
        boolean priority = true;

        try {
            highestPriority = (int) (Test.class.getDeclaredMethod("value").getDefaultValue());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        int lowestPriority = highestPriority + 9;
        System.out.format("Highest Priority for %s: %d\n", Test.class.getSimpleName(), highestPriority);
        System.out.format("Lowest Priority for %s: %d\n\n", Test.class.getSimpleName(), lowestPriority);

        Cat cat = null;
        try {
            Constructor<Cat> constr = catClass.getConstructor(String.class);
            cat = constr.newInstance("Barsik");
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Method[] methods = catClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                bs++;
            }
            if (m.isAnnotationPresent(AfterSuite.class)) {
                as++;
            }
            if (m.isAnnotationPresent(Test.class)){
                if (m.getAnnotation(Test.class).value() < highestPriority || m.getAnnotation(Test.class).value() > lowestPriority) {
                    priority = false;
                }
            }
        }
        if (bs > 1 || as > 1 || !priority) {
            throw new RuntimeException();
        }

        Method[] beforeAll = catClass.getDeclaredMethods();
        for (Method m : beforeAll) {
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                try {
                    m.setAccessible(true);
                    m.invoke(cat);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        Method[] testAll = catClass.getDeclaredMethods();
        for (int i = highestPriority; i <= lowestPriority; i++) {
            for (Method m : testAll) {
                if (m.isAnnotationPresent(Test.class)) {
                    try {
                        if (i == m.getAnnotation(Test.class).value()) {
                            m.setAccessible(true);
                            System.out.println("Priority set to " + m.getAnnotation(Test.class).value());
                            m.invoke(cat);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Method[] afterAll = catClass.getDeclaredMethods();
        for (Method m : afterAll) {
            if (m.isAnnotationPresent(AfterSuite.class)) {
                try {
                    m.setAccessible(true);
                    m.invoke(cat);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
