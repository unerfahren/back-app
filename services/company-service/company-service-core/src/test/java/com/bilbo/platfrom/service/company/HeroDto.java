package com.bilbo.platfrom.service.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HeroDto {
    @JsonProperty("1")
    private AntiMage antiMag;

    @Data
    public static class AntiMage {
        private String name;
        private String displayName;
        private String shortName;
        private Stat stat;
    }

    @Data
    public static class Stat {
                private String attackType;
                private Double startingArmor;
                private Double startingMagicArmor;
                private Double startingDamageMin;
                private Double startingDamageMax;
                private Double attackRate;
                private Double attackAnimationPoint;
                private Double attackAcquisitionRange;
                private Double attackRange;
                private String AttributePrimary;
                private Double heroPrimaryAttribute;
                private Double strengthBase;
                private Double strengthGain;
                private Double intelligenceBase;
                private Double intelligenceGain;
                private Double agilityBase;
                private Double agilityGain;
                private Double hpRegen;
                private Double mpRegen;
                private Double moveSpeed;
                private Double moveTurnRate;
                private Double hpBarOffset;
                private Double visionDaytimeRange;
                private Double visionNighttimeRange;
                private Double complexity;
                private Double primaryAttributeEnum;
    }
}
