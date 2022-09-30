import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        BufferedReader vertices2 = new BufferedReader(new FileReader(new File("NordicKanter.txt")));
        BufferedReader node2 = new BufferedReader(new FileReader(new File("NordicNodeToCords.txt")));
        BufferedReader names2 = new BufferedReader(new FileReader(new File("NordicNodeToName.txt")));
        BufferedReader landmarks= new BufferedReader(new FileReader(new File("NordicAlt.txt")));
        BufferedReader reverse = new BufferedReader(new FileReader(new File("NordicAltReverse.txt")));


        System.out.println("Write in start node(nr)");
        int start = Integer.parseInt(sc.nextLine());
        System.out.println("Write in goal node(nr)");
        int goal = Integer.parseInt(sc.nextLine());
        System.out.println("Write in type (gas station = 2)");
        int type = Integer.parseInt(sc.nextLine());
        System.out.println("Write in amount");
        int amount = Integer.parseInt(sc.nextLine());

        //'''''''''''''''' Denne må legges inn om du vil at det skal lages preprosisert data når du gjører den'''''''''''''
        //graph = fh2.makeAltReversedGraph();

        Filehandling fh2 = new Filehandling(vertices2,node2,names2,landmarks,reverse);
        Node[] graph = fh2.makeGraph();

        System.out.println("made map\n\n");

        FindTarget ft = new FindTarget(graph);
        ArrayList<Node> wantedType = ft.dijkstraType(start,type,amount);
        FileWriter fw = new FileWriter("TypeClose.csv");
        fw.write("Closest, type" + "\n");
        for (int i = 0; i < wantedType.size(); i++) {
            fw.write(wantedType.get(i).latitude + "," + wantedType.get(i).longitude+"\n");
        }

        fw.close();

        for(int i = 0; i<graph.length;i++){
            graph[i].inpeq = false;
            graph[i].weightToStart = Integer.MAX_VALUE;
            graph[i].followedNode = null;
            graph[i].popped = false;
            graph[i].altvalue = 0;
        }

        FindTarget ftd = new FindTarget(graph);
        Node end;
        Date startD = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            ftd.dijkstra(start, goal);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime() - startD.getTime() < 1000);
        tid = (double)
                (slutt.getTime() - startD.getTime()) / runder;
        System.out.println("tid Dijstra :" + tid + "\n");

        for(int i = 0; i<graph.length;i++){
            graph[i].inpeq = false;
            graph[i].weightToStart = Integer.MAX_VALUE;
            graph[i].followedNode = null;
            graph[i].popped = false;
            graph[i].altvalue = 0;
        }
        end = ftd.dijkstra(start, goal);

        fh2.wirteFile(end);
        int s=end.weightToStart/100;
        int sec = s % 60;
        int min = (s / 60)%60;
        int hours = (s/60)/60;

        String strSec=(sec<10)?"0"+(sec):Integer.toString(sec);
        String strmin=(min<10)?"0"+(min):Integer.toString(min);
        String strHours=(hours<10)?"0"+(hours):Integer.toString(hours);

        System.out.println("Time for diklsra: "+strHours + ":" + strmin + ":" + strSec+"\n");

        for(int i = 0; i<graph.length;i++){
            graph[i].inpeq = false;
            graph[i].weightToStart = Integer.MAX_VALUE;
            graph[i].followedNode = null;
            graph[i].popped = false;
            graph[i].altvalue = 0;
        }


        FindTarget ftAlt = new FindTarget(graph);
        Node altEnd;
        Date startA = new Date();
        int runderA = 0;
        double tidA;
        Date sluttA;
        do {
            ftAlt.alt(start,goal);
            sluttA = new Date();
            ++runderA;
        } while (sluttA.getTime() - startA.getTime() < 1000);
        tidA = (double)
                (sluttA.getTime() - startA.getTime()) / runderA;
        System.out.println("tid Alt :" + tidA + "\n");

        for(int i = 0; i<graph.length;i++){
            graph[i].inpeq = false;
            graph[i].weightToStart = Integer.MAX_VALUE;
            graph[i].followedNode = null;
            graph[i].popped = false;
            graph[i].altvalue = 0;
        }
        altEnd = ftAlt.alt(start,goal);


        int sAlt=altEnd.weightToStart/100;
        int secAlt = sAlt % 60;
        int minAlt = (sAlt / 60)%60;
        int hoursAlt = (sAlt/60)/60;

        String strSecAlt=(secAlt<10)?"0"+(secAlt):Integer.toString(secAlt);
        String strminAlt=(minAlt<10)?"0"+(minAlt):Integer.toString(minAlt);
        String strHoursAlt=(hoursAlt<10)?"0"+(hoursAlt):Integer.toString(hoursAlt);

        System.out.println("Time for alt: "+strHoursAlt + ":" + strminAlt + ":" + strSecAlt);
        fh2.wirteFileAlt(altEnd);


    }
}
