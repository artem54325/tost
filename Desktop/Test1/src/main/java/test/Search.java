package test;


import test.prefixTree.PrefixTree;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search {
//    private List<StringIntObj> vocabularyList;
    private List<String> text;
    private int number;
    private PrefixTree tree = new PrefixTree();

    public Search() throws IOException {//Заполняет arrayList
        text  = Files.readAllLines(Paths.get("test.in"), StandardCharsets.UTF_8);
        writeText();
    }
    private void writeText() throws IOException {//Заполняет массив vocabularyList
        number = Integer.parseInt(text.get(0));
        for (int i=1;i<number+1;i++){
            tree.put(text.get(i));
        }
    }

    public void test(boolean see){
        int i=number+2;
        number += Integer.parseInt(text.get(number+1));
        for (;i<number;i++){
            if (see){
                String list = parsText(text.get(i));
                if (list!=null)
                    System.out.println(text.get(i) + " " +list);
            }else{
                parsText(text.get(i));
            }
        }
    }

    private List<StringIntObj> sort(List<StringIntObj> list){//Находит максимум а надо мининмальный найти!!
        List<StringIntObj> sortList = new ArrayList<>(10);
        if (list==null||list.size()==0)
            return null;
        StringIntObj bottom = list.get(0);
        for (int i=0;i<list.size();i++){
            if (sortList.size()<10){
                sortList.add(list.get(i));
                if (bottom.compareTo(list.get(i))>0)
                    bottom = list.get(i);//Находит минимум из 1 десяти!
            }else{
                if (bottom.compareTo(list.get(i))<0)
                    continue;
                Collections.sort(sortList);
                if (sortList.get(9).compareTo(list.get(i))>0)
                    sortList.set(9, list.get(i));
                for (int a=0;a<sortList.size()-1;a++){
                    if (sortList.get(a).compareTo(list.get(i))>0&&sortList.get(a+1).compareTo(list.get(i))<0){
                        sortList.set(a, list.get(i));
                        bottom=sortList.get(0);
                        break;
                    }
                }
            }
        }
        return sortList;
    }


    private String parsText(String word){//Ищет подходящие слова, если их не находит возвращает пустой массив.
        List<StringIntObj> list = sort(tree.search(word));
//        List<StringIntObj> list = (tree.search(word));
        //Если использовать в сортировки TreeSet то время увеличиться до 25 секунд. Так что нужно дальше думать!
        StringBuffer buffer = new StringBuffer();
        if (list == null||list.size()==0)
            return null;
        Collections.sort(list);
        for (int i = 0;i<10&&i<list.size();i++){
            buffer.append(list.get(i).get()).append(" ");
        }
        return String.valueOf(buffer);
    }
}