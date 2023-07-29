package net.altarise.gb.utils;


import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;
    private Material material;
    private int amount;
    private MaterialData data;
    private short damage;
    private Map<Enchantment, Integer> enchantments;
    private String displayname;
    private List<String> lore;
    private List<ItemFlag> flags;
    private Consumer<Player> onClick;
    private PotionEffect mainEffect;
    private boolean andSymbol;
    private boolean unsafeStackSize;
    private boolean unbreakable;

    public ItemBuilder(Material material) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap();
        this.lore = new ArrayList();
        this.flags = new ArrayList();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        if (material == null) {
            material = Material.AIR;
        }

        this.item = new ItemStack(material);
        this.material = material;
        this.unbreakable = false;
    }

    public ItemBuilder(Material material, int amount) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap();
        this.lore = new ArrayList();
        this.flags = new ArrayList();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        if (material == null) {
            material = Material.AIR;
        }

        if (amount > material.getMaxStackSize() || amount <= 0) {
            amount = 1;
        }

        this.amount = amount;
        this.item = new ItemStack(material, amount);
        this.material = material;
    }

    public ItemBuilder(Material material, int amount, String displayname) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap();
        this.lore = new ArrayList();
        this.flags = new ArrayList();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        if (material == null) {
            material = Material.AIR;
        }

        Validate.notNull(displayname, "The displayname is null.");
        this.item = new ItemStack(material, amount);
        this.material = material;
        if (amount > material.getMaxStackSize() || amount <= 0) {
            amount = 1;
        }

        this.amount = amount;
        this.displayname = displayname;
    }

    public ItemBuilder(Material material, String displayname) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap();
        this.lore = new ArrayList();
        this.flags = new ArrayList();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        if (material == null) {
            material = Material.AIR;
        }

        Validate.notNull(displayname, "The displayname is null.");
        this.item = new ItemStack(material);
        this.material = material;
        this.displayname = displayname;
    }

    public ItemBuilder(ItemStack item) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap();
        this.lore = new ArrayList();
        this.flags = new ArrayList();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        Validate.notNull(item, "The item is null.");
        this.item = item;
        this.material = item.getType();
        this.amount = item.getAmount();
        this.data = item.getData();
        this.damage = item.getDurability();
        this.enchantments = item.getEnchantments();
        if (item.hasItemMeta()) {
            this.meta = item.getItemMeta();
            this.displayname = item.getItemMeta().getDisplayName();
            if (item.getItemMeta().hasLore()) {
                this.lore = item.getItemMeta().getLore();
            }

            this.flags.addAll(item.getItemMeta().getItemFlags());
        }

    }

    public ItemBuilder(FileConfiguration cfg, String path) {
        this(cfg.getItemStack(path));
    }

    /** @deprecated */
    @Deprecated
    public ItemBuilder(ItemBuilder builder) {
        this.material = Material.STONE;
        this.amount = 1;
        this.damage = 0;
        this.enchantments = new HashMap();
        this.lore = new ArrayList();
        this.flags = new ArrayList();
        this.andSymbol = true;
        this.unsafeStackSize = false;
        Validate.notNull(builder, "The ItemBuilder is null.");
        this.item = builder.item;
        this.meta = builder.meta;
        this.material = builder.material;
        this.amount = builder.amount;
        this.damage = builder.damage;
        this.data = builder.data;
        this.enchantments = builder.enchantments;
        this.displayname = builder.displayname;
        this.lore = builder.lore;
        this.flags = builder.flags;
    }

    public ItemBuilder amount(int amount) {
        if ((amount > this.material.getMaxStackSize() || amount <= 0) && !this.unsafeStackSize) {
            amount = 1;
        }

        this.amount = amount;
        return this;
    }

    public ItemBuilder data(MaterialData data) {
        Validate.notNull(data, "The data is null.");
        this.data = data;
        return this;
    }

    /** @deprecated */
    @Deprecated
    public ItemBuilder damage(short damage) {
        this.damage = damage;
        return this;
    }

    public ItemBuilder durability(short damage) {
        this.damage = damage;
        return this;
    }

    public ItemBuilder unbreakable(boolean option) {
        this.unbreakable = option;
        return this;
    }

    public ItemBuilder material(Material material) {
        Validate.notNull(material, "The material is null.");
        this.material = material;
        return this;
    }

    public ItemBuilder meta(ItemMeta meta) {
        Validate.notNull(meta, "The meta is null.");
        this.meta = meta;
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        Validate.notNull(enchant, "The Enchantment is null.");
        this.enchantments.put(enchant, level);
        return this;
    }

    public ItemBuilder enchant(Map<Enchantment, Integer> enchantments) {
        Validate.notNull(enchantments, "The enchantments are null.");
        this.enchantments = enchantments;
        return this;
    }


    public ItemBuilder displayname(String displayname) {
        Validate.notNull(displayname, "The displayname is null.");
        this.displayname = this.andSymbol ? ChatColor.translateAlternateColorCodes('&', displayname) : displayname;
        return this;
    }

    public ItemBuilder lore(String line) {
        Validate.notNull(line, "The line is null.");
        this.lore.add(this.andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        Validate.notNull(lore, "The lores are null.");
        this.lore = lore;
        return this;
    }

    /** @deprecated */
    @Deprecated
    public ItemBuilder lores(String... lines) {
        Validate.notNull(lines, "The lines are null.");
        String[] var2 = lines;
        int var3 = lines.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            this.lore(this.andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        }

        return this;
    }

    public ItemBuilder lore(String... lines) {
        Validate.notNull(lines, "The lines are null.");
        String[] var2 = lines;
        int var3 = lines.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            this.lore(this.andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        }

        return this;
    }

    public ItemBuilder lore(String line, int index) {
        Validate.notNull(line, "The line is null.");
        this.lore.set(index, this.andSymbol ? ChatColor.translateAlternateColorCodes('&', line) : line);
        return this;
    }

    public ItemBuilder flag(ItemFlag flag) {
        Validate.notNull(flag, "The flag is null.");
        this.flags.add(flag);
        return this;
    }

    public ItemBuilder flag(List<ItemFlag> flags) {
        Validate.notNull(flags, "The flags are null.");
        this.flags = flags;
        return this;
    }

    public ItemBuilder disableFlags() {
        this.flags.addAll((Collection)Arrays.stream(ItemFlag.values()).filter((tag) -> {
            return tag.toString().contains("HIDE");
        }).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder glow() {
        this.enchant(this.material != Material.BOW ? Enchantment.ARROW_INFINITE : Enchantment.LUCK, 10);
        this.flag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder headOwnerName(String playerName) {
        Validate.notNull(playerName, "The owner of the head is null.");
        if (this.material == Material.SKULL_ITEM) {
            ((SkullMeta)this.meta).setOwner(playerName);
        }

        return this;
    }

    public ItemBuilder headOwnerTextures(String textures) {
        if (textures.isEmpty()) {
            return this;
        } else {
            this.headOwnerUrl(new String(Base64.getDecoder().decode(textures), StandardCharsets.UTF_8));
            return this;
        }
    }

    public ItemBuilder headOwnerUrl(String id) {
        if (id.isEmpty()) {
            return this;
        } else if (!(this.item.getItemMeta() instanceof SkullMeta)) {
            return this;
        } else {
            SkullMeta skullMeta = (SkullMeta)this.item.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            byte[] data = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "https://textures.minecraft.net/texture/" + id).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(data)));

            try {
                Field field = skullMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(skullMeta, profile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException var6) {
                var6.printStackTrace();
            }

            this.item.setItemMeta(skullMeta);
            return this;
        }
    }

    /** @deprecated */
    @Deprecated
    public ItemBuilder replaceAndSymbol() {
        this.replaceAndSymbol(!this.andSymbol);
        return this;
    }

    public ItemBuilder replaceAndSymbol(boolean replace) {
        this.andSymbol = replace;
        return this;
    }

    public ItemBuilder toggleReplaceAndSymbol() {
        this.replaceAndSymbol(!this.andSymbol);
        return this;
    }

    public ItemBuilder unsafeStackSize(boolean allow) {
        this.unsafeStackSize = allow;
        return this;
    }

    public ItemBuilder mainPotionEffect(PotionEffectType type, int level, int duration) {
        this.mainEffect = new PotionEffect(type, duration, level);
        return this;
    }

    public ItemBuilder toggleUnsafeStackSize() {
        this.unsafeStackSize(!this.unsafeStackSize);
        return this;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public int getAmount() {
        return this.amount;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

    /** @deprecated */
    @Deprecated
    public short getDamage() {
        return this.damage;
    }

    public short getDurability() {
        return this.damage;
    }

    public List<String> getLores() {
        return this.lore;
    }

    public boolean getAndSymbol() {
        return this.andSymbol;
    }

    public List<ItemFlag> getFlags() {
        return this.flags;
    }

    public Material getMaterial() {
        return this.material;
    }

    public ItemMeta getMeta() {
        return this.meta;
    }

    public MaterialData getData() {
        return this.data;
    }

    /** @deprecated */
    @Deprecated
    public List<String> getLore() {
        return this.lore;
    }

    public ItemBuilder toConfig(FileConfiguration cfg, String path) {
        cfg.set(path, this.build());
        return this;
    }

    public ItemBuilder fromConfig(FileConfiguration cfg, String path) {
        return new ItemBuilder(cfg, path);
    }

    public ItemBuilder onClick(Consumer<Player> action) {
        this.onClick = action;
        return this;
    }

    public static void toConfig(FileConfiguration cfg, String path, ItemBuilder builder) {
        cfg.set(path, builder.build());
    }

    public String toJson() {
        return (new Gson()).toJson(this);
    }

    public static String toJson(ItemBuilder builder) {
        return (new Gson()).toJson(builder);
    }

    public static ItemBuilder fromJson(String json) {
        return (ItemBuilder)(new Gson()).fromJson(json, ItemBuilder.class);
    }

    public ItemBuilder applyJson(String json, boolean overwrite) {
        ItemBuilder b = (ItemBuilder)(new Gson()).fromJson(json, ItemBuilder.class);
        if (overwrite) {
            return b;
        } else {
            if (b.displayname != null) {
                this.displayname = b.displayname;
            }

            if (b.data != null) {
                this.data = b.data;
            }

            if (b.material != null) {
                this.material = b.material;
            }

            if (b.lore != null) {
                this.lore = b.lore;
            }

            if (b.enchantments != null) {
                this.enchantments = b.enchantments;
            }

            if (b.item != null) {
                this.item = b.item;
            }

            if (b.flags != null) {
                this.flags = b.flags;
            }

            this.damage = b.damage;
            this.amount = b.amount;
            return this;
        }
    }

    public ItemStack build() {


        this.item.setType(this.material);
        this.item.setAmount(this.amount);
        this.item.setDurability(this.damage);
        this.meta = this.item.getItemMeta();
        if (this.data != null) {
            this.item.setData(this.data);
        }

        if (this.enchantments.size() > 0) {
            this.item.addUnsafeEnchantments(this.enchantments);
        }

        if (this.displayname != null) {
            this.meta.setDisplayName(this.displayname);
        }

        if (this.lore.size() > 0) {
            this.meta.setLore(this.lore);
        }

        if (this.flags.size() > 0) {
            Iterator var1 = this.flags.iterator();

            while(var1.hasNext()) {
                ItemFlag f = (ItemFlag)var1.next();
                this.meta.addItemFlags(new ItemFlag[]{f});
            }
        }

        if (this.mainEffect != null && this.material == Material.POTION) {
            PotionMeta potionMeta = (PotionMeta)this.meta;
            potionMeta.addCustomEffect(this.mainEffect, true);
        }

        this.item.setItemMeta(this.meta);
        return this.item;
    }
}
