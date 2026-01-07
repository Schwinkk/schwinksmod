package com.schwink.schwinkmod.server.shufflebag;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.schwink.schwinkmod.common.Config;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;
import org.checkerframework.common.reflection.qual.GetClass;
import org.jline.utils.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShuffleBagJsonParser {

    public static final ShuffleBagJsonParser INSTANCE = new ShuffleBagJsonParser();

    public Map<String, List<ShuffleBagEntry>> bags = new HashMap<>();

    // if we dont want to let player know chances from jar, we need to save config in run/config and read it.

    public void parse() throws IOException {
        try (InputStream stream = getClass().getResourceAsStream("/data/schwinkmod/gachafiles/shufflebags.json")){

            if (stream == null) {
                Log.error("Failed to load shufflebags.json");
                return;
            }
            else {
                InputStreamReader reader = new InputStreamReader(stream);

                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, List<ShuffleBagEntry>>>(){}.getType();

                bags = gson.fromJson(reader, type);

                System.out.println(bags.get("tree_bag").getFirst().getAmount());
            }
        }
    }
}

