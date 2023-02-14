import java.io.*;
import java.util.*;

public class Application {

    private static Map<Integer, Integer> bidBook = new TreeMap<>();
    private static Map<Integer, Integer> askBook = new TreeMap<>(Collections.reverseOrder());

    public static void main(String[] args) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("input.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter("output.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String line;
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] parts = line.split(",");
            if (parts[0].equals("u")) {
                int price = Integer.parseInt(parts[1]);
                int size = Integer.parseInt(parts[2]);
                String orderType = parts[3];
                if (orderType.equals("bid")) {
                    bidBook.put(price, size);
                } else {
                    askBook.put(price, size);
                }
            } else if (parts[0].equals("q")) {
                String queryType = parts[1];
                if (queryType.equals("best_bid")) {
                    int bestBidPrice = bidBook.isEmpty() ? 0 : bidBook.keySet().stream().max(Integer::compare).get();
                    int bestBidSize = bidBook.isEmpty() ? 0 : bidBook.get(bestBidPrice);
                    fw.write(bestBidPrice + "," + bestBidSize + "\n");
                } else if (queryType.equals("best_ask")) {
                    int bestAskPrice = askBook.isEmpty() ? 0 : askBook.keySet().stream().max(Integer::compare).get();
                    int bestAskSize = askBook.isEmpty() ? 0 : askBook.get(bestAskPrice);
                    fw.write(bestAskPrice + "," + bestAskSize + "\n");
                } else {
                    int price = Integer.parseInt(parts[2]);
                    int bidSize = bidBook.containsKey(price) ? bidBook.get(price) : 0;
                    int askSize = askBook.containsKey(price) ? askBook.get(price) : 0;
                    fw.write((bidSize - askSize) + "\n");
                }
            } else {
                int size = Integer.parseInt(parts[2]);
                String orderType = parts[1];
                if (orderType.equals("sell")) {
                    int totalSize = 0;
                    for (Map.Entry<Integer, Integer> entry : bidBook.entrySet()) {
                        int currentSize = entry.getValue();
                        totalSize += currentSize;
                        if (totalSize >= size) {
                            int remainingSize = totalSize - size;
                            entry.setValue(remainingSize);
                            break;
                        } else {
                            bidBook.remove(entry.getKey());
                        }
                    }
                } else {
                    int totalSize = 0;
                    for (Map.Entry<Integer, Integer> entry : askBook.entrySet()) {
                        int currentSize = entry.getValue();
                        totalSize += currentSize;
                        if (totalSize >= size) {
                            int remainingSize = totalSize - size;
                            entry.setValue(remainingSize);
                            break;
                        } else {
                            askBook.remove(entry.getKey());
                        }
                    }
                }
            }
        } fw.close();

    }
}