package com.talks.springstuff.server;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Sample {

    public void sample() {
//        List<Integer> sampleList = Arrays.asList(1,2,3,4,5);
//        sampleList.stream();
//        sampleList.parallelStream();
//
//        for (int i = 0; i < 5 ; i++) {
//            System.out.printlnt(i);
//        }
//
//        for ( int i : sampleList) {
//            System.out.printlnt(i);
//        }
//
//        sampleList.stream().forEach(System.out::println);

        List<Integer> sampleList = Arrays.asList(1,2,3,4,5);
        List<Integer> anotherList = sampleList.stream()
                .filter(x -> x > 2)
                .filter(x -> x < 4)
                .peek(System.out::println)
                .collect(Collectors.toList());


    }
}
