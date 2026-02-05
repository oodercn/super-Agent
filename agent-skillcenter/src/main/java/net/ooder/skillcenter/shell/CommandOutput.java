package net.ooder.skillcenter.shell;

/**
 * 命令输出类，处理命令的输出
 */
public class CommandOutput {
    
    // ANSI 颜色代码
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    
    /**
     * 输出普通信息
     * @param message 信息内容
     */
    public void println(String message) {
        System.out.println(message);
    }
    
    /**
     * 输出成功信息
     * @param message 成功信息
     */
    public void success(String message) {
        System.out.println(ANSI_GREEN + "✓ " + message + ANSI_RESET);
    }
    
    /**
     * 输出错误信息
     * @param message 错误信息
     */
    public void error(String message) {
        System.err.println(ANSI_RED + "✗ " + message + ANSI_RESET);
    }
    
    /**
     * 输出警告信息
     * @param message 警告信息
     */
    public void warn(String message) {
        System.out.println(ANSI_YELLOW + "⚠ " + message + ANSI_RESET);
    }
    
    /**
     * 输出提示信息
     * @param message 提示信息
     */
    public void info(String message) {
        System.out.println(ANSI_BLUE + "ℹ " + message + ANSI_RESET);
    }
    
    /**
     * 输出调试信息
     * @param message 调试信息
     */
    public void debug(String message) {
        System.out.println(ANSI_CYAN + "🐛 " + message + ANSI_RESET);
    }
    
    /**
     * 输出不换行信息
     * @param message 信息内容
     */
    public void print(String message) {
        System.out.print(message);
    }
    
    /**
     * 输出表格
     * @param headers 表头
     * @param rows 行数据
     */
    public void table(String[] headers, String[][] rows) {
        if (headers == null || headers.length == 0) {
            return;
        }
        
        // 计算每列的宽度
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }
        
        for (String[] row : rows) {
            for (int i = 0; i < row.length && i < headers.length; i++) {
                if (row[i] != null && row[i].length() > widths[i]) {
                    widths[i] = row[i].length();
                }
            }
        }
        
        // 输出表头
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            sb.append(String.format("%-" + widths[i] + "s", headers[i]));
            if (i < headers.length - 1) {
                sb.append(" | ");
            }
        }
        println(sb.toString());
        
        // 输出分隔线
        sb.setLength(0);
        for (int i = 0; i < headers.length; i++) {
            for (int j = 0; j < widths[i]; j++) {
                sb.append('-');
            }
            if (i < headers.length - 1) {
                sb.append(" | ");
            }
        }
        println(sb.toString());
        
        // 输出行数据
        for (String[] row : rows) {
            sb.setLength(0);
            for (int i = 0; i < row.length && i < headers.length; i++) {
                String value = row[i] != null ? row[i] : "";
                sb.append(String.format("%-" + widths[i] + "s", value));
                if (i < headers.length - 1) {
                    sb.append(" | ");
                }
            }
            println(sb.toString());
        }
    }
}
