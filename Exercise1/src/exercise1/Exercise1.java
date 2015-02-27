package exercise1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import juice.Juice;
import test3.Min;

public class Exercise1 {
    
    static class Comp implements Comparator<String> {
                @Override
                public int compare(String a, String b) {
                    return a.compareTo(b);
                }
            }
    
    public static void main(String[] args) throws FileNotFoundException,
            IOException, InterruptedException {
        
        LinkedList<Juice> juices = new LinkedList<Juice>();        
        BufferedReader inFile = new BufferedReader(new FileReader("juice.in"));
        
        while(inFile.ready()) {
            Juice temp = new Juice(inFile.readLine());
            
            if(!juices.contains(temp)) {
                juices.add(temp);
            }
        }
        
        inFile.close();
        
        BufferedWriter out = new BufferedWriter(new FileWriter("juice1.out"));
        LinkedList<String> allIngr = new LinkedList<String>();
        
        for(Juice tmp : juices) {
            String str[] = tmp.getComps();
            
            for(int i = 0; i < tmp.getNum(); i++) {
                if(!allIngr.contains(str[i])) {
                    allIngr.add(str[i]);
                }
            }
        }
        
        for(String it : allIngr) {
            out.write(it);
            out.newLine();
        }
        
        out.flush();
        out.close();
        
        BufferedWriter out2 = new BufferedWriter(new FileWriter("juice2.out"));
        Collections.sort(allIngr,new Comp());
        
        for(String it : allIngr) {
            out2.write(it);
            out2.newLine();
        }
        
        out2.flush();
        out2.close();
        
        BufferedWriter out3 = new BufferedWriter(new FileWriter("juice3.out"));
        Min m = new Min(juices);
        m.start();
        m.join();
        int a = juices.size() - m.getN();
        out3.write(Integer.toString(a));
        out3.newLine();
        out3.flush();
        out3.close();        
    }    
}
