package com.schwink.schwinkmod.server.shufflebag;

import com.schwink.schwinkmod.common.Config;
import com.schwink.schwinkmod.common.DataTypes.PlayerBagData;
import com.schwink.schwinkmod.common.DataTypes.BagInfo;
import com.schwink.schwinkmod.common.DataTypes.ShuffleBagEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jline.utils.Log;
import org.joml.Random;

import java.util.*;
import java.util.random.RandomGenerator;

public class ShuffleBagManager {

    public static final ShuffleBagManager INSTANCE = new ShuffleBagManager();

    public void dropItemFromBag(ServerLevel level, BlockPos pos, ServerPlayer player, String bagName){

        BlockState state = level.getBlockState(pos);

        ItemStack stack = pickItemFromBag(player, bagName);

        if (stack.isEmpty()) return;

        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
    }

    private ItemStack pickItemFromBag(ServerPlayer player, String bagName){
        ItemStack result = ItemStack.EMPTY;
        UUID uuid = player.getUUID();

        PlayerBagData playerBagData = DatabaseManager.INSTANCE.currentPlayerBags.get(uuid);

        if(playerBagData == null){
            playerBagData = generatePlayerBagData(bagName);
        }

        //Тут я понял, что как-то неправильно храню данные
        int indexOfItem = arrayFromSeed(playerBagData.getBags().get(bagName).getSeed(),bagName)
                .indexOf(playerBagData.getBags().get(bagName).getCount());


        ShuffleBagEntry bagEntry =  ShuffleBagJsonParser.INSTANCE.getShuffleBag(bagName).get(indexOfItem);

        if (bagEntry == null){
            Log.error("ETO ZALET KONKRETNII");
            return ItemStack.EMPTY;
        }

        if (bagEntry.type().equals("item")){
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath("minecraft", bagEntry.name());
            Item item = Registry.;
            result = bagEntry.name();
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
        return Random.newSeed();
    }

    private PlayerBagData generatePlayerBagData(String bagName){
        PlayerBagData data = new PlayerBagData();
        BagInfo bagInfo = new BagInfo();

        bagInfo.setCount(0);
        bagInfo.setSeed(generateSeed());

        data.addToBags(bagName, bagInfo);

        return data;
    }

    private PlayerBagData increasedBagsCountData(PlayerBagData data, String bagName){

        int currentCount = data.getBags().get(bagName).getCount();

        if (currentCount >= ShuffleBagJsonParser.INSTANCE.getShuffleSize(bagName)){
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
