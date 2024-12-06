package controller;

import model.values.IValue;
import model.values.RefValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GarbageCollector {

    public static Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey())) // Keep only referenced addresses
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue) // Filter values that are references
                .map(v -> ((RefValue) v).getAddress()) // Extract addresses
                .collect(Collectors.toList());
    }

    public static List<Integer> getTransitiveAddresses(List<Integer> addresses, Map<Integer, IValue> heap) {
        Set<Integer> result = addresses.stream().collect(Collectors.toSet());
        boolean changed;

        do {
            changed = false;
            Set<Integer> newAddresses = heap.entrySet().stream()
                    .filter(e -> result.contains(e.getKey())) // Look for currently known addresses
                    .map(Map.Entry::getValue)
                    .filter(v -> v instanceof RefValue) // Extract references from values
                    .map(v -> ((RefValue) v).getAddress()) // Extract the addresses
                    .filter(addr -> !result.contains(addr)) // Keep only new addresses
                    .collect(Collectors.toSet());

            changed = result.addAll(newAddresses);
        } while (changed);

        return result.stream().toList();
    }
}
