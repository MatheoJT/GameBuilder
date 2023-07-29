package net.altarise.gb.bedwars;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Team {


    private String name;
    private final String id;
    private String color;
    private byte byteColor;
    private Location spawn;
    private Location generator;
    private Location shop;
    private Location upgrade;
    private Location protectedArea1;
    private Location protectedArea2;
    private final List<String> beds = new ArrayList<>();


    public Team(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public byte getByteColor() {
        return byteColor;
    }

    public void setByteColor(byte byteColor) {
        this.byteColor = byteColor;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getGenerator() {
        return generator;
    }

    public void setGenerator(Location generator) {
        this.generator = generator;
    }

    public Location getShop() {
        return shop;
    }

    public void setShop(Location shop) {
        this.shop = shop;
    }

    public Location getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(Location upgrade) {
        this.upgrade = upgrade;
    }

    public Location getProtectedArea1() {
        return protectedArea1;
    }

    public void setProtectedArea1(Location protectedArea1) {
        this.protectedArea1 = protectedArea1;
    }

    public Location getProtectedArea2() {
        return protectedArea2;
    }

    public void setProtectedArea2(Location protectedArea2) {
        this.protectedArea2 = protectedArea2;
    }


    public void addBed(Location bed) {
        beds.add(bed.getWorld().getName() + ", " + bed.getX() + ", " + bed.getY() + ", " + bed.getZ());
    }

    public boolean isFinish() {
        return color != null && byteColor != 0 && spawn != null && generator != null && shop != null && upgrade != null && protectedArea1 != null && protectedArea2 != null && beds.size() == 2;
    }

    public String notSet() {
        if (color == null) return "color";
        if (byteColor == 0) return "byteColor";
        if (spawn == null) return "spawn";
        if (generator == null) return "generator";
        if (shop == null) return "shop";
        if (upgrade == null) return "upgrade";
        if (protectedArea1 == null) return "protectedArea1";
        if (protectedArea2 == null) return "protectedArea2";
        if (beds.size() != 2) return "beds";
        return null;
    }

    public List<String> getBeds() {
        return beds;
    }

    public String getName() {
        return name;
    }
}
