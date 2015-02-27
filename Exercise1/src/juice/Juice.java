package juice;

import java.util.LinkedList;
import java.util.TreeSet;

public class Juice {
    
    private LinkedList<String> comps;
    private int num;
    public String str;
    
    public Juice(String in) {
        str = in;
        this.comps = new LinkedList<String>();
        String s[] = in.split("[\\ ]+");
        this.num = s.length;
        
        for(int i = 0; i < s.length; i++) {
            this.comps.add(s[i]);
        }
    }
    
    public int getNum() {
        return this.num;
    }
    
    public boolean contains(String fruit) {        
        if (this.comps.contains(fruit)) {
            return true;
        }else {
            return false;
        }
    }
    
    public LinkedList getList() {
        return this.comps;
    }
    
    public String[] getComps() {
        String s[] = this.comps.toArray(new String[]{});
        return s;
    }
    
    @Override
    public boolean equals(Object b) {
        Juice a = (Juice) b;
        TreeSet a1 = new TreeSet();
        TreeSet a2 = new TreeSet();
        String e[] = this.getComps();
        
        for(int i = 0; i < e.length; i++) {
            a1.add(e[i]);            
        }
        
        e = a.getComps();
        
        for(int i = 0; i < e.length; i++) {
            a2.add(e[i]);            
        }
        
        if(a1.equals(a2)) {
            return true;
        }else {
            return false;
        }
    }
}
