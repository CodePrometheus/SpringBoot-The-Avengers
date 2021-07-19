package com.star.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: zzStar
 * @Date: 07-19-2021 22:15
 */
public class Collect {

    Stream<String> stream = Stream.of("I", "love", "you", "too");

    @Test
    public void toList() {
        // List<String> list = stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        // System.out.println(list);
        List<String> list1 = stream.collect(Collectors.toList());
        System.out.println(list1);
    }

    @Test
    public void join() {
        List<String> list = Arrays.asList("java", "python", "C++","php","java");
        System.out.println(String.join("", list));
        System.out.println(list.stream().collect(Collectors.joining("|")));
    }

}
