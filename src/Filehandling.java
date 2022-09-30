import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Filehandling {
    String[] values = new String[10]; //Used for fixing the splitting
    BufferedReader vertices; //filenames
    BufferedReader nodefile;   // filenames
    BufferedReader names;  //filenames
    BufferedReader landmarks;
    BufferedReader reversed;
    HashMap<String, Integer> places;
    Node[] node;
    int N;
    int K;

    Filehandling(BufferedReader vertices, BufferedReader nodefile, BufferedReader names) {
        this.vertices = vertices;
        this.nodefile = nodefile;
        this.names = names;

    }

    Filehandling(BufferedReader vertices, BufferedReader nodefile, BufferedReader names, BufferedReader landmarkfile, BufferedReader reversed) {
        this.vertices = vertices;
        this.nodefile = nodefile;
        this.names = names;
        this.landmarks = landmarkfile;
        this.reversed = reversed;

    }

    public Node[] makeGraph() throws IOException {
        makenodes();
        makeVertices();
        nodeData();
        nodeToLand();
        return node;
    }

    //for making of preprossesed data
    public Node[] makeAltReversedGraph() throws IOException {
        makenodes();
        makeOVertices(); // reversed making data
        nodeData();
        nodeToLand();
        makeAltReversedData(2951840); //reversed making data Mehamn
        makeAltReversedHelp();
        return node;

    }
    public void makeAltReversedHelp() throws IOException {
        makeVertices();
        makeAltData(2951840); // making data
    }


    public void nodeToLand() throws IOException {
        StringTokenizer st = new StringTokenizer(landmarks.readLine());
        int lenght = Integer.parseInt(st.nextToken());
        for (int i = 0; i < lenght; ++i) {
            st = new StringTokenizer(landmarks.readLine());
            int n = Integer.parseInt(st.nextToken());
            int landmarkdistance = Integer.parseInt(st.nextToken());
            this.node[n].dFLM1 = landmarkdistance;
        }

        StringTokenizer str = new StringTokenizer(reversed.readLine());
        int len = Integer.parseInt(str.nextToken());
        for (int i = 0; i < len; ++i) {
            str = new StringTokenizer(reversed.readLine());
            int n = Integer.parseInt(str.nextToken());
            int landmarkdistance = Integer.parseInt(str.nextToken());
            this.node[n].dTLM1 = landmarkdistance;
        }

    }


    public void makeVertices() throws IOException {
        StringTokenizer st = new StringTokenizer(vertices.readLine());
        this.K = Integer.parseInt(st.nextToken());
        for (String data = vertices.readLine(); data != null; data = vertices.readLine()) {
            hsplit(data);
            Corner c = new Corner(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
            node[Integer.parseInt(values[0])].corners.add(c);
        }
        vertices.close();
    }

    //for making of pre prossessed data
    public void makeOVertices() throws IOException {
        StringTokenizer st = new StringTokenizer(vertices.readLine());
        this.K = Integer.parseInt(st.nextToken());
        for (String data = vertices.readLine(); data != null; data = vertices.readLine()) {
            hsplit(data);

            Corner c = new Corner(Integer.parseInt(values[1]), Integer.parseInt(values[0]), Integer.parseInt(values[2]));
            node[Integer.parseInt(values[1])].corners.add(c);
        }
        vertices.close();
    }

    void makenodes() throws IOException {
        StringTokenizer st = new StringTokenizer(nodefile.readLine());
        this.N = Integer.parseInt(st.nextToken());
        this.node = new Node[N];
        for (int i = 0; i < N; ++i) {
            st = new StringTokenizer(nodefile.readLine());
            int index = Integer.parseInt(st.nextToken());
            double lat = Double.parseDouble(st.nextToken());
            double lon = Double.parseDouble(st.nextToken());
            node[index] = new Node(index, lat, lon);
            node[index].weightToStart = Integer.MAX_VALUE;
        }
    }

    void nodeData() throws IOException {
        StringTokenizer st = new StringTokenizer(names.readLine());
        int lenght = Integer.parseInt(st.nextToken());
        for (int i = 0; i < lenght; ++i) {
            st = new StringTokenizer(names.readLine());
            int n = Integer.parseInt(st.nextToken());
            int type = Integer.parseInt(st.nextToken());
            String name = st.nextToken();
            while (st.hasMoreTokens())
                name += " " + st.nextToken();
            this.node[n].type = type;
            this.node[n].name = name;
        }

    }

    void hsplit(String linje) {
        int j = 0;
        int lengde = linje.length();
        for (int i = 0; i < 5; ++i) {
//Hopp over innledende blanke, finn starten på ordet
            while (linje.charAt(j) <= ' ') ++j;
            int ordstart = j;
//Finn slutten på ordet, hopp over ikke-blanke
            while (j < lengde && linje.charAt(j) > ' ') ++j;
            values[i] = linje.substring(ordstart, j);
        }
    }


    public void wirteFile(Node n) throws IOException {
        FileWriter fw = new FileWriter("Dpath.csv");
        int i=1;
        while (n.followedNode != null) {

            fw.write(n.latitude + "," + n.longitude + "\n");
            n = n.followedNode;
            i++;
            if(i%2000==0){
                fw.close();
                fw = new FileWriter("Dpath"+i+".csv");
            }
        }
        fw.write(n.latitude + "," + n.longitude);
        fw.close();
    }
    public void wirteFileAlt(Node n) throws IOException {
        FileWriter fw = new FileWriter("path.csv");
        int i=1;
        while (n.followedNode != null) {
            fw.write(n.latitude + "," + n.longitude + "\n");
            n = n.followedNode;
            i++;
            if(i%2000==0){
                fw.close();
                fw = new FileWriter("path"+i+".csv");
            }
        }
        fw.write(n.latitude + "," + n.longitude);
        fw.close();
    }

    //for making preprossesed data
    public void makeAltReversedData(int nodenr) throws IOException {
        Node landmark = node[nodenr];
        FileWriter fw = new FileWriter("NordicAltReverse.txt");
        PriorityQueue pqNode = new PriorityQueue<Node>(Comparator.comparingInt(a -> (a.nodeNr)));
        FindTarget ft = new FindTarget(node);
        ArrayList<Node> distance = ft.dijkstra(landmark.nodeNr);

        fw.write(distance.size() + "\n");

            for (int j = 0; j < distance.size(); j++) {
                pqNode.add(distance.get(j));
            }

            for (int i = 0; i < distance.size(); i++) {
                Node n =(Node) pqNode.poll();
            fw.write(n.nodeNr + "\t" + n.weightToStart+"\n");

        }
        fw.close();

    }

    public void makeAltData(int nodenr) throws IOException {
        Node landmark = node[nodenr];
        FileWriter fw = new FileWriter("NordicAlt.txt");
        PriorityQueue pqNode = new PriorityQueue<Node>(Comparator.comparingInt(a -> (a.nodeNr)));
        FindTarget ft = new FindTarget(node);
        ArrayList<Node> w1 = ft.dijkstra(landmark.nodeNr);

        fw.write(w1.size()+"\n");

        for(int j = 0; j<w1.size();j++){
            pqNode.add(w1.get(j));
        }
        for(int i = 0; i<w1.size();i++){
            Node n = (Node) pqNode.poll();
            fw.write( + n.nodeNr+"\t" + n.weightToStart + "\n");
        }

        fw.close();

    }

}




class Node{
    //hiearistick value. value from landmark, minus the value of the target, from the landmark.
    public double latitude;
    public double longitude;
    public String name;
    public boolean popped;
    public int type;
    public int nodeNr;
    public int weightToStart = Integer.MAX_VALUE;
    public int dTLM1; //reversed
    public int dFLM1; //normal
    public int altvalue = Integer.MAX_VALUE;
    public Node followedNode;
    public boolean inpeq = false;
    ArrayList<Corner> corners = new ArrayList<>();


    public Node(int index, double lat, double lon) {
        this.nodeNr = index;
        this.latitude = lat;
        this.longitude = lon;
    }
}

class  Corner{
    public int nodeFrom;
    public int nodeTo;
    public int weight;

    Corner(int nodeFrom, int nodeTo, int weight){
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.weight = weight;
    }

}


