import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class EmailSender {
    // 配置信息
    private static final String EXCEL_FILE = "E:/游客.xlsx";
    private static final String SENDER_EMAIL = "your_email@example.com";  // 发件人邮箱
    private static final String SENDER_PASSWORD = "your_email_password";  // 发件人邮箱密码或授权码
    private static final String SMTP_SERVER = "smtp.example.com";  // SMTP服务器地址
    private static final int SMTP_PORT = 587;  // SMTP服务器端口

    // 邮件内容
    private static final String SUBJECT = "颐和园游览集合通知";
    private static final String MEETING_TIME = "2月3号下午2：00";
    private static final String MEETING_PLACE = "颐和园东门";
    private static final String CONTACT_PERSON = "李芳";

    // 邮件正文模板
    private static final String EMAIL_TEMPLATE = """
尊敬的客人：

您好！

我们诚挚地邀请您参加2月3号的颐和园游览活动。具体安排如下：

📅 集合时间：%s
📍 集合地点：%s
👤 联系人：%s

请您准时到达集合地点，我们会在那里等候您。如果您有任何疑问，请随时联系我们的联系人。

期待与您共度美好的一天！

祝好！
颐和园游览团队
""".formatted(MEETING_TIME, MEETING_PLACE, CONTACT_PERSON);

    public static void main(String[] args) {
        System.out.println("🚀 开始执行邮件发送程序...");
        System.out.println("📁 读取Excel文件：" + EXCEL_FILE);
        System.out.println("📧 发件人：" + SENDER_EMAIL);
        System.out.println("📅 集合时间：" + MEETING_TIME);
        System.out.println("📍 集合地点：" + MEETING_PLACE);
        System.out.println("👤 联系人：" + CONTACT_PERSON);
        System.out.println("=" .repeat(60));

        // 1. 读取Excel文件，获取邮箱地址
        List<String> emails = readExcelFile(EXCEL_FILE);

        if (emails.isEmpty()) {
            System.out.println("❌ 没有找到有效邮箱地址，程序退出");
            return;
        }

        System.out.println("📊 开始发送邮件，共 " + emails.size() + " 个邮箱地址");
        System.out.println("=" .repeat(60));

        // 2. 发送邮件
        int successCount = 0;
        int failureCount = 0;

        for (String email : emails) {
            if (sendEmail(email, SUBJECT, EMAIL_TEMPLATE)) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        // 3. 统计结果
        System.out.println("=" .repeat(60));
        System.out.println("📊 邮件发送统计");
        System.out.println("✅ 成功发送：" + successCount);
        System.out.println("❌ 发送失败：" + failureCount);
        System.out.println("📈 成功率：" + (successCount * 100.0 / emails.size()) + "%");
        System.out.println("=" .repeat(60));
        System.out.println("🎉 邮件发送程序执行完成！");
    }

    /**
     * 读取Excel文件，提取邮箱地址
     * @param file_path Excel文件路径
     * @return 邮箱地址列表
     */
    private static List<String> readExcelFile(String file_path) {
        List<String> emails = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(file_path));
             Workbook workbook = new XSSFWorkbook(fis)) {

            // 遍历所有工作表
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                System.out.println("📋 处理工作表：" + sheet.getSheetName());

                // 查找包含邮箱的列
                List<Integer> emailColumns = findEmailColumns(sheet);

                if (!emailColumns.isEmpty()) {
                    // 提取邮箱地址
                    for (int colIndex : emailColumns) {
                        extractEmailsFromColumn(sheet, colIndex, emails);
                    }
                }
            }

            // 去重
            emails = new ArrayList<>(new HashSet<>(emails));
            // 过滤有效邮箱
            List<String> validEmails = new ArrayList<>();
            for (String email : emails) {
                if (email.contains("@")) {
                    validEmails.add(email);
                }
            }

            System.out.println("✅ 成功读取Excel文件，找到 " + validEmails.size() + " 个有效邮箱地址");
            return validEmails;

        } catch (IOException e) {
            System.out.println("❌ 读取Excel文件失败：" + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 查找包含邮箱的列
     * @param sheet 工作表
     * @return 邮箱列索引列表
     */
    private static List<Integer> findEmailColumns(Sheet sheet) {
        List<Integer> emailColumns = new ArrayList<>();
        Row headerRow = sheet.getRow(0);

        if (headerRow != null) {
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    String cellValue = cell.toString().toLowerCase();
                    if (cellValue.contains("email") || cellValue.contains("mail") ||
                            cellValue.contains("邮箱") || cellValue.contains("邮件")) {
                        emailColumns.add(i);
                    }
                }
            }
        }

        return emailColumns;
    }

    /**
     * 从列中提取邮箱地址
     * @param sheet 工作表
     * @param colIndex 列索引
     * @param emails 邮箱列表
     */
    private static void extractEmailsFromColumn(Sheet sheet, int colIndex, List<String> emails) {
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell cell = row.getCell(colIndex);
                if (cell != null) {
                    String cellValue = cell.toString().trim();
                    if (!cellValue.isEmpty()) {
                        emails.add(cellValue);
                    }
                }
            }
        }
    }

    /**
     * 发送邮件
     * @param toEmail 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 是否发送成功
     */
    private static boolean sendEmail(String toEmail, String subject, String content) {
        try {
            // 配置邮件会话
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_SERVER);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            // 创建邮件
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(content);

            // 发送邮件
            Transport.send(message);

            System.out.println("✅ 邮件发送成功：" + toEmail);
            return true;

        } catch (Exception e) {
            System.out.println("❌ 邮件发送失败 " + toEmail + "：" + e.getMessage());
            return false;
        }
    }
}