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
        Iterator it = src.iterator();
        while(it.hasNext()) {
            Juice st = (Juice) it.next();
            Iterator it1 = src.iterator();
            while(it1.hasNext()) {
                Juice s = (Juice) it1.next();
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
        }
        else {
            return false;
        }
    }
    public void per(LinkedList<String> temp,int lvl) {
        Iterator it = n.iterator();
        LinkedList<String> ll = new LinkedList<>();
        Iterator it1 = temp.iterator();
        while(it.hasNext()){
            String w = (String) it.next();
            boolean flag1 = true;
            it1 = temp.iterator();
            while(it1.hasNext()) {
                String str = (String) it1.next();
                if(w.equals(str)) {
                    flag1 = false;
                    break;
                }                
            } 
            if(flag1) {
                ll.add(w);
            }
        }
        it = ll.iterator();
        while(it.hasNext()) {
            String i = (String) it.next();
                    System.out.println(i+lvl);
            it1 = temp.iterator();
            boolean flag = true;
            while(it1.hasNext()) {
                String j = (String) it1.next();
                if(cmp(i,j)) {
                    //System.out.println(i);
                    flag = false;
                    //System.out.println(j);
                }                        
            }
            //System.out.println();
            if(flag) {
                temp.add(i);
                per(temp,lvl+1);
                if(min < temp.size()) {
                    min = temp.size();
                }
                //System.out.println(temp.size());
                temp.remove(i);
            }
        }
        //if(lvl<6)
        //System.out.println(temp.size()+" "+lvl);
    }                    
}

public class Min extends Thread{
    
    private int n;
    private LinkedList<Juice> src;
    public Min(LinkedList<Juice> t) {
        src = t;
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
