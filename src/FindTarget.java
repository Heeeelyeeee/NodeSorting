import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


public class FindTarget {
    Node[] graph;
    PriorityQueue<Node> pq;

    FindTarget(Node[] graph) {
        this.graph = graph;
    }


    public Node dijkstra(int startInt, int goalInt) {
        ArrayList<Integer> popped = new ArrayList<>();
        this.pq = new PriorityQueue<>(Comparator.comparingInt(a -> (a.weightToStart)));
        //initilizing the algorythm by finding the corresponding first node and adding it to the prio que
        Node start = graph[startInt];
        start.weightToStart = 0; // distance to start
        pq.add(start);

        //main for loop
        while (!pq.isEmpty()) {// goes through all elements in PQ
            Node current = pq.poll();
            for (int j = 0; j < current.corners.size(); j++) {// goes through all corners it has
                Node outskirtNode = graph[current.corners.get(j).nodeTo];// finds the respective node
                if (current.followedNode != outskirtNode && !outskirtNode.popped) {
                    //if the weight of the road is less than the earlier road, then set new road.
                    int totWeightB = current.corners.get(j).weight + current.weightToStart;
                    if (outskirtNode.inpeq) {
                        pq.remove(outskirtNode);
                    }
                    if (totWeightB < outskirtNode.weightToStart) {
                        outskirtNode.followedNode = current;
                        outskirtNode.weightToStart = totWeightB;
                    }
                    outskirtNode.inpeq = true;
                    pq.add(outskirtNode);
                }
            }
           Node polled = current;
            polled.popped = true;
            popped.add(polled.nodeNr);
            if (polled.nodeNr == goalInt) {
                System.out.println("Dijkstra used " + popped.size() + " nodes");
                return polled;
            }

        }
    return null;
    }
//for getting all nodes, used for alt DATA
    public ArrayList<Node> dijkstra(int startInt){
        ArrayList<Node> popped = new ArrayList<>();
        pq = new PriorityQueue<>(Comparator.comparingInt(a -> (a.weightToStart)));
        //initilizing the algorythm by finding the corresponding first node and adding it to the prio que
        Node start = graph[startInt];
        start.weightToStart = 0; // distance to start
        pq.add(start);

        //main for loop
        while (!pq.isEmpty()) {// goes through all elements in PQ
            Node current = pq.poll();
            for (int j = 0; j < current.corners.size(); j++) {// goes through all corners it has
                Node outskirtNode = graph[current.corners.get(j).nodeTo];// finds the respective node
                if (current.followedNode != outskirtNode && !outskirtNode.popped) {
                    //if the weight of the road is less than the earlier road, then set new road.
                    int totWeightB = current.corners.get(j).weight + current.weightToStart;
                    if (outskirtNode.inpeq) {
                        pq.remove(outskirtNode);
                    }
                    if (totWeightB < outskirtNode.weightToStart) {
                        outskirtNode.followedNode = current;
                        outskirtNode.weightToStart = totWeightB;
                    }
                    outskirtNode.inpeq = true;
                    pq.add(outskirtNode);
                }
            }
            Node polled = current;
            polled.popped = true;
            popped.add(polled);
        }
        return popped;
    }

    public ArrayList<Node> dijkstraType(int startInt, int type, int amount){
        ArrayList<Node> popped = new ArrayList<>();
        ArrayList<Node> wantedType = new ArrayList<>();
        pq = new PriorityQueue<>(Comparator.comparingInt(a -> (a.weightToStart)));
        //initilizing the algorythm by finding the corresponding first node and adding it to the prio que
        Node start = graph[startInt];
        start.weightToStart = 0; // distance to start
        pq.add(start);

        //main for loop
        while (!pq.isEmpty()) {// goes through all elements in PQ
            Node current = pq.poll();
            for (int j = 0; j < current.corners.size(); j++) {// goes through all corners it has
                Node outskirtNode = graph[current.corners.get(j).nodeTo];// finds the respective node
                if (current.followedNode != outskirtNode && !outskirtNode.popped) {
                    //if the weight of the road is less than the earlier road, then set new road.
                    int totWeightB = current.corners.get(j).weight + current.weightToStart;
                    if (outskirtNode.inpeq) {
                        pq.remove(outskirtNode);
                    }
                    if (totWeightB < outskirtNode.weightToStart) {
                        outskirtNode.followedNode = current;
                        outskirtNode.weightToStart = totWeightB;
                    }
                    outskirtNode.inpeq = true;
                    pq.add(outskirtNode);
                }
            }
            Node polled = current;
            if(polled.type == type){
                wantedType.add(polled);
            }
            if(wantedType.size() == amount){
                return wantedType;
            }
            polled.popped = true;
            popped.add(polled);
        }
        return wantedType;
    }


    public Node alt(int startInt, int goalInt) {
        ArrayList<Integer> popped = new ArrayList<>();
        this.pq = new PriorityQueue<>((o1, o2) -> {
            int val = o2.altvalue - o1.altvalue;
            if (val >= 0)
                return -1;
            else
                return 1;
        });
        //initilizing the algorythm by finding the corresponding first node and adding it to the prio que
        Node start = graph[startInt];
        start.weightToStart = 0; // distance to start
        pq.add(start);
        int fFGTL = graph[goalInt].dTLM1;
        int fFLTG = graph[goalInt].dFLM1;

        //main for loop
        while (!pq.isEmpty()) {// goes through all elements in PQ
            Node current = pq.poll();
            for (int j = 0; j < current.corners.size(); j++) {// goes through all corners it has
                Node outskirtNode = graph[current.corners.get(j).nodeTo];// finds the respective node
                if (current.followedNode != outskirtNode && !outskirtNode.popped) {
                    //if the weight of the road is less than the earlier road, then set new road.

                    int totWeightB = current.corners.get(j).weight + current.weightToStart;
                    if (outskirtNode.inpeq) {
                        pq.remove(outskirtNode);
                    }
                    if (totWeightB < outskirtNode.weightToStart) {
                        outskirtNode.followedNode = current;
                        outskirtNode.weightToStart = totWeightB;

                        int posibleAlt = fFLTG - outskirtNode.dFLM1 + outskirtNode.weightToStart;
                        if(posibleAlt <= outskirtNode.dTLM1 - fFGTL + outskirtNode.weightToStart){
                            posibleAlt = outskirtNode.dTLM1 - fFGTL + outskirtNode.weightToStart;
                        }
                        if(posibleAlt < 0){
                            posibleAlt = 0;
                        }

                        outskirtNode.altvalue = posibleAlt;

                    }

                    outskirtNode.inpeq = true;
                    pq.add(outskirtNode);
                }
            }
            Node polled = current;
            polled.popped = true;
            popped.add(polled.nodeNr);
            if (polled.nodeNr == goalInt) {
                System.out.println("alt used " + popped.size() + " nodes");
                return polled;
            }

        }
        return null;
    }
}




