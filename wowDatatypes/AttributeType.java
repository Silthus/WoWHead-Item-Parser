package wowDatatypes;

/**
 * @author Silthus
 */
public enum AttributeType {

    AGILITY(3),
    STRENGTH(4),
    INTELLECT(5),
    SPIRIT(6),
    STAMINA(7),
    WEAPON_SKILL_RATING(11),
    DEFENSE_RATING(12),
    DODGE_RATING(13),
    PARRY_RATING(14),
    SHIELD_BLOCK_RATING(15),
    RANGED_CRITICAL_STRIKE_RATING(20),
    HIT_RATING(31),
    CRITICAL_STRIKE_RATING(32),
    EXPERTISE_RATING(34), //seems to be MASTERY_RATING(49) today
    RESILIENCE_RATING(35),
    HASTE_RATING(36),
    EXPERTISE_RATING_2(37),
    ATTACK_POWER(38),
    RANGED_ATTACK_POWER(39),
    MANA_REGENERATION(43),
    ARMOR_PENETRATION_RATING(44),
    SPELL_POWER(45),
    HEALTH_REGEN(46),
    SPELL_PENETRATION(47),
    BLOCK_VALUE(48),
    MASTERY_RATING(49),
    FIRE_RESISTANCE(51),
    FROST_RESISTANCE(52),
    HOLY_RESISTANCE(53),
    SHADOW_RESISTANCE(54),
    NATURE_RESISTANCE(55),
    ARCANE_RESISTANCE(56);

    private final int id;

    private AttributeType(int id) {

        this.id = id;
    }

    public int getId() {

        return id;
    }

    public static AttributeType fromId(int id) {

        for (AttributeType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
