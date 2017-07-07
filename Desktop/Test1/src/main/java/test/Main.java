package test;

import java.io.IOException;
import java.util.Date;

//Класс из которого будет всё запускаться. Будет 2 метода маин, один из которых будет за "штрихован" продолжим...
public class Main {
    public static void main(String[] args) throws IOException {
        Date date = new Date();
        Search search = new Search();
        search.test(false);//Если делаете true, то в консоли видите список из 10 слов, если false то не видите
        System.out.println("\n\n\n\n\n"+ (new Date().getTime()-date.getTime()));
    }

}
