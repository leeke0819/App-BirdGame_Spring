package com.example.demo.service;

import lombok.Getter;

@Getter
public enum UserExperienceLevel {
    LV1(1, 1, 10),
    LV2(2, 11, 25),
    LV3(3, 26, 50),
    LV4(4, 51, 90),
    LV5(5, 91, 140),
    LV6(6, 141, 200),
    LV7(7, 201, 270),
    LV8(8, 271, 350),
    LV9(9, 351, 440),
    LV10(10, 441, 540),
    LV11(11, 541, 650),
    LV12(12, 651, 770),
    LV13(13, 771, 900),
    LV14(14, 901, 1040),
    LV15(15, 1041, 1190),
    LV16(16, 1191, 1350),
    LV17(17, 1351, 1520),
    LV18(18, 1521, 1700),
    LV19(19, 1701, 1890),
    LV20(20, 1891, 2090),
    LV21(21, 2091, 2300),
    LV22(22, 2301, 2520),
    LV23(23, 2521, 2750),
    LV24(24, 2751, 2990),
    LV25(25, 2991, 3240),
    LV26(26, 3241, 3500),
    LV27(27, 3501, 3770),
    LV28(28, 3771, 4050),
    LV29(29, 4051, 4340),
    LV30(30, 4341, 4640),
    LV31(31, 4641, 4950),
    LV32(32, 4951, 5270),
    LV33(33, 5271, 5600),
    LV34(34, 5601, 5940),
    LV35(35, 5941, 6290),
    LV36(36, 6291, 6650),
    LV37(37, 6651, 7020),
    LV38(38, 7021, 7400),
    LV39(39, 7401, 7790),
    LV40(40, 7791, 8190),
    LV41(41, 8191, 8600),
    LV42(42, 8601, 9020),
    LV43(43, 9021, 9450),
    LV44(44, 9451, 9890),
    LV45(45, 9891, 10340),
    LV46(46, 10341, 10800),
    LV47(47, 10801, 11270),
    LV48(48, 11271, 11750),
    LV49(49, 11751, 12240),
    LV50(50, 12241, 12740);

    private final int level;
    private final int minExp;
    private final int maxExp;

    UserExperienceLevel(int level, int minExp, int maxExp) {
        this.level = level;
        this.minExp = minExp;
        this.maxExp = maxExp;
    }

    public static UserExperienceLevel findByExp(int exp) {
        for (UserExperienceLevel level : values()) {
            if (exp >= level.getMinExp() && exp <= level.getMaxExp()) {
                return level;
            }
        }
        throw new IllegalArgumentException("경험치 범위에 해당하는 레벨이 없습니다: " + exp);
    }

}
