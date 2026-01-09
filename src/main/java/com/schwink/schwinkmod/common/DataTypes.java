package com.schwink.schwinkmod.common;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataTypes {

    public record ShuffleBagEntry(int amount, String type, String name) {

    }

    public static class BagInfo {
        private long seed;
        private int count;

        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }

        public long getSeed() {
            return seed;
        }
        public void setSeed(long seed) {
            this.seed = seed;
        }
    }

    public static class PlayerBagData {
        private boolean isDirty;
        private final Map<String,BagInfo> bags = new HashMap<String,BagInfo>();

        public void setDirty(boolean dirty) {
            isDirty = dirty;
        }
        public boolean isDirty() {
            return isDirty;
        }

        public Map<String, BagInfo> getBags() {
            return bags;
        }
        public void addToBags(String bagName, BagInfo bagInfo) {
            bags.put(bagName, bagInfo);
            setDirty(true);
        }
        public void removeFromBags(String bagName) {
            bags.remove(bagName);
            setDirty(true);
        }
        public void clearBags() {
            bags.clear();
            setDirty(true);
        }
        public void addToBagsKey(String bagName, BagInfo bagInfo) {
            setDirty(true);
            if (!bags.containsKey(bagName)) {
                bags.put(bagName, bagInfo);
            }
            else {
                bags.replace(bagName, bagInfo);
            }

        }

    }
}
