package ru.geekbrains.j3hw7;

public class Cat {
    public String name;

    public Cat(String name) {
        this.name = name;
    }

    @BeforeSuite
    private void wakeUp() {
        System.out.println(name + " woke up\n");
    }

    @Test
    private void stretch() {
        System.out.println(name + " stretched\n");
    }

    @Test(value = 5)
    private void eat() {
        System.out.println(name + " ate\n");
    }

    @Test(value = 3)
    private void wash() {
        System.out.println(name + " washed\n");
    }

//    @Test(value = 11)
//    private void throwRTE1() {
//        System.out.println("This won't be printed");
//    }

    @AfterSuite
    private void sleep() {
        System.out.println(name + " fell asleep\n");
    }

//    @AfterSuite
//    private void throwRTE2() {
//        System.out.println("This won't be printed");
//    }

}

