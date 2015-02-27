package test3;

import java.util.Iterator;
import java.util.LinkedList;
import juice.Juice;

class Tree {
    
    LinkedList<Juice> nods;
    public LinkedList<String> temp;
    LinkedList<String> n;
    int min;
    
    public Tree(LinkedList<Juice> src) {
        min = 1;
        nods = new LinkedList<>();
        temp = new LinkedList<>();
        n = new LinkedList<>();
        
        for(Juice st : src) {
            for(Juice s :src) {
                String str[] = st.getComps();
                boolean flag = true;
                
                for(int i = 0; i < str.length; i++) {
                    if(!s.contains(str[i])) {
                        flag = false;
                    }                        
                }
                
                if(flag && !s.equals(st)) {
                    n.add(st.str+"-"+s.str);
                }
            }
            
            nods.add(st);
        }
    }
    
    public boolean cmp(String a, String b) {
        String a1 = a.substring(0,a.indexOf("-"));        
        String b1 = b.substring(0,b.indexOf("-"));
        String a2 = a.substring(a.indexOf("-"));
        String b2 = b.substring(b.indexOf("-"));
        
        if(a2.equals(b2)||a1.equals(b1)) {
            return true;
        }else {
            return false;
        }
    }
    
    public void per(LinkedList<String> temp, int lvl) {
        boolean flag1;
                
        for(String i : this.n) {
            flag1 = true;
            
            for(String j : temp) {
                if(cmp(i,j)) {
                    flag1 = false;
                }
            }
            
            if(flag1) {
                temp.add(i);
                per(temp,lvl+1);
                
                if(min < temp.size()) {
                    min = temp.size();
                }
                
                temp.remove(i);
            }
        }
    }                    
}

public class Min extends Thread{
    
    private int n;
    private LinkedList<Juice> src;
    
    public Min(LinkedList<Juice> t) {
        src = t;
        n = 0;
    }
    
    public int getN() {
        return n;
    }
    
    @Override
    public void run() {
        Tree tr = new Tree(src);
        tr.per(tr.temp,1);
        n = tr.min;
    }
}
