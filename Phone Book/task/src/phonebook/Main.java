package phonebook;
import java.util.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        File searchFile = new File("/users/william/Downloads/find.txt");
        File directoryFile = new File("/users/william/Downloads/directory.txt");
        List<String> search = new ArrayList<>();
        List<String> directory = new ArrayList<>();

        try (Scanner scanner = new Scanner(searchFile)){
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                search.add(name);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.printf("Searching for %d names%n", search.size());
        try (Scanner scanner = new Scanner(directoryFile)) {
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                String[] parts = name.split(" ");
                if (parts.length > 2) {
                    parts[1] = parts[1] + " " + parts[2];
                }
                directory.add(parts[1]);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.printf("Searching through %d names%n%n", directory.size());
        linear(search, directory);
        bubbleJump(search, directory);
        quickBinary(search, directory);
        hashTable(search, directoryFile);
    }
    public static void linear(List<String> search, List<String> directory) {
        System.out.println("Starting search (linear search)...");
        int count = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < search.size(); i++) {
            for (int j = 0; j < directory.size(); j++) {
                if (search.get(i).equals(directory.get(j))) {
                    count++;
                }
            }
        }
        long end = System.currentTimeMillis();
        long searchTime = end - start;
        long m = searchTime / 60000;
        if (m > 0) {
            searchTime -= (60000 * m);
        }
        long s = searchTime / 1000;
        long ms = searchTime % 1000;
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.%n", count, search.size(), m, s, ms);
    }
    public static void bubbleJump(List<String> search, List<String> directory) {
        int linearCount = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < search.size(); i++) {
            for (int j = 0; j < directory.size(); j++) {
                if (search.get(i).equals(directory.get(j))) {
                    linearCount++;
                }
            }
        }
        long end = System.currentTimeMillis();
        long linearTime = end - start;
        long linearM = linearTime / 60000;
        if (linearM > 0) {
            linearTime -= (60000 * linearM);
        }
        long linearS = linearTime / 1000;
        long linearMs = linearTime % 1000;

        System.out.print("\nStarting search (bubble sort + jump search)...");
        long startSort = System.currentTimeMillis();
        long breakTime = 0;
        for (int i = 0; i < directory.size() - 1; i++) {
            breakTime = System.currentTimeMillis() - startSort;
            if (breakTime > 12 * linearTime) {
                break;
            }
            for (int j = 0; j < directory.size() - i - 1; j++) {
                if (directory.get(j).compareToIgnoreCase(directory.get(j + 1)) > 0) {
                    String temp = directory.get(j);
                    directory.set(j, directory.get(j + 1));
                    directory.set(j + 1, temp);
                }
            }
        }
        long breakM = breakTime / 60000;
        if (breakM > 0) {
            breakTime -= (60000 * breakTime);
        }
        long breakS = breakTime / 1000;
        long breakMs = breakTime % 1000;

        long endSort = System.currentTimeMillis();
        long sortTime = endSort - startSort;
        long sortM = sortTime / 60000;
        if (sortM > 0) {
            sortTime -= (60000 * sortTime);
        }
        long sortS = sortTime / 1000;
        long sortMs = sortTime % 1000;

        long startSearch = System.currentTimeMillis();
        int block = (int) Math.floor(Math.sqrt(directory.size()));
        int count = 0;
        for (int k = 0; k < search.size(); k++) {
            int prev = block - 1;
            while (prev < directory.size() && search.get(k).compareToIgnoreCase(directory.get(prev)) > 0) {
                prev += block;
            }
            for (int l = prev - block + 1; l <= prev && l < directory.size(); l++) {
                if (search.get(k).equals(directory.get(l))) {
                    count++;
                }
            }
        }
        long endSearch = System.currentTimeMillis();
        long searchTime = endSearch - startSearch;
        long searchM = searchTime / 60000;
        if (searchTime > 0) {
            searchTime -= (60000 * searchTime);
        }
        long searchS = searchTime / 1000;
        long searchMs = searchTime % 1000;

        if (breakTime > 10 * linearTime) {
            System.out.printf("%nFound %d / %d entries. ", linearCount, search.size());
            System.out.printf("Time taken: %d min. %d sec. %d ms.%n", linearM + breakM, linearS + breakS, linearMs + breakMs);
            System.out.printf("Sorting time: %d min. %d sec. %d ms. - STOPPED, moved to linear search%n", breakM, breakS, breakMs);
            System.out.printf("Searching time: %d min. %d sec. %d ms.%n", linearM, linearS, linearMs);
        } else {
            System.out.printf("%nFound %d / %d entries. ", count, search.size());
            System.out.printf("Time taken: %d min. %d sec. %d ms.%n", sortM + searchM, sortS + searchS, sortMs + searchMs);
            System.out.printf("Sorting time: %d min. %d sec. %d ms.%n", sortM, sortS, sortMs);
            System.out.printf("Searching time: %d min. %d sec. %d ms.%n", searchM, searchS, searchMs);
        }
    }
    public static int partition(List<String> directory, int low, int high) {
        String pivot = directory.get(high);
        int i = low - 1;
        for (int j = low; j <= high - 1; j++) {
            if (directory.get(j).compareToIgnoreCase(pivot) < 0) {
                i++;
                String temp = directory.get(i);
                directory.set(i, directory.get(j));
                directory.set(j, temp);
            }
        }
        String temp = directory.get(i + 1);
        directory.set(i + 1, directory.get(high));
        directory.set(high, temp);
        return (i + 1);
    }
    public static void quickSort(List<String> directory, int low, int high) {
        if (low < high) {
            int pi = partition(directory, low, high);
            quickSort(directory, low, pi - 1 );
            quickSort(directory, pi + 1, high);
        }
    }
    public static int binarySearch(List<String> search, List<String> directory) {
        int count = 0;
        for (int i = 0; i < search.size(); i++) {
            int low = 0;
            int high = directory.size() - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (search.get(i).equals(directory.get(mid))) {
                    count++;
                }
                if (search.get(i).compareToIgnoreCase(directory.get(mid)) > 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return count;
    }
    public static void quickBinary(List<String> search, List<String> directory) {
        System.out.println("\nStarting search (quick sort + binary search)...");
        long sortStart = System.currentTimeMillis();
        quickSort(directory, 0, directory.size() - 1);
        long sortEnd = System.currentTimeMillis();
        long sortTotal = sortEnd - sortStart;
        long sortM = sortTotal / 60000;
        if (sortM > 0) {
            sortTotal -= (60000 * sortTotal);
        }
        long sortS = sortTotal / 1000;
        long sortMs = sortTotal % 1000;

        long searchStart = System.currentTimeMillis();
        int count = binarySearch(search, directory);
        long searchEnd = System.currentTimeMillis();
        long searchTotal = searchEnd - searchStart;
        long searchM = searchTotal / 60000;
        if (searchM > 0) {
            searchTotal -= (60000 * searchTotal);
        }
        long searchS = searchTotal / 1000;
        long searchMs = searchTotal % 1000;

        System.out.printf("Found: %d / %d entries. Time taken: %d min. %d sec. %d ms.%n", count, search.size(), sortM + searchM, sortS + searchS, sortMs + searchMs);
        System.out.printf("Sorting time: %d min. %d sec. %d ms.%n", sortM, sortS, sortMs);
        System.out.printf("Searching time: %d min. %d sec. %d ms.%n", searchM, searchS, searchMs);
    }
    public static void hashTable(List<String> search, File directoryFile) {
        Hashtable<String, String> hash = new Hashtable<>();
        System.out.println("\nStarting search (hash table)...");
        long hashStart = System.currentTimeMillis();
        try (Scanner scanner = new Scanner(directoryFile)) {
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                String[] parts = name.split(" ");
                if (parts.length > 2) {
                    parts[1] = parts[1] + " " + parts[2];
                }
                hash.put(parts[1], parts[0]);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        long hashEnd = System.currentTimeMillis();
        long hashTime = hashEnd - hashStart;
        long hashM = hashTime / 60000;
        if (hashM > 0) {
            hashTime -= (60000 * hashTime);
        }
        long hashS = hashTime / 1000;
        long hashMs = hashTime % 1000;

        long searchStart = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < search.size(); i++) {
            if (hash.containsKey(search.get(i))) {
                count++;
            }
        }
        long searchEnd = System.currentTimeMillis();
        long searchTime = searchEnd - searchStart;
        long searchM = searchTime / 60000;
        if (hashM > 0) {
            searchTime -= (60000 * searchTime);
        }
        long searchS = searchTime / 1000;
        long searchMs = searchTime % 1000;

        System.out.printf("Found: %d / %d entries. Time taken: %d min. %d sec. %d ms.%n", count, search.size(), hashM + searchM, hashS + searchS, hashMs + searchMs);
        System.out.printf("Creating time: %d min. %d sec. %d ms.%n", hashM, hashS, hashMs);
        System.out.printf("Searching time: %d min. %d sec. %d ms.%n", searchM, searchS, searchMs);

    }

}