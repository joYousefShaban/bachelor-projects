package aed.collections;

import java.sql.SQLOutput;

public class main {
    public static void main(String[] args) {
//        SmartList<Integer> list = new SmartList<>();
//        for (int i = 0; i < 10; i++)
//            list.add(i);
//        float avg = list.getAvgMTFCompares(); //here 0 will be returned, as it still doesn't hear any invocation of the
//        System.out.println(list.numberOfMTFCompares+"|"+ list.numberOfMTFCalls);
//        System.out.println(avg);
//        list.searchMTF(2);
//        avg = list.getAvgMTFCompares(); // 0 will be returned. There was an invocation of searchMTF, but no comparisons were made (the list was empty).
//        System.out.println(list.numberOfMTFCompares+"|"+ list.numberOfMTFCalls);
//        System.out.println(avg);
//        list.add(1);
//        list.add(2);
//        list.searchMTF(2);
//        avg = list.getAvgMTFCompares(); // will be returned 0.5. There have been 2 summons so far, and 1 comparison. A comparison was only made because element 2 was already at the beginning of the list, and it was found with the first comparison.
//        System.out.println(list.numberOfMTFCompares+"|"+ list.numberOfMTFCalls);
//        System.out.println(avg);
//        list.searchMTF(1);
//        avg = list.getAvgMTFCompares(); //will return 1. There have been 3 searchMTF invocations so far, and 3 comparisons. In the last searchMTF, two comparisons were made. Element one was compared with the element at the beginning of the list, and with the second element
//        System.out.println(list.numberOfMTFCompares+"|"+ list.numberOfMTFCalls);
//        System.out.println(avg);
//        list.searchMTF(1);
//        list.searchMTF(2);
//        list.searchMTF(2);
//        list.searchMTF(2);
//        list.searchMTF(2);
//        list.searchMTF(2);
//        list.searchMTF(3);
//        list.searchMTF(3);
//        avg = list.getAvgMTFCompares(); //will return 1. There have been 3 searchMTF invocations so far, and 3 comparisons. In the last searchMTF, two comparisons were made. Element one was compared with the element at the beginning of the list, and with the second element
//        System.out.println(list.numberOfMTFCompares+"|"+ list.numberOfMTFCalls);
//        System.out.println(avg);
        QueueArray<Integer> q = new QueueArray<>();
        for (int i = 0; i < 10; i++)
            q.enqueue(i);
        System.out.println(q.dequeue());
        System.out.println(q.dequeue());
    }
}