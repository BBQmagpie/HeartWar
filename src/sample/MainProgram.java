package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Queue;

/**
 *
 * @author Maggie
 *
 */
public class MainProgram {
    public static ArrayList<Integer> playerTeamRanking=new ArrayList<>();
    public static ArrayList<Integer> oppositeTeamRanking=new ArrayList<>();
    public static ArrayList<String> diff=new ArrayList<>();
    public static int host;
    private static final String outPath=".\\out\\data\\save.txt";

    /**
     *
     * @throws Exception
     */
    public static void initiation() throws Exception {
        read(outPath);
    }

    /**
     *
     */
    public static void newGame() {
        playerTeamRanking.clear();
        playerTeamRanking.add(1);
        oppositeTeamRanking.clear();
        oppositeTeamRanking.add(1);
        diff.clear();
        for (int i = 0; i < 3; i++) {
            diff.add("ez");
        }
        host=0;
    }

    /**
     *
     * @throws Exception
     */
    public static void end() throws Exception {
        save(outPath);
    }

    /**
     * Read the saved ranking data.
     *
     * @param path is the name of the saving file.
     * @throws Exception
     */
    private static void read(String path) throws Exception {
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        // first line for player team's token
        String[] data = line.split(",");
        for (String next : data) {
            playerTeamRanking.add(Integer.parseInt(next));
        }
        line = br.readLine();
        // second line for opposite team's token
        data = line.split(",");
        for (String next : data) {
            oppositeTeamRanking.add(Integer.parseInt(next));
        }
        line = br.readLine();
        // third line for the difficulty of each computer player
        data = line.split(",");
        for (String next : data) {
            diff.add(next);
        }
        line = br.readLine();
        //forth line for the current host
        host=Integer.parseInt(line);
    }

    /**
     *
     * @param path
     * @throws Exception
     */
    private static void save(String path) throws Exception {
        PrintStream out = new PrintStream(path);
        save(out);
        out.close();
    }

    /**
     *
     * @param out
     * @throws Exception
     */
    private static void save(PrintStream out) throws Exception {
        for (int token : playerTeamRanking) {
            out.print(token + ",");
        }
        out.println();
        for (int token : oppositeTeamRanking) {
            out.print(token + ",");
        }
        out.println();
        for (String str : diff) {
            out.print(str + ",");
        }
        out.println();
        out.print(host);
    }

    public static void majorRoundEnd(Game game){
        int score = game.getScore();
        if(host==1||host==3){
            if(score>=80){
                nextHost(host);
                if(score>=120){
                    playerTeamRanking.add(1);
                    if(score>=160){
                        playerTeamRanking.add(1);
                    }
                }
            }else{
                oppositeTeamRanking.add(1);
                if(score<30){
                    oppositeTeamRanking.add(1);
                    if(score==0){
                        oppositeTeamRanking.add(1);
                    }
                }
            }
        }else{
            if(score>=80){
                nextHost(host);
                if(score>=120){
                    oppositeTeamRanking.add(1);
                    if(score>=160){
                        oppositeTeamRanking.add(1);
                    }
                }
            }else{
                playerTeamRanking.add(1);
                if(score<30){
                    playerTeamRanking.add(1);
                    if(score==0){
                        playerTeamRanking.add(1);
                    }
                }
            }
        }
        try {
            save(outPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void nextHost(int index) {
        if (index + 1 > 3) {
            host = 0;
        } else {
            host++;
        }
    }

    public static int loopInFour(int index) {
        if (index + 1 > 3) {
            index = 0;
        } else {
            index++;
        }
        return index;
    }

    public static void main(String[] args) {

    }
}
