import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String dataGraphFile = args[0];
        String queryGraphFile = args[1];
        int numQueries = Integer.parseInt(args[2]);
//        String queryGraphFile = "data/human_40n";

        Scanner scanner = new Scanner(new File(queryGraphFile));

        ArrayList<ArrayList<IdDegreePair>> graphsWithDegrees = new ArrayList<>();

        scanner.nextLine();
        while (scanner.hasNextLine())
            graphsWithDegrees.add(readDegrees(scanner));

        graphsWithDegrees.forEach(Collections::sort);

        ArrayList<ArrayList<Integer>> ids = graphsWithDegrees.stream()
                                                             .map(list -> list.stream()
                                                                              .map(pair -> pair.getId())
                                                                              .collect(Collectors.toCollection(ArrayList::new)))
                                                             .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> output = ids.stream()
                                      .map(list -> list.stream()
                                                       .map(id -> id.toString())
                                                       .reduce("", (a, b) -> a + " " + b))
                                      .collect(Collectors.toCollection(ArrayList::new));

        PrintStream out = new PrintStream(queryGraphFile + ".dag");
        output.forEach(out::println);
    }

    public static ArrayList<IdDegreePair> readDegrees(Scanner scanner) {
        ArrayList<IdDegreePair> result = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitted = line.split(" ");

            if (splitted[0].equals("t"))
                break;

            result.add(new IdDegreePair(Integer.parseInt(splitted[0]), splitted.length - 3));
        }

        return result;
    }

    public static class Pair<A, B> {
        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }

    public static class IdDegreePair implements Comparable<IdDegreePair> {
        private final Pair<Integer, Integer> pair;

        public IdDegreePair(int id, int degree) {
            this.pair = new Pair<>(id, degree);
        }

        public Integer getId() {
            return this.pair.getFirst();
        }

        public Integer getDegree() {
            return this.pair.getSecond();
        }

        public int compareTo(IdDegreePair other) {
            return other.getDegree().compareTo(this.getDegree());
        }
    }
}
