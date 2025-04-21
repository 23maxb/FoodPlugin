package max.plugin.foodPlugin2;

import com.google.gson.JsonArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

public class GenerateTexturePack {
    public static final String breadJSON = "SampleTexturePack/FoodTexturePack/assets/minecraft/items/bread.json";
    public static final String sandwichJSON = "SampleTexturePack/FoodTexturePack/assets/minecraft/models/item/SteakSandwich.json";

    public static void main(String[] args) throws IOException {
        JsonArray temp = new JsonArray();
        temp.add(readFileAsJSON("FoodDefinitions/sushi.json").getAsJsonObject());
        makeFolders("hi");
        Files.delete(Path.of("hi"));
        writeJSONS("hi", temp, Path.of("textures"));
    }

    public static List<Recipe> generateRecipes(JsonArray foodItems) {
        Map<String, ItemStack> customItems = new HashMap<>();
        for (JsonElement foodItem : foodItems) {
            JsonObject recipeJson = foodItem.getAsJsonObject();

            //create id field if not already declared
            if (!recipeJson.has("id")) {
                recipeJson.addProperty("id", nameToID(recipeJson.get("name").getAsString()));
            }

            //create prototype item
            ItemStack item = new ItemStack(Material.BREAD, 1);
            ItemMeta meta = item.getItemMeta();

            //metadata stuff
            //Lore
            ArrayList<String> lore = new ArrayList<>();
            recipeJson.get("lore").getAsJsonArray().forEach(e -> {
                lore.add(e.getAsString());
            });
            assert meta != null;
            meta.setLore(lore);

            //Name
            meta.setDisplayName(recipeJson.get("name").getAsString());

            //Custom model data for the skin and the hunger calculations later
            CustomModelDataComponent newCustomModelDataComponent = meta.getCustomModelDataComponent();
            List<String> strings = new ArrayList<>(newCustomModelDataComponent.getStrings());
            newCustomModelDataComponent.setStrings(strings);
            meta.setCustomModelDataComponent(newCustomModelDataComponent);

            // Set item meta since meta is done
            item.setItemMeta(meta);
            customItems.put(recipeJson.get("id").getAsString(), item);
        }
        List<Recipe> toReturn = new ArrayList<>();
        //generate recipes
        for (JsonElement foodItem : foodItems) {
            JsonArray recipeJson = foodItem.getAsJsonObject().get("recipes").getAsJsonArray();
            for (JsonElement recipeJsonElement : recipeJson) {
                JsonObject recipeJsonObject = recipeJsonElement.getAsJsonObject();
                JsonArray keysJson = recipeJsonObject.get("keys").getAsJsonArray();
                Recipe newRecipe;
                //recipe types
                String recipeType = nameToID(recipeJsonObject.get("type").getAsString());
                if (recipeType.endsWith("recipe"))
                    recipeType = recipeType.substring(0, recipeType.length() - "recipe".length());
                switch (recipeType) {
                    case "blasting":
                        continue;
                    case "campfire":
                        continue;
                    case "furnace":
                        continue;
                    case "merchant":
                        continue;
                    case "shaped":
                        continue;
                    case "shapeless":
                        continue;
                    case "smithing":
                        continue;
                    case "smithingtransform":
                        continue;
                    case "smithingtrim":
                        continue;
                    case "smoking":
                        continue;
                    case "stonecutting":
                        continue;
                    case "transmute":
                        continue;

                }
                toReturn.add(newRecipe);
            }

        }
        return toReturn;
    }


    public static Recipe generateRecipe(JsonElement json) {
        ItemStack SteakSandwich = new ItemStack(Material.BREAD, 1);
        ItemMeta old = SteakSandwich.getItemMeta();
        old.setDisplayName("Steak Sandwich");
        old.setLore(List.of("This sandwich has steak!", "Yummy!"));
        CustomModelDataComponent custom = old.getCustomModelDataComponent();

        List<String> a = new ArrayList<String>(custom.getStrings());
        System.out.println(a);
        a.add("Steak Sandwich");
        custom.setStrings(a);
        old.setCustomModelDataComponent(custom);
        SteakSandwich.setItemMeta(old);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "SteakSandwich"),
                SteakSandwich);

        recipe.shape("B", "S", "B");
        recipe.setIngredient('S', Material.COOKED_BEEF);
        recipe.setIngredient('B', Material.BREAD);

        //return recipe;
        return null;
    }

    public static void makeFolders(String packName) {
        if (!new File(packName).mkdirs()) {
            System.out.println(packName + "is already a pack. Try a different name or delete the old one.");
        } else {
            if (
                    new File(packName + "/assets/minecraft/items").mkdirs()
                            && new File(packName + "/assets/minecraft/models/item").mkdirs()
                            && new File(packName + "/assets/minecraft/textures/item").mkdirs()
            ) {
                System.out.println(packName + " interior folders created successfully.");
            }
        }
    }

    public static void writeJSONS(String packName, JsonArray foodToAdd) throws IOException {
        writeJSONS(packName, foodToAdd, null);
    }

    public static void writeJSONS(String packName, JsonArray foodToAdd, Path imageDirectory) throws IOException {
        Path minecraftPath = Path.of(packName + "/assets/minecraft");
        if (Files.exists(minecraftPath)) {
            JsonElement sampleBreadJSON = readFileAsJSON(breadJSON);
            JsonElement casesPrototype = sampleBreadJSON.getAsJsonObject().get("model").getAsJsonObject().get("cases").getAsJsonArray().get(0);
            JsonElement sampleSandwichJSON = readFileAsJSON(sandwichJSON);
            JsonArray cases = new JsonArray();
            for (JsonElement food : foodToAdd) {
                JsonObject foodJson = food.getAsJsonObject();

                //Create Cases for mc/items/json
                JsonObject newCase = casesPrototype.deepCopy().getAsJsonObject();
                newCase.addProperty("when", foodJson.get("name").getAsString());
                if (!foodJson.has("id"))
                    foodJson.addProperty("id", nameToID(foodJson.get("name").getAsString()));
                newCase.get("model").getAsJsonObject().addProperty("model", "item/" + foodJson.get("id").getAsString());
                cases.add(newCase);

                //create mappings for models/item/json
                JsonElement temp = sampleSandwichJSON.deepCopy();
                temp.getAsJsonObject().get("textures").getAsJsonObject().addProperty("layer0", "item/" + foodJson.get("id").getAsString());
                File foodResourceDeclaration = new File(packName + "/assets/minecraft/models/item/" + foodJson.get("id").getAsString() + ".json");
                if (foodResourceDeclaration.createNewFile()) {
                    FileWriter fw = new FileWriter(foodResourceDeclaration);
                    fw.write(temp.toString());
                    fw.close();
                } else {
                    throw new RuntimeException("Failed to crate file" + foodResourceDeclaration.getAbsolutePath());
                }

                //add texture
                Path sourceImage = imageDirectory.resolve(foodJson.get("id").getAsString() + ".png");
                Path target = Path.of(packName + "/assets/minecraft/textures/item").resolve(foodJson.get("id").getAsString() + ".png");
                if (Files.exists(sourceImage)) {
                    Files.copy(sourceImage, target);
                } else {
                    System.out.println("Couldn't find source image for " + foodJson.get("id").getAsString() + ". I looked at " + sourceImage);
                }
            }

            //handle writing cases
            JsonObject casesJson = sampleBreadJSON.getAsJsonObject().get("model").getAsJsonObject();
            casesJson.remove("cases");
            casesJson.add("cases", cases);
            File realBreadJson = new File(packName + "/assets/minecraft/items/bread.json");
            if (realBreadJson.createNewFile()) {
                FileWriter fw = new FileWriter(realBreadJson);
                fw.write(sampleBreadJSON.toString());
                fw.close();
            } else {
                throw new IOException("tried to write to " + realBreadJson.getAbsolutePath() + " but it already exists.");
            }

        } else
            throw new RuntimeException(minecraftPath + " folder not found.");
    }

    public static JsonArray getFoodItems(JsonArray foodToAdd) {
        return null;
    }

    public static String nameToID(String name) {
        return name.trim().toLowerCase().replaceAll(" ", "");
    }

    public static JsonElement readFileAsJSON(Path p) throws IOException {
        return JsonParser.parseString(Files.readString(p));
    }

    public static JsonElement readFileAsJSON(String s) throws IOException {
        return readFileAsJSON(Path.of(s));
    }

}