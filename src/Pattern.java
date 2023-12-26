public class Pattern {
    static java.util.regex.Pattern AC = java.util.regex.Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{4,15}$");
    static java.util.regex.Pattern PW = java.util.regex.Pattern.compile("^[a-zA-Z]\\w{5,17}$");

    /**
     * 检验账号格式正确
     *
     * @param regex 正则表达式AC->之后将整体修改为无参函数
     * @return true 匹配格式; false 不匹配格式
     */

    public static boolean checkAC(String regex) {
        return AC.matcher(regex).matches();
    }

    /**
     * 检验密码格式正确
     *
     * @param regex 正则表达式PW->之后将整体修改为无参函数
     * @return true 匹配格式; false 不匹配格式
     */
    public static boolean checkPW(String regex) {
        return PW.matcher(regex).matches();
    }
}
