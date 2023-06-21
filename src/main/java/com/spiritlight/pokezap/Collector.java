package com.spiritlight.pokezap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Collector {
    private static final OkHttpClient client = new OkHttpClient()
            .newBuilder().connectTimeout(60, TimeUnit.SECONDS).build();

    private static final String POKE_URL = "https://pokeapi.co/api/v2/pokemon?limit=2000";

    public static List<String> fetchNames() {
        Request request = new Request.Builder()
                .url(POKE_URL)
                .get()
                .addHeader("User-Agent", "SpiritLight/1.0")
                .build();

        try (Response response = client.newCall(request).execute()) {
            JsonObject object = (JsonObject) JsonParser.parseString(response.body().string());
            JsonArray array = object.getAsJsonArray("results");
            List<String> ret = new LinkedList<>();

            for (JsonElement element : array) {
                JsonObject obj = (JsonObject) element;
                if(!obj.has("name")) continue;
                String string = obj.get("name").getAsString();

                ret.add(title(string));
            }
            return ret;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String title(String value) {
        String[] str = value.split(" ");
        String[] res = new String[str.length];

        int idx = 0;
        for(String s : str) {
            res[idx++] = s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
        }

        return String.join(" ", res);
    }
}
