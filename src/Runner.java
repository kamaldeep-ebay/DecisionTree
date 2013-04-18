import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


class Attribute
{    
     //AttributeValue[] atrval;
     ArrayList<AttributeValue> atrval;
     
     String name;
     HashMap<String, Integer> hm;
     /*AttributeValue[] getAttrArray()
     {
         return atrval;
     }*/
     public Attribute(String name)
     {
        // TODO Auto-generated constructor stub
        
        this.name = name;
        atrval = new ArrayList<AttributeValue>();
        hm = new HashMap<String, Integer>(); 
     }
     
}

class AttributeValue
{
     int positive;//<=50k is considered positive
     int negative;//>50k is considered negative
    
     //int countpos;
     //int countneg;
     String name;
     
     public AttributeValue(String name)
     {
        // TODO Auto-generated constructor stub
         this.name = name;
         positive=0;
         negative=0;
         //countpos=0;
         //countneg=0;
     }
    
}

class DecisionTree
{
    Attribute[] array;
    
    void setup()
    {
        array = new Attribute[]{new Attribute("age"),
        new Attribute("workclass"),
        new Attribute("fnlwgt"),
        new Attribute("education"),
        new Attribute("education-num"),
        new Attribute("marital-status"),
        new Attribute("occupation"),
        new Attribute("relationship"),
        new Attribute("race"),
        new Attribute("sex"),
        new Attribute("capital-gain"),
        new Attribute("capital-loss"),
        new Attribute("hours-per-week"),
        new Attribute("native-country")};
    }
    
    static int search(ArrayList<AttributeValue> a1, String val)
    {
        Iterator<AttributeValue> itr = a1.iterator();
        int index=0;
        while(itr.hasNext())
        {
            if(itr.next().name.equals(val))
            {
                return index;
            }
            index++;
        }
        
        return -1;
        
    }
    
    static int System_positive=0;
    static int System_negative=0;
    
    static double calcEntropy(AttributeValue a1)
    {
        double probpos = (a1.positive / (a1.positive + a1.negative));
        double probneg = (a1.negative / (a1.positive + a1.negative));
        
        double entropy = 
       -1 *( (probpos * (Math.log10(probpos)/Math.log10(2))) + 
             (probneg * (Math.log10(probneg)/Math.log10(2))));
        
        return entropy;
    }
    
    static double getSystemEntropy()
    {
        double probpos = (System_positive / (System_positive + System_negative));
        double probneg = (System_negative / (System_positive + System_negative));
        
        double entropy = 
       -1 *( (probpos * (Math.log10(probpos)/Math.log10(2))) + 
             (probneg * (Math.log10(probneg)/Math.log10(2))));
        
        return entropy;

    }
    /*static double getAttributeValEntropy(AttributeValue a1)
    {
        double probpos = (a1.positive / (a1.count + a1.countneg));
        double probneg = (System_negative / (System_positive + System_negative));
        
        double entropy = 
       -1 *( (probpos * (Math.log10(probpos)/Math.log10(2))) + 
             (probneg * (Math.log10(probneg)/Math.log10(2))));
        
        return entropy;
    }*/
    static double egain(Attribute a1)
    {
        double systement = getSystemEntropy();
        double sum = 0 ;double prob = 0;
        
        ArrayList<AttributeValue> array = a1.atrval;
        Iterator<AttributeValue> itr = array.iterator();
        
        while(itr.hasNext())
        {
            AttributeValue temp = itr.next();
            prob = (temp.positive + temp.negative) / 
                            (System_positive + System_negative);
            sum += prob * calcEntropy(temp);
        }
        
        return (systement-sum);
    }
    static double egain_general(Attribute a1,AttributeValue av1)
    {
        //double systement = calcEntropy(av1);
        double sysent_attrval = calcEntropy(av1);
        double sum = 0 ;double prob = 0;
        
        ArrayList<AttributeValue> array = a1.atrval;
        Iterator<AttributeValue> itr = array.iterator();
        
        while(itr.hasNext())
        {
            AttributeValue temp = itr.next();
            prob = (temp.positive + temp.negative) / 
                            (av1.positive+av1.negative);
            sum += prob * calcEntropy(temp);
        }
        
        return (sysent_attrval-sum);
    }
}
public class Runner
{

