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

        ArrayList<ArrayList<IdDegreePair>> idDegreeLists = new ArrayList<>();

        // First line unconditionally starts with "t". So consume that and start reading IdDegreePairs
        scanner.nextLine();
        while (scanner.hasNextLine())
            idDegreeLists.add(readIdDegreePairs(scanner));

        // Sort each ArrayList of IdDegreePairs in descending order.
        idDegreeLists.forEach(Collections::sort);

        // Extract Ids from sorted lists.
        ArrayList<ArrayList<Integer>> idLists = idDegreeLists.stream()
                                                             .map(list -> list.stream()
                                                                              .map(pair -> pair.getId())
                                                                              .collect(Collectors.toCollection(ArrayList::new)))
                                                             .collect(Collectors.toCollection(ArrayList::new));

        // Build output strings.
        ArrayList<String> output = idLists.stream()
                                          .map(list -> list.stream()
                                                           .map(id -> id.toString())
                                                           .reduce((a, b) -> a + " " + b).get())
                                          .collect(Collectors.toCollection(ArrayList::new));

//        PrintStream out = new PrintStream(queryGraphFile + ".dag");
        output.forEach(System.out::println);
    }

    /**
     * This consumes the scanner line by line until the first token in the line is equal to "t"
     * @param scanner
     * @return An ArrayList of IdDegreePair objects
     */
    public static ArrayList<IdDegreePair> readIdDegreePairs(Scanner scanner) {
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

    /**
     * Immutable tuple of size 2.
     * @param <A> type of first element
     * @param <B> type of second element
     */
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

    /**
     * A pair of vertex ID and degree.
     */
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

        /**
         * This returns the oppositely signed value of conventional compareTo
         * between two integers.
         * @param other
         * @return -1 * (degree_of_this.compareTo(degree_of_other))
         */
        public int compareTo(IdDegreePair other) {
            return other.getDegree().compareTo(this.getDegree());
        }
    }
}
