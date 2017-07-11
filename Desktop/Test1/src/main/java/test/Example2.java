package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

class CountedWord {
    int c;
    String w;
    public CountedWord(String s) {
        String[] l = s.split(" ", 2);
        this.c = Integer.parseInt(l[1]);
        this.w = l[0];
    }
}

public class Example2 {
    public static int TEN = 10;

    public static void main(String[] args) throws Exception {// Сделано пользователем https://toster.ru/user/longclaps
        long start = System.nanoTime();
        HashMap<String, CountedWord[]> prefixMap = new HashMap<>();
        BufferedReader data = new BufferedReader(new FileReader("test.in"), 0x10000);
        for (int i = Integer.parseInt(data.readLine()); i > 0; i--) {
            CountedWord cw = new CountedWord(data.readLine());
            String prefix = cw.w;
            for (int le = prefix.length() - 1; le >= 0; le--) {
                CountedWord[] buf = prefixMap.get(prefix);
                if (buf == null) {
                    buf = new CountedWord[TEN];
                    buf[0] = cw;
                    prefixMap.put(prefix, buf);
                } else { // это даже не сортировка вставкой, а просто вставка
                    int j = TEN - 1;
                    if (buf[j] != null && cw.c <= buf[j].c)
                        break; // уже на этом префиксе cw.c оказался меньше всех
                    while (buf[--j] == null) ;
                    j++;
                    while (j-- > 0 && buf[j].c < cw.c) buf[j + 1] = buf[j];
                    buf[j + 1] = cw;
                }
                prefix = prefix.substring(0, le);
            }
        }

        for (int i = Integer.parseInt(data.readLine()); i > 0; i--) {
            String prefix = data.readLine();
            System.out.println(String.format("     ~ %s", prefix));
            CountedWord[] buf = prefixMap.get(prefix);
            if (buf == null) continue;
            for (int j = 0; j < TEN; j++) {
                CountedWord cw = buf[j];
                if (cw == null) break;
                System.out.println(String.format("%6d %s", cw.c, cw.w));
            }
        }
        System.out.println(String.format(
                "\nit takes %.3f sec", 1e-9 * (System.nanoTime() - start)));
    }
}