    /**
     * @param args
     * @throws FileNotFoundException 
     */
   
          
    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
       // File file = new  File("/home/kamal/temp.txt");
        BufferedReader br = new BufferedReader(new FileReader("/home/kamal/temp.txt"));
       
        String line = new String();
        
        //String[] array = .split(",");
        
        //Attribute[] atrarray = new Attribute[]{};
        //setup();
        
        DecisionTree dt = new DecisionTree();
        dt.setup();
        int index =0;
        while((line = br.readLine())!=null)
        {
                String[] array = line.split(",");
                //System.out.println(line);
                String val =array[array.length-1].trim();
               
                if(val.equalsIgnoreCase("<=50K"))
                    dt.System_positive++;
                else
                    dt.System_negative++;
                
                for(int i =0;i<array.length-1;i++)
                {
                    array[i]=array[i].trim();
                    if(val.equalsIgnoreCase("<=50K"))
                    {
                        
                        
                        if(dt.array[i].hm.containsKey(array[i]))
                        {
                            index = dt.search(dt.array[i].atrval,array[i]);
                            dt.array[i].atrval.get(index).positive++;
                            //System.out.println("hi"+line);
                            //dt.array[i].atrval.get(index).countpos++;
                        }
                        else
                        {
                                dt.array[i].hm.put(array[i], index);
                                dt.array[i].atrval.add(new AttributeValue(array[i]));
                                //index = dt.search(dt.array[i].atrval,array[i]);
                                dt.array[i].atrval.get(dt.array[i].atrval.size()-1).positive++;
                               // dt.array[i].atrval.get(dt.array[i].atrval.size()-1).countpos++;
                        }
                    }
                    else
                    {
                       
                        
                        if(dt.array[i].hm.containsKey(array[i]))
                        {
                            index = dt.search(dt.array[i].atrval,array[i]);
                            dt.array[i].atrval.get(index).negative++;
                           // dt.array[i].atrval.get(index).countneg++;
                        }
                        else
                        {
                                dt.array[i].hm.put(array[i], index);
                                dt.array[i].atrval.add(new AttributeValue(array[i]));
                                //index = dt.search(dt.array[i].atrval,array[i]);
                                //dt.array[i].atrval.
                                dt.array[i].atrval.get(dt.array[i].atrval.size()-1).negative++;
                                //dt.array[i].atrval.get(dt.array[i].atrval.size()-1).countneg++;
                        }
                    }
                }
        }//while
        
        //file reading and setup done..now start decision learning algo.
        double[] gainarray = new double[dt.array.length];
        
        for(int i =0;i<dt.array.length;i++)
        {
            gainarray[i] = dt.egain(dt.array[i]);
            
        }
        double max =0;int maxindex=0;
        for(int i =0;i<gainarray.length;i++)
        {
            if(gainarray[i]>max)
            {
                max = gainarray[i];
                maxindex=i;
            }
        }
        
        Attribute root = dt.array[maxindex];
        
        //System.out.println(dt.array[1].atrval.get(2).name);
       // System.out.println(dt.array[1].atrval.get(2).name);
        
        ArrayList<Attribute> attrarray_for_attrvalue = new ArrayList<Attribute>(root.atrval.size());
        for(int j=0;j<root.atrval.size();j++)
        {
        double[] gainarray_temp = new double[dt.array.length];    
        for(int i =0;i<dt.array.length;i++)
        {
            if(i == maxindex)
                continue;
            
            else
            {
                dt.egain_general(dt.array[i],root.atrval.get(j));
            }
        }
        
        double max1 =0;int maxindex1=0;
        for(int i =0;i<gainarray_temp.length;i++)
        {
            if(gainarray[i]>max1)
            {
                max1 = gainarray[i];
                maxindex1=i;
            }
        }
        attrarray_for_attrvalue.add(dt.array[maxindex1]);
        }//outer for   
       // System.out.println(dt.array[1].atrval.get(2).name);
        //System.out.println(attrarray_for_attrvalue.get(0).name);
    }

}


