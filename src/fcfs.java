import java.util.*;

public class fcfs {
    static volatile boolean inputDone = false;

    public static void main(String[] args) {
        PriorityQueue<process> queue = new PriorityQueue<>((a, b) -> Integer.compare(a.at, b.at));
        ArrayList<process> list = new ArrayList<>();

        // Thread to take input from user
        Thread inputThread = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("Enter process (PID AT BT) or type 'done':");
                String line = sc.nextLine();
                if (line.equalsIgnoreCase("done")) {
                    inputDone = true;
                    break;
                }
                String[] parts = line.trim().split(" ");
                if (parts.length == 3) {
                    String pid = parts[0];
                    int at = Integer.parseInt(parts[1]);
                    int bt = Integer.parseInt(parts[2]);
                    process p = new process(pid, at, bt);
                    synchronized (queue) {
                        queue.add(p);
                    }
                    synchronized (list) {
                        list.add(p);
                    }
                } else {
                    System.out.println("Invalid input. Please enter in format: PID AT BT");
                }
            }
        });

        StringBuilder gant=new StringBuilder();
                gant.append(0+" || ");
        // Thread to simulate FCFS scheduling
        Thread schedulerThread = new Thread(() -> {
            int currenttime = 0;
            while (true) {
                process curr = null;
                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        curr = queue.poll();
                    }
                }
                //will also be printing gand chart
                if (curr != null) {
                    if (currenttime < curr.at) {
                        currenttime = curr.at;
                        gant.append("ideal || "+currenttime+" || ");
                        
                    }
                    curr.startTime = currenttime;
                    currenttime += curr.bt;
                    gant.append(curr.pid+" || "+currenttime+" || ");
                    curr.completionTime = currenttime;
                } else {
                    if (inputDone && queue.isEmpty()) {
                        System.out.println(gant);
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        inputThread.start();
        schedulerThread.start();

        try {
            inputThread.join();
            schedulerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //time line info
        
        System.out.println("\nPid || AT || BT || CT || WT || TAT");
        float totalwait = 0;
        float pcount = 0;
        float totaltat = 0;
        synchronized (list) {
            for (process str : list) {
                int wt = str.completionTime - str.at - str.bt;
                int tat = str.completionTime - str.at;
                pcount++;
                totalwait += wt;
                totaltat += tat;
                System.out.println(str.pid + "     " + str.at + "     " + str.bt + "     " + str.completionTime + "     " + wt + "     " + tat);
            }
        }
        System.out.println("Average Waiting time:" + (float) (totalwait / pcount));
        System.out.println("Average Turn Around time:" + (float) (totaltat / pcount));
    }
}

class process {
    String pid;
    int at;
    int bt;
    public process(String pid, int at, int bt) {
        this.pid = pid;
        this.at = at;
        this.bt = bt;
    }

    //for gant chart and other calculations
    int startTime;
    int completionTime;
}