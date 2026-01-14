package com.schwink.schwinkmod.server.shufflebag;

import com.schwink.schwinkmod.common.Config;
import com.schwink.schwinkmod.common.DataTypes.PlayerBagData;
import com.schwink.schwinkmod.common.DataTypes.BagInfo;
import com.schwink.schwinkmod.common.DataTypes.ShuffleBagEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jline.utils.Log;

import java.util.*;
import java.util.random.RandomGenerator;

public class ShuffleBagManager {

    public static final ShuffleBagManager INSTANCE = new ShuffleBagManager();

    public void dropItemFromBag(ServerLevel level, BlockPos pos, ServerPlayer player, String bagName){

        ItemStack stack = pickItemFromBag(player, bagName);

        if (stack.isEmpty()) return;

        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
    }

    private ItemStack pickItemFromBag(ServerPlayer player, String bagName){
        ItemStack result = ItemStack.EMPTY;
        UUID uuid = player.getUUID();

        PlayerBagData playerBagData = DatabaseManager.INSTANCE.currentPlayerBags.get(uuid);

        if (playerBagData == null || playerBagData.getBags().get(bagName) == null){
            playerBagData = generateBagData(bagName, playerBagData);
        }

        BagInfo currentBag = playerBagData.getBags().get(bagName);

        // now we find array of items, after we get current item from shuffle
        int indexOfItem = arrayFromSeed(currentBag.getSeed(),bagName)
                .get(currentBag.getCount());


        ShuffleBagEntry bagEntry = ShuffleBagJsonParser.INSTANCE.getShuffleBag(bagName).get(indexOfItem);

        if (bagEntry == null){
            Log.error("ETO ZALET KONKRETNII");
            return ItemStack.EMPTY;
        }

        if (bagEntry.type().equals("item")){
            ResourceLocation location = ResourceLocation.tryParse(bagEntry.name());

            if (location == null){
                Log.error("item name in JSON is INCORRECT");

                result = ItemStack.EMPTY;
            }
            else {
                Item item = BuiltInRegistries.ITEM.getValue(location);
                result = new ItemStack(item);
            }
        }

        if  (bagEntry.type().equals("shuffle_bag")){
            result = pickItemFromBag(player, bagEntry.name());
        }

        // increasing count of openings, if count is bigger than size of shuffle, set shuffle to 0 and regenerate seed
        playerBagData = increasedBagsCountData(playerBagData, bagName);
        DatabaseManager.INSTANCE.changeBagData(uuid, playerBagData);

        return result;
    }

    private ArrayList<Integer> arrayFromSeed(long seed, String bagName) {

        ArrayList<Integer> result = new ArrayList<>();
        List<ShuffleBagEntry> entries = ShuffleBagJsonParser.INSTANCE.getShuffleBag(bagName);

        for (ShuffleBagEntry entry : entries) {
            for (int i = 0; i < entry.amount(); i++){
                result.add(entries.indexOf(entry));
            }
        }

        RandomGenerator generator = new java.util.Random(seed);

        Collections.shuffle(result, generator);

        System.out.println(result);

        return result;
    }

    private long generateSeed(){
        long result;
        result = new Random().nextLong();
        return result;
    }

    public PlayerBagData generateBagData(String bagName, PlayerBagData data){

        if (data == null){
            data = new PlayerBagData();
        }

        BagInfo bagInfo = new BagInfo();

        bagInfo.setCount(0);
        bagInfo.setSeed(generateSeed());

        data.addToBags(bagName, bagInfo);

        return data;
    }

    private PlayerBagData increasedBagsCountData(PlayerBagData data, String bagName){

        int currentCount = data.getBags().get(bagName).getCount();

        if (currentCount >= ShuffleBagJsonParser.INSTANCE.getShuffleSize(bagName) - 1){
            currentCount = 0;

            data.getBags().get(bagName).setSeed(generateSeed());
            data.getBags().get(bagName).setCount(currentCount);
        }
        else{
            currentCount++;

            data.getBags().get(bagName).setCount(currentCount);
        }

        return data;
    }
}
