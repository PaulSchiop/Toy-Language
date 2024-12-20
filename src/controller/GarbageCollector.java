package controller;

import model.values.IValue;
import model.values.RefValue;

import java.util.*;
import java.util.stream.Collectors;

public class GarbageCollector {

    public static Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toList());
    }

    public static List<Integer> getTransitiveAddresses(List<Integer> addresses, Map<Integer, IValue> heap) {
        Set<Integer> result = new HashSet<>(addresses);
        boolean changed;
        do {
            Set<Integer> newAddresses = heap.entrySet().stream()
                    .filter(e -> result.contains(e.getKey()))
                    .map(Map.Entry::getValue)
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue) v).getAddress())
                    .filter(addr -> !result.contains(addr))
                    .collect(Collectors.toSet());

            changed = result.addAll(newAddresses);
        } while (changed);

        return result.stream().toList();
    }
}
