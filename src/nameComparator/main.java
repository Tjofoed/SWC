package nameComparator;

import java.util.*;
import java.io.*;

public class main {

    public static void main(String[] args) throws IOException {

        ArrayList<String> list = new ArrayList<>();
        String filename = "html.txt";

//        list = extractNames(filename);
//        compare(list);

        checkHTML(filename);
    }

    public static void compare(ArrayList list) throws FileNotFoundException{
        Scanner temp = new Scanner(new File ("C:\\Users\\mikke_000\\iCloudDrive\\3 semester\\ChatTest\\src\\nameComparator\\names.txt"));
        ArrayList<String> oldList = new ArrayList<>();
        ArrayList<String> newList = list;

        while(temp.hasNextLine()){
            oldList.add(temp.nextLine());
        }

        for (int i = 0; i < newList.size(); i++) {
            for (int j = 0; j < oldList.size(); j++) {
                if (newList.get(i).equals(oldList.get(j))){
                    oldList.remove(j);
                }
            }
        }

        System.out.println("AMOUNT OF DELETES: " + oldList.size());
        System.out.println("DELETEES: " + oldList);

    }

    public static ArrayList extractNames(String filename) throws IOException {
        Scanner html = new Scanner(new File ("C:\\Users\\mikke_000\\iCloudDrive\\3 semester\\ChatTest\\src\\nameComparator\\" + filename));
        FileWriter fw = new FileWriter("C:\\Users\\mikke_000\\iCloudDrive\\3 semester\\ChatTest\\src\\nameComparator\\namesFromHtml.txt");

        ArrayList<String> list = new ArrayList<>();

        while(html.hasNext()){
            if(html.next().contains("_2lek")){
                String firstName = html.next();
                String secondName = html.next();
                list.add(firstName.replace("_2lel\">", "") + " " + secondName.replace("</div><div", ""));
            }
        }
        list.remove(0);
        System.out.println("IMPORTED LIST SIZE: " + list.size());

        for (int i = 0; i < list.size(); i++) {
            fw.write(list.get(i));
            fw.write("\r\n");
        }
       fw.close();


        return list;
    }

    public static void checkHTML(String filename) throws IOException {
        Scanner html = new Scanner(new File ("C:\\Users\\mikke_000\\iCloudDrive\\3 semester\\ChatTest\\src\\nameComparator\\" + filename));
        Scanner names = new Scanner(new File ("C:\\Users\\mikke_000\\iCloudDrive\\3 semester\\ChatTest\\src\\nameComparator\\names.txt"));

        ArrayList<String> namesList = new ArrayList<>();
        String htmlString = "";

        while(html.hasNextLine()){
            htmlString += html.nextLine().replace("&#039;", "'");
        }

        while(names.hasNextLine()){
            namesList.add(names.nextLine());
        }

        for (int i = 0; i < namesList.size(); i++) {
            if(!htmlString.contains(namesList.get(i))){
                System.out.println(namesList.get(i));
            }
        }
    }
}
