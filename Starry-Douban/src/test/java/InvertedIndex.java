import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * 倒排索引
 *
 * @Author: zzStar
 * @Date: 05-15-2021 10:29
 */
public class InvertedIndex {

    public static void main(String[] args) {
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input KeyWords: ");
        String keyword = scanner.nextLine();
        File file;

        for (int i = 1; i <= 6; i++) {
            int cnt = 0;
            try {
                file = ResourceUtils.getFile("classpath:data/file" + i + ".txt");
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String content;
                while ((content = bufferedReader.readLine()) != null) {
                    if (content.toLowerCase().contains(keyword)) {
                        cnt++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 放入treemap
            if (cnt != 0) {
                treeMap.put("file" + i + ".txt", cnt);
            }
        }
        // 按照相关程度排序 （出现次数）
        Comparator<Map.Entry<String, Integer>> comparator = (o1, o2) -> o2.getValue() - o1.getValue();
        // Map转化成List<Entry<String, Integer>>，调用排序API
        List<Map.Entry<String, Integer>> list = new ArrayList<>(treeMap.entrySet());
        // 根据Value大小进行排序
        list.sort(comparator);

        System.out.println("-------------------");
        System.out.println("|filename\t|count|");
        System.out.println("-------------------");
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println("|"+entry.getKey()+"\t|"+entry.getValue()+"\t  |");
        }
        System.out.println("-------------------");
    }
}
