package utils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.struts2.ServletActionContext;

import java.io.*;

public class FileUtils {
    /**
     * 上传文件
     *
     * @param uploadFile         文件
     * @param uploadFileFileName 文件名
     * @throws IOException
     */
    public static String uploadFile(File uploadFile, String uploadFileFileName, String savePath) throws IOException {
        //输入流
        InputStream is = new FileInputStream(uploadFile);
        //上传文件目录
        String uploadPath = ServletActionContext.getServletContext().getRealPath(savePath);
        //输出流
        OutputStream os = new FileOutputStream(new File(uploadPath, uploadFileFileName));
        byte[] buffer = new byte[1024000];//设置缓存
        int length = 0;
        //读取uploadFile文件输出到toFile文件中
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close(); // 关闭输入流
        os.close(); // 关闭输出流
        return uploadPath;
    }

    /**
     * 读取Excel表格数据
     *
     * @param inputStream InputStream
     * @return String[][]
     */
    public static String[][] readXlsFiles(InputStream inputStream, int beginIndex, int endIndex) {
        try {
            Workbook workbook = Workbook.getWorkbook(inputStream);
            //获得工作薄（Workbook）中工作表（Sheet）的个数，示例：
            int sheets = workbook.getNumberOfSheets();
            //获取第一张Sheet表
            Sheet sheet = workbook.getSheet(0);
            String sheetName = sheet.getName();
            System.out.println(sheets + "  " + sheetName);
            int clos = sheet.getColumns();//得到所有的列
            int rows = sheet.getRows();//得到所有的行

            String[][] values = new String[rows][clos];
            for (int i = beginIndex; i < rows - endIndex; i++) {
                for (int j = 0; j < clos; j++) {
                    Cell[] cells = sheet.getRow(i);
                    String value = cells[j].getContents();
                    values[i][j] = value;
                    System.out.println(value);
                }
            }
            return values;
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return null;
    }
}
