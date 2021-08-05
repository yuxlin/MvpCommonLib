package cn.kaer.common.bases;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 项目名称：
 * 类描述：UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。 如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框
 * 实现该接口并注册为程序中的默认未捕获异常处理 这样当未捕获异常发生时，就可以做些异常处理操作 例如：收集异常信息，发送错误报告 等。
 * <p/>
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
@SuppressWarnings("ALL")
public class CrashHandler implements UncaughtExceptionHandler {

    private static  String CRASH_SAVE_PATH = Environment.getExternalStorageDirectory() + "/Android/CrashLogs/";
    public static final String TAG = CrashHandler.class.getSimpleName();
    /**
     * 是否开启异常捕获
     */
    public static final boolean OPENCAUGHTEXCEPTION = true;
    /**
     * 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能
     */
    public static final boolean DEBUG = true;

    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE;

    private static String localFileUrl = "";
    /**
     * 程序的Context对象
     */
    private Context mContext;

    /**
     * 程序要重新拉起的Activity
     */


    /**
     * infos
     */
    private Map<String, String> infos = new LinkedHashMap<String, String>();

    /**
     * formatter
     */
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        init(ctx,"sdcard/Android/"+ctx.getPackageName()+"/"+"crashLog/");
    }

    public void init(Context context,String crashDir){
        CRASH_SAVE_PATH = crashDir;
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

    }
    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG,"产生异常");
        handleException(ex);

        //mDefaultHandler.uncaughtException(thread, ex);
        SystemClock.sleep(1000);

        System.exit(1);
        android.os.Process.killProcess(android.os.Process.myPid());

//        Intent intent = new Intent(mContext, DXAds_Activity.class);



        /*        PendingIntent restartIntent = PendingIntent.getActivity(
                mContext, 0, mIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        //退出程序
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 000,
                restartIntent); // 1秒钟后重启应用
        LogUtils.e(TAG, "杀死旧进程！");*/

    }

    private void handleException(final Throwable ex) {
        if (ex == null)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "Sorry,the program has an exception and is about to exit!", Toast.LENGTH_LONG).show();
                //LogUtils.e(TAG,"程序出现异常,即将退出");
                collectDeviceInfo(mContext);
                //LogUtils.e(TAG,"收集崩溃信息！");
                writeCrashInfoToFile(ex);

                Looper.loop();

            }
        }).start();

    }

    /**
     * @param ctx 手机设备相关信息
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("packageName", pi.packageName);
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                infos.put("crashTime", formatter.format(new Date()));
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
    }


    /**
     * @param ex 将崩溃写入文件系统
     */
    private void writeCrashInfoToFile(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }


        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        Log.e(STACK_TRACE, result == null ? "" : result);
        //这里把刚才异常堆栈信息写入SD卡的Log日志里面
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdcardPath = CRASH_SAVE_PATH;
            localFileUrl = writeLog(sb.toString(), sdcardPath);
        }
    }

    /**
     * @param log
     * @param name
     * @return 返回写入的文件路径
     * 写入Log信息的方法，写入到SD卡里面
     */
    private String writeLog(String log, String name) {
        String str = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());


        String filename = "crash-" + str + ".txt";
        File crashDirectory = new File(name);
        if (!crashDirectory.exists()) {
            crashDirectory.mkdirs();
        }
        File file = new File(crashDirectory.getPath(), filename);
        try {

            FileOutputStream stream = new FileOutputStream(file);
            OutputStreamWriter output = new OutputStreamWriter(stream);
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            //写入相关Log到文件
            bw.write(log);
            bw.newLine();
            bw.close();
            fw.close();

            /////////
            String packageName = infos.get("packageName");
            String versionName = infos.get("versionName");
            String disPlay = infos.get("DISPLAY");
            List<File> files = new ArrayList<>();
            files.clear();
            files.add(file);
        /*    SendMailUtil.sendFileMail(null, packageName+"_V"+versionName,
                    "crashBug_"+disPlay, files);*/
            ////////

            return filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}