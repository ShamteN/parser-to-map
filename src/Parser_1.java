import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;


class Object {
 int obj = 0;
 int objLen;
 ArrayList<double[]> wSP = new ArrayList<>();
 ArrayList<ArrayList<double[]>> wXY = new ArrayList<>();

 public void addObj(double[] sp, double[][] xy) {
  wSP.add(sp);
  System.out.println("xSP: " + wSP.get(obj)[0]);
  System.out.println("ySP: " + wSP.get(obj)[1]);

  objLen = xy.length;
  wXY.add(new ArrayList<>());
  for (int i = 0; i < objLen; i++) {
   wXY.get(obj).add(new double[]{xy[i][0], xy[i][1]});
  }

  obj += 1;
 }

 public double[] getX(int obj) {
  int objLen = wXY.get(obj).size();
  double res[] = new double[objLen+1];
  res[0] = wSP.get(obj)[0];
  for (int i = 0; i < objLen; i++) {
   res[i+1] = res[i] + wXY.get(obj).get(i)[0];
  }
  return res;
 }

 public double[] getY(int obj) {
  int objLen = wXY.get(obj).size();
  double res[] = new double[objLen+1];
  res[0] = wSP.get(obj)[1];
  for (int i = 0; i < objLen; i++) {
   res[i+1] = res[i] + wXY.get(obj).get(i)[1];
  }
  return res;
 }
}

class Woods extends Object {}
class Meadows extends Object {}

class Rocks {
 int rock = 0;
 int rockLen = 6;
 ArrayList<double[]> rSP = new ArrayList<>();
 ArrayList<double[]> rSC = new ArrayList<>();

 double xR[] = {0, 3, 13, 16, 19, 20};
 double yR[] = {33, 12, 17, 0, 8, 33};

 public void addRock(double[] sp, double[] s) {
  // dodanie SP (traslete(x,y))
  rSP.add(sp);
  // skalowanie (scale(x, y))
  rSC.add(s);
  rock += 1;
 }

 public double[] getX(int rock) {
  double res[] = new double[rockLen];
  for(int i=0;i<rockLen;i++){
   res[i] = rSP.get(rock)[0] + xR[i] * rSC.get(rock)[0];
  }
  return res;
 }
 public double[] getY(int rock) {
  double res[] = new double[rockLen];
  for(int i=0;i<rockLen;i++){
   res[i] = rSP.get(rock)[1] + yR[i] * rSC.get(rock)[1];
  }
  return res;
 }

}

class RockBlackTemplate extends Rocks {
 String color = "BLACK";
}

class RockGrayTemplate extends Rocks {
 String color = "DARKGRAY";
}

class Handler_1 extends DefaultHandler
{
 Woods wood1 = new Woods();
 Meadows meadow1 = new Meadows();

 RockBlackTemplate rock1cz = new RockBlackTemplate();
 RockGrayTemplate rock1sz = new RockGrayTemplate();

 String loc_name;
 String temp_loc_name;
 String id = "";
 String path_d = " ";
 boolean next_path = false;
 boolean next_transform = false;
 boolean next_link = false;
 boolean first_elem = true;
 String temp_str;
 String fixedStr;
 String rock_transform;
 double[][] coordXY;
 int row_path = 0;
 double[] xy_int_sk = new double[2];
 boolean rock_black = false;
 boolean rock_gray = false;
 double[] xy = new double[2];
 double[] s = new double[] {1, 1};


