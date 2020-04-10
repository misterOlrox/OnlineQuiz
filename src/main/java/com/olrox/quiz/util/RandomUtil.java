package com.olrox.quiz.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class RandomUtil {
    public static <E> List<E> pickNRandomElements(List<E> list, int n, Random random) {
        int length = list.size();

        if (length <= n) {
            return list;
        }

        // We don't need to shuffle the whole list
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i, random.nextInt(i + 1));
        }

        return list.subList(length - n, length);
    }

    public static <E> List<E> pickNRandomElements(Collection<E> collection, int n) {
        return pickNRandomElements(new ArrayList<>(collection), n, ThreadLocalRandom.current());
    }
}
