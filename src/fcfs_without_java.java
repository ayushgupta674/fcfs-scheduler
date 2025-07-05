import java.util.*;
public class fcfs_without_java {
    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);
        boolean chk=true;
        List<MYProcess> ls=new ArrayList<MYProcess>();
        int currenttime=0;
        StringBuilder gant=new StringBuilder();
        gant.append("| 0");
        int count=0;
        int netwait=0;
        int netturnaround=0;
        while (chk) {
            System.out.println("Still having process:0 or 1");
            String in=scan.next();
            if (in.equals("1")) {
                ls.add(new MYProcess(scan.next(),scan.nextInt(),scan.nextInt()));
                MYProcess curr=ls.get(ls.size()-1);
                if (currenttime<curr.at) {
                    currenttime=curr.at;
                    gant.append(" | ideal | "+currenttime);
                }
                currenttime+=curr.bt;
                gant.append(" | "+curr.pid+" | "+currenttime);
                curr.turnaroundtime=currenttime-curr.at;
                curr.waitingtime=curr.turnaroundtime-curr.bt;
                count++;
                netturnaround+=curr.turnaroundtime;
                netwait+=curr.waitingtime;
            }
            else{
                chk=false;
            }
        }

        //output details of execution
        System.out.println("PID AT BT TT WT");
        for(MYProcess p:ls){
            System.out.print(p.pid+" ");
            System.out.println(p.at+" "+p.bt+" "+p.turnaroundtime+" "+p.waitingtime);
        }
        System.out.println("Average Waiting Time:"+(float)(netwait)/count);
        System.out.println("Average TurnAround Time:"+(float)(netturnaround)/count);
        System.out.println(gant);
        scan.close();
    }
}
class MYProcess {
    String pid;
    int at;
    int bt;
    public MYProcess(String pid, int at, int bt) {
        this.pid = pid;
        this.at = at;
        this.bt = bt;
    }

    //for gant chart and other calculations
    int turnaroundtime;
    int waitingtime;
}