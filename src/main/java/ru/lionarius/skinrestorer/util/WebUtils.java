package ru.lionarius.skinrestorer.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class WebUtils {
    private static final String API = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String SESSION_SERVER = "https://sessionserver.mojang.com/session/minecraft/profile/";

    public static UUID getUUID(String name) throws IOException {
        return UUID.fromString(getJsonFrom(new URL(API + name)).get("id").getAsString()
                .replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
    }

    public static Property getSkin(UUID uuid) throws IOException {
        JsonObject texture = getJsonFrom(new URL(SESSION_SERVER + uuid.toString() + "?unsigned=false")).getAsJsonArray("properties").get(0).getAsJsonObject();

        return new Property(texture.get("name").getAsString(), texture.get("value").getAsString(), texture.get("signature").getAsString());
    }

    private static JsonObject getJsonFrom(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JsonParser parser = new JsonParser();

        return parser.parse(response.toString()).getAsJsonObject();
    }
}
