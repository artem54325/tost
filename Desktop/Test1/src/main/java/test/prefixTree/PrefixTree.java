package test.prefixTree;


import test.StringIntObj;

import java.util.*;

public class PrefixTree {
    Map<Character, List<StringIntObj>> children = new TreeMap<>();

    public void put(String text){
        Character character = text.toCharArray()[0];
        if (children.containsKey(character)){
            children.get(character).add(new StringIntObj(text));
        }else{
            List<StringIntObj> list = new ArrayList<>();
            list.add(new StringIntObj(text));
            children.put(character, list);
        }
    }

    public List<StringIntObj> search(String search){
        List<StringIntObj> list = children.get(search.toCharArray()[0]);
        List<StringIntObj> objList = new ArrayList<>();

        if (list==null)
            return null;


        for (int i=0;i<list.size();i++){
            if (list.get(i).getWord().startsWith(search)) {
                objList.add(list.get(i));
            }
        }

        return objList;
    }
}
