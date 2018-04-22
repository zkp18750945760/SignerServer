package utils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

import java.io.File;

/**
 * @author zhoukp
 * @time 2018/4/21 15:29
 * @email 275557625@qq.com
 * @function
 */
public class Word2Pdf {
    /**
     * word转pdf
     *
     * @param path     word路径
     * @param savePath PDF保存路径
     */
    public static String word2Pdf(String path, String savePath) {
        ActiveXComponent app = null;
        // 开始时间
        long start = System.currentTimeMillis();
        try {
            // 打开word
            app = new ActiveXComponent("Word.Application");
            // 设置word不可见,很多博客下面这里都写了这一句话，其实是没有必要的，因为默认就是不可见的，如果设置可见就是会打开一个word文档，对于转化为pdf明显是没有必要的
            //app.setProperty("Visible", false);
            // 获得word中所有打开的文档
            Dispatch documents = app.getProperty("Documents").toDispatch();
            System.out.println("打开文件: " + path);
            // 打开文档
            Dispatch document = Dispatch.call(documents, "Open", path, false, true).toDispatch();
            // 如果文件存在的话，不会覆盖，会直接报错，所以我们需要判断文件是否存在
            File target = new File(savePath);
            if (target.exists()) {
                target.delete();
            }
            System.out.println("另存为: " + savePath);
            // 另存为，将文档报错为pdf，其中word保存为pdf的格式宏的值是17
            Dispatch.call(document, "SaveAs", savePath, 17);
            // 关闭文档
            Dispatch.call(document, "Close", false);
            // 结束时间
            long end = System.currentTimeMillis();
            System.out.println("转换成功，用时：" + (end - start) + "ms");
            return savePath;
        } catch (Exception e) {
            System.out.println("转换失败" + e.getMessage());
            return "";
        } finally {
            // 关闭office
            if (app != null) {
                app.invoke("Quit", 0);
            }
        }
    }
}
