package org.deng.littledengserver.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class BirthDateCalculator {
    /**
     * 根据出生日期计算年龄
     * @param birthDate 出生日期
     * @return 年龄
     */
    public static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("出生日期不能为null");
        }

        LocalDate today = LocalDate.now();

        return Period.between(birthDate, today).getYears();
    }

    /**
     * 根据出生日期计算生肖
     * @param birthDate 出生日期
     * @return 生肖（如：鼠、牛、虎...）
     */
    public static String getChineseZodiac(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("出生日期不能为null");
        }

        int year = birthDate.getYear();
        // 1900年是鼠年，生肖顺序：鼠、牛、虎、兔、龙、蛇、马、羊、猴、鸡、狗、猪
        String[] zodiacs = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        return zodiacs[(year - 1900) % 12];
    }

    /**
     * 根据出生日期计算星座
     * @param birthDate 出生日期
     * @return 星座（如：白羊座、金牛座...）
     */
    public static String getZodiacSign(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("出生日期不能为null");
        }

        int month = birthDate.getMonthValue();
        int day = birthDate.getDayOfMonth();

        // 白羊座：3月21日 - 4月19日
        if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
            return "白羊座";
        }
        // 金牛座：4月20日 - 5月20日
        else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
            return "金牛座";
        }
        // 双子座：5月21日 - 6月21日
        else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
            return "双子座";
        }
        // 巨蟹座：6月22日 - 7月22日
        else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
            return "巨蟹座";
        }
        // 狮子座：7月23日 - 8月22日
        else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
            return "狮子座";
        }
        // 处女座：8月23日 - 9月22日
        else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
            return "处女座";
        }
        // 天秤座：9月23日 - 10月23日
        else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
            return "天秤座";
        }
        // 天蝎座：10月24日 - 11月22日
        else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
            return "天蝎座";
        }
        // 射手座：11月23日 - 12月21日
        else if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) {
            return "射手座";
        }
        // 摩羯座：12月22日 - 1月19日
        else if ((month == 12 && day >= 22) || (month == 1 && day <= 19)) {
            return "摩羯座";
        }
        // 水瓶座：1月20日 - 2月18日
        else if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
            return "水瓶座";
        }
        // 双鱼座：2月19日 - 3月20日
        else {
            return "双鱼座";
        }
    }

    /**
     * 计算距离下一次生日还有多少天
     * @param birthDate 出生日期
     * @return 距离下一次生日的天数
     */
    public static long getDaysToNextBirthday(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("出生日期不能为null");
        }

        LocalDate today = LocalDate.now();

        // 计算今年的生日
        LocalDate birthdayThisYear = LocalDate.of(today.getYear(),
                birthDate.getMonth(),
                birthDate.getDayOfMonth());

        // 如果今年的生日已过，则计算明年的生日
        if (birthdayThisYear.isBefore(today) || birthdayThisYear.isEqual(today)) {
            birthdayThisYear = birthdayThisYear.plusYears(1);
        }

        return Period.between(today, birthdayThisYear).getDays();
    }

    /**
     * 将Date转换为LocalDate
     */
    private static LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
