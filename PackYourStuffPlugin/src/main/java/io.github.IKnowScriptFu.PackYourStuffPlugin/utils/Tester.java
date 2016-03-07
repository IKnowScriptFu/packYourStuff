package io.github.IKnowScriptFu.PackYourStuffPlugin.utils;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Set;
import java.util.Stack;

/**
 * Created by Aemilius on 06/03/2016.
 */
public class Tester {
    private static BlueprintsLoader bl;

    public static void main(String[] args){
        bl = new BlueprintsLoader();
        if(!bl.loadingFailed) {
            printNestedMap(bl.getAllBlueprints(), 0);
        }
    }

    private static void printNestedMap(JSONObject nest, int depth){
        Stack<String> keys = new Stack<>();
        for ( String key : (Set<String>) nest.keySet()) {
            Object value = nest.get(key);
            try{
                JSONObject next = (JSONObject) value;
                keys.push(key);
            }catch (ClassCastException ignored){}

            try{
                String data = (String) value;
                printIndented(key + " : " + data, depth);
            }catch (ClassCastException ignored){}

            try{
                long data = (Long) value;
                printIndented(key + " : " + String.valueOf(data), depth);
            }catch (ClassCastException ignored){}
        }

        keys.forEach(key->{
            JSONObject next = (JSONObject) nest.get(key);
            printIndented(key + " : ", depth);
            printNestedMap(next, depth+1);
        });
    }

    private static void printIndented(String string, int depth){
        while(depth-- >= 0){
            System.out.print(" ");
        }
        System.out.println(string);
    }
}
