package com.star.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author: zzStar
 * @Date: 07-19-2021 21:56
 */
public class Method {

    Stream<String> stream = Stream.of("love", "you", "zzStar", "star", "love");

    @Test
    public void foreach() {
        stream.forEach(System.out::println);
    }

    @Test
    public void distinct() {
        stream.distinct()
                .forEach(System.out::println);
    }

    @Test
    public void sorted() {
        stream.sorted((v1, v2) -> v1.length() - v2.length())
                .forEach(System.out::println);
    }

    @Test
    public void map() {
        stream.map(String::toUpperCase)
                .forEach(System.out::println);
    }

    @Test
    public void flatMap() {
        Stream<List<Integer>> stream = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4, 5));
        stream.flatMap(list -> list.stream())
                .forEach(System.out::println);
    }

    @Test
    public void reduce() {
        Integer len = stream.reduce(0, (sum, str) -> sum + str.length(),
                (a, b) -> a + b);
        System.out.println(len);
    }

}