 @Override
 public void startElement(String uri,
                          String localName,
                          String qName,
                          Attributes attributes)
   throws SAXException
 {

  System.out.println("Start Element: " + qName);

  for (int i=0; i < attributes.getLength(); i++) {
   loc_name = attributes.getQName(i);
   System.out.println("attr name: : " + loc_name +
       " value: " + attributes.getValue(loc_name));

   if (loc_name.equals("d")) {
    next_path = true;
   }

   if (loc_name.equals("xlink:href")) {
    next_link = true;
   }

   if (loc_name.equals("id")) {
    id = attributes.getValue(loc_name);
   }

   if (next_path) {
    temp_loc_name = loc_name;
    if (temp_loc_name.equals("d")) {
     path_d = attributes.getValue(temp_loc_name);

     StringTokenizer st1 = new StringTokenizer(path_d, " ");

     int tokenCount = st1.countTokens();
     coordXY = new double[tokenCount - 3][2];

     while (st1.hasMoreTokens()) {
      temp_str = st1.nextToken();

      List<String> xy_str = Arrays.asList(temp_str.split(","));

//         System.out.println(xy_str); // [x, y] - lista stringow
      if (first_elem && !(temp_str.equals("M"))) {

       for (int k = 0; k < xy_int_sk.length; k++) {
        xy_int_sk[k] = Double.parseDouble(xy_str.get(k));
       }
       first_elem = false;
      } else {
       if (xy_str.get(0).contains("l")) {
        fixedStr = xy_str.get(0).replace("l", "");
        Collections.replaceAll(xy_str, xy_str.get(0), fixedStr);
       }
       try {
        coordXY[row_path][0] = Double.parseDouble(xy_str.get(0));
        coordXY[row_path][1] = Double.parseDouble(xy_str.get(1));
        row_path += 1;
       } catch (Exception NumberFormatException) {
//           System.out.println(NumberFormatException);
       }

      }

     }
    }

    if (id.equals("lasy")) {
     double[] xy = xy_int_sk.clone();
     wood1.addObj(xy, coordXY);
    }
    if (id.equals("")) {
     double[] xy = xy_int_sk.clone();
     meadow1.addObj(xy, coordXY);
    }

   }

   if (next_link) {
    temp_loc_name = loc_name;
    String rock_type = attributes.getValue(temp_loc_name);
    if (rock_type.equals("#skala_cz"))
     rock_black = true;
    if (rock_type.equals("#skala_sz"))
     rock_gray = true;
    next_transform = true;
   }
   if (next_transform) {
    temp_loc_name = attributes.getQName(i + 1);
    if (id.equals("skaly")) {
     rock_transform = attributes.getValue(temp_loc_name);
     StringTokenizer st2 = new StringTokenizer(rock_transform, ")");
     int st2count = st2.countTokens();
     boolean next_scale = false;

     while (st2.hasMoreTokens()) {
      String temp = st2.nextToken();
      temp = temp.replaceAll("translate\\(", "");
      temp = temp.replaceAll(", ", ",");
      temp = temp.replaceAll(",", " ");
      if (temp.contains("scale(")) {
       temp = temp.replaceAll("scale\\(", "");
       temp = temp.replaceAll(",  ", ",");
       temp = temp.trim();
      }
//      System.out.println("Test!: " + temp);
      List<String> wsp = Arrays.asList(temp.split(" "));

      if (!next_scale) {
       xy[0] = Double.parseDouble(wsp.get(0));
       xy[1] = Double.parseDouble(wsp.get(1));
      }

      if (next_scale) {
       s[0] = Double.parseDouble(wsp.get(0));
       s[1] = Double.parseDouble(wsp.get(1));
      }
      if (st2count == 2) {
       next_scale = true;
      }

     }
     double sp[] = xy.clone();
     double sc[] = s.clone();
     if (rock_black)
      rock1cz.addRock(sp, sc);
     if (rock_gray)
      rock1sz.addRock(sp, sc);
     s[0] = 1;
     s[1] = 1;
    }


   }
   next_path = false;
   next_link = false;
   next_transform = false;
   first_elem = true;
   row_path = 0;
   rock_black = false;
   rock_gray = false;

   if (qName.equalsIgnoreCase("svg")) {

   }
  }
 }


 @Override
 public void endElement(String uri,
                        String localName,
                        String qName)
   throws SAXException
 {
  if (qName.equalsIgnoreCase("svg"))
  {
   System.out.println("End Element: " + qName);
  }
  if (qName.equalsIgnoreCase("g"))
  {
   id = "";
  }

 }


 @Override
 public void characters(char ch[], int start, int length) throws SAXException
 {

  System.out.println(new String(ch, start, length));
 }

}


public class Parser_1
{

 Woods wood1 = new Woods();
 Meadows meadow1 = new Meadows();

 RockBlackTemplate rock1cz = new RockBlackTemplate();
 RockGrayTemplate rock1sz = new RockGrayTemplate();

 public void run()
 {

  try
  {
   File inputFile = new File("points.xml");
   SAXParserFactory factory = SAXParserFactory.newInstance();
   SAXParser saxParser = factory.newSAXParser();

   Handler_1 handler_1 = new Handler_1();

   saxParser.parse(inputFile, handler_1);
   wood1 = handler_1.wood1;
   meadow1 = handler_1.meadow1;

   rock1cz = handler_1.rock1cz;
   rock1sz = handler_1.rock1sz;
  }
  catch (Exception e)
  {
   e.printStackTrace();
  }
 }

}
