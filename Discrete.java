import java.util.*;

public class Discrete{
    private String exp;
    
    public Discrete(){
        this("( p AND q )");
    }
    
    public Discrete(String s){
        exp = s;
    }
    
    public void setExp(String s){
        exp = s;
    }
    
    public String getExp(){
        return exp;
    }
    
    public String truthTable(){
        String s = "|", components[] = exp.split(" "), nameSpaces[] = new String[64];
        int numNames = 0;
        for(String component: components){
            if(!(component.equals("(") || component.equals("AND") || component.equals("OR") || component.equals("XOR") || component.equals("NOT") || component.equals("->") || component.equals("<->") || component.equals(")")) && !Arrays.asList(nameSpaces).contains(component)){
                nameSpaces[numNames] = component;
                if(component.length() > 5){
                    s += component.substring(0, 5) + "|";
                }else{
                    s += String.format("%-5s", component) + "|";
                }
                numNames++;
            }
        }
        s += String.format("%-5s", "OUT") + "|\n";
        int numPerm = (int)Math.pow(2, numNames);
        boolean vals[][] = new boolean[numPerm][numNames], lastVals[] = new boolean[numNames];
        for(int i = 0; i < numPerm; i++){
            Stack<String> ops = new Stack<String>();
            Stack<Boolean> names = new Stack<Boolean>();
            for(int j = 0; j < numNames; j++){
                if(i == 0){
                    vals[i][j] = true;
                }else{
                    vals[i][j] = (i % (numPerm/Math.pow(2, j + 1)) == 0? !lastVals[j]: lastVals[j]);
                }
            }
            lastVals = vals[i];
            for(String component: components){
                if(component.equals("(")){
                }else if(component.equals("AND") || component.equals("OR") || component.equals("XOR") || component.equals("NOT") || component.equals("->") || component.equals("<->")){
                    ops.push(component);
                }else if(component.equals(")")){
                    String op = ops.pop();
                    boolean b = (boolean)names.pop();
                    if(op.equals("AND")){
                        boolean b2 = (boolean)names.pop();
                        names.push(b2 && b);
                    }else if(op.equals("OR")){
                        boolean b2 = (boolean)names.pop();
                        names.push(b2 || b);
                    }else if(op.equals("XOR")){
                        boolean b2 = (boolean)names.pop();
                        names.push(b2 ^ b);
                    }else if(op.equals("NOT")){
                        names.push(!b);
                    }else if(op.equals("->")){
                        boolean b2 = (boolean)names.pop();
                        names.push(!b2 || b);
                    }else{
                        boolean b2 = (boolean)names.pop();
                        names.push((!b2 || b) && (!b || b2));
                    }
                }else{
                    int index = 0;
                    for(int j = 0; j < numNames; j++){
                        if(component.equalsIgnoreCase(nameSpaces[j])){
                            index = j;
                            break;
                        }
                    }
                    names.push(lastVals[index]);
                }
            }
            for(int j = 0; j <= numNames; j++){
                s += "+-----";
            }
            s += "+\n|";
            for(boolean b: lastVals){
                s += b + (b?" |":"|");
            }
            if(!names.empty()){
                s += names.peek()?names.pop().toString() + " |":names.pop().toString() + "|";
            }
            s += "\n";
        }
        for(int i = 0; i <= numNames; i++){
            s += "+-----";
        }
        s += "+\n";
        return s;
    }
}