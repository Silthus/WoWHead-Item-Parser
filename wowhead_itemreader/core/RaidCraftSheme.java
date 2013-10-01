package wowhead_itemreader.core;

import wowDatatypes.AttributeType;
import wowDatatypes.EquipmentSlot;
import wowDatatypes.ItemClass;
import wowDatatypes.ItemSubClass;
import wowDatatypes.SpellStats;
import wowhead_itemreader.WoWHeadData;

/**
 * @author Silthus
 */
public class RaidCraftSheme extends BasicSheme {

    public RaidCraftSheme() {

        super(new String[]{""});
    }

    @Override
    public String createQuery(WoWHeadData data, String colums) {

        int itemId = data.itemId;
        String query = "SET FOREIGN_KEY_CHECKS = 0; BEGIN;\n" +
                "INSERT IGNORE INTO `rcitems_items` (id, name, minecraft_id, item_level, quality, sell_price, item_type, info) \n" +
                "VALUES (\n" +
                itemId + "," +
                "'" + data.name + "'," +
                getMinecraftId(data.itemClass, data.itemSubClass, data.itemQuality, data.inventoryType) + "," +
                data.itemLevel + "," +
                "'" + getItemQuality(data.itemQuality) + "'," +
                getSellPrice(data.sellPrice) + "," +
                "'" + getItemClass(data.itemClass) + "'," +
                "'WoW Import');\n" +
                "INSERT IGNORE INTO `rcitems_equipment` (id, item_id, equipment_slot, durability) \n" +
                "VALUES (\n" +
                itemId + "," +
                itemId + "," +
                "'" + getEquipmentSlot(data.slot) + "'," +
                data.durability + ");\n";
        if (getItemClass(data.itemClass) == ItemClass.ARMOR) {
            query += "INSERT IGNORE INTO `rcitems_armor` (equipment_id, armor_type, armor_value) \n" +
                    "VALUES (\n" +
                    itemId + "," +
                    "'" + getItemType(data.itemClass, data.itemSubClass) + "', " +
                    data.armor + ");\n";
        } else if (getItemClass(data.itemClass) == ItemClass.WEAPON) {
            query += "INSERT IGNORE INTO `rcitems_weapons` (equipment_id, weapon_type, min_damage, max_damage, swing_time) \n" +
                    "VALUES (\n" +
                    itemId + "," +
                    "'" + getItemType(data.itemClass, data.itemSubClass) + "', " +
                    data.dmg_min1 + ", " +
                    data.dmg_max1 + ", " +
                    data.attSpeed / 100.0 + ");\n";
        }
        // now lets parse the attributes
        for (SpellStats spellStats : data.spellStatsAr) {
            if (spellStats.getStatValue() > 0) {
                query += "INSERT IGNORE INTO `rcitems_equipment_attributes` (equipment_id, attribute, attribute_value) \n" +
                        "VALUES (\n" +
                        itemId + "," +
                        "'" + AttributeType.fromId(spellStats.getStatType()) + "'," +
                        spellStats.getStatValue() + ");\n";
            }
        }
        // also insert an item level requirement for every item
        if (data.reqLevel > 1) {
            query += "INSERT IGNORE INTO `rcitems_attachments` (item_id, attachment_name, provider_name) \n" +
                    "VALUES (\n" +
                    itemId + "," +
                    "'level'," +
                    "'skills.requirements');\n" +
                    "INSERT INTO `rcitems_attachment_data` (attachment_id, data_key, data_value) \n" +
                    "VALUES (\n" +
                    "LAST_INSERT_ID()," +
                    "'level'," +
                    data.reqLevel + ");\n";
        }
        query += "COMMIT;SET FOREIGN_KEY_CHECKS = 1;";
        return query;
    }

    // source of information: http://us.battle.net/wow/en/forum/topic/2973392418

    private int getMinecraftId(int itemClass, int itemSubClass, int itemQuality, int inventoryType) {

        ItemSubClass itemType = getItemType(itemClass, itemSubClass);
        switch (itemType) {
            case AXE:
            case TWO_HAND_AXE:
                switch (itemQuality) {
                    case 0:
                    case 1:
                        return 271;
                    case 2:
                        return 275;
                    case 3:
                        return 258;
                    case 4:
                        return 279;
                }
                break;
            case SWORD:
            case TWO_HAND_SWORD:
            case DAGGER:
                switch (itemQuality) {
                    case 0:
                    case 1:
                        return 268;
                    case 2:
                        return 272;
                    case 3:
                        return 267;
                    case 4:
                        return 276;
                }
                break;
            case BOW:
                return 261;
            case MAGIC_WAND:
                switch (itemQuality) {
                    case 0:
                    case 1:
                    case 2:
                        // stick
                        return 280;
                    case 3:
                    case 4:
                        // blaze rod
                        return 369;
                }
                break;
            case SHIELD:
                switch (itemQuality) {
                    case 0:
                    case 1:
                        // lower wooden door
                        return 64;
                    case 2:
                        // piston head
                        return 34;
                    case 3:
                        // one half of a bed
                        return 26;
                    case 4:
                        // lower iron door half
                        return 71;
                }
                break;
            case STAFF:
                switch (itemQuality) {
                    case 0:
                    case 1:
                        // wood hoe
                        return 290;
                    case 2:
                        return 291;
                    case 3:
                        return 292;
                    case 4:
                        return 293;
                }
                break;
            case FISHING_POLE:
                return 346;
            case POLEARM:
                switch (itemQuality) {
                    case 0:
                    case 1:
                        // wood shovel
                        return 269;
                    case 2:
                        return 273;
                    case 3:
                        return 256;
                    case 4:
                        return 277;
                }
                break;
            case CLOTH:
                switch (inventoryType) {
                    // head
                    case 1:
                        return 302;
                    // chest
                    case 5:
                        return 303;
                    // legs
                    case 7:
                        return 304;
                    // feet
                    case 8:
                        return 305;
                }
                break;
            case LEATHER:
                switch (inventoryType) {
                    // head
                    case 1:
                        return 298;
                    // chest
                    case 5:
                        return 299;
                    // legs
                    case 7:
                        return 300;
                    // feet
                    case 8:
                        return 301;
                }
                break;
            case MAIL:
                switch (inventoryType) {
                    // head
                    case 1:
                        return 306;
                    // chest
                    case 5:
                        return 307;
                    // legs
                    case 7:
                        return 308;
                    // feet
                    case 8:
                        return 309;
                }
                break;
            case PLATE:
                switch (inventoryType) {
                    // head
                    case 1:
                        return 310;
                    // chest
                    case 5:
                        return 311;
                    // legs
                    case 7:
                        return 312;
                    // feet
                    case 8:
                        return 313;
                }
                break;
        }
        return -1;
    }

    private ItemClass getItemClass(int itemClass) {

        switch (itemClass) {
            case 2:
                return ItemClass.WEAPON;
            case 4:
                return ItemClass.ARMOR;
        }
        return ItemClass.UNDEFINED;
    }

    private ItemSubClass getItemType(int itemClass, int itemSubClass) {

        switch (getItemClass(itemClass)) {
            case WEAPON:
                switch (itemSubClass) {
                    case 0:
                        return ItemSubClass.AXE;
                    case 1:
                        return ItemSubClass.TWO_HAND_AXE;
                    case 2:
                        return ItemSubClass.BOW;
                    case 4:
                        return ItemSubClass.MACE;
                    case 5:
                        return ItemSubClass.TWO_HAND_MACE;
                    case 6:
                        return ItemSubClass.POLEARM;
                    case 7:
                        return ItemSubClass.SWORD;
                    case 8:
                        return ItemSubClass.TWO_HAND_SWORD;
                    case 10:
                        return ItemSubClass.STAFF;
                    case 15:
                        return ItemSubClass.DAGGER;
                    case 19:
                        return ItemSubClass.MAGIC_WAND;
                    case 20:
                        return ItemSubClass.FISHING_POLE;
                }
                break;
            case ARMOR:
                switch (itemSubClass) {
                    case 1:
                        return ItemSubClass.CLOTH;
                    case 2:
                        return ItemSubClass.LEATHER;
                    case 3:
                        return ItemSubClass.MAIL;
                    case 4:
                        return ItemSubClass.PLATE;
                    case 6:
                        return ItemSubClass.SHIELD;
                }
                break;
        }
        return ItemSubClass.UNDEFINED;
    }

    private EquipmentSlot getEquipmentSlot(int id) {

        switch (id) {
            case 1:
                return EquipmentSlot.HEAD;
            case 5:
                return EquipmentSlot.CHEST;
            case 7:
                return EquipmentSlot.LEGS;
            case 8:
                return EquipmentSlot.FEET;
            case 13:
                return EquipmentSlot.ONE_HANDED;
            case 14:
                return EquipmentSlot.SHIELD_HAND;
            case 15:
                return EquipmentSlot.TWO_HANDED;
            case 17:
                return EquipmentSlot.TWO_HANDED;
            case 21:
                return EquipmentSlot.ONE_HANDED;
            case 22:
                return EquipmentSlot.SHIELD_HAND;
        }
        return EquipmentSlot.UNDEFINED;
    }

    private double getSellPrice(int sellPrice) {

        return sellPrice / 100.0;
    }

    private String getItemQuality(int data) {

        switch (data) {
            case 0:
                return "POOR";
            case 1:
                return "COMMON";
            case 2:
                return "UNCOMMON";
            case 3:
                return "RARE";
            case 4:
                return "EPIC";
            case 5:
                return "LEGENDARY";
            default:
                return "";
        }
    }

    @Override
    protected String getValueData(WoWHeadData data) {

        return "";
    }

    @Override
    public String getTableName() {

        return "rcitems_items";
    }
}
