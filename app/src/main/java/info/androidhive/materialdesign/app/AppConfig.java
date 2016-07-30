package info.androidhive.materialdesign.app;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import info.androidhive.materialdesign.R;

public class AppConfig extends Application {

    // Server GET OTP
    public static String GET_OTP = "http://rkvacations.in/sm/webservices/getotp";

    // Server OTP Verification
    public static String OTP_VERIFICATION = "http://rkvacations.in/sm/webservices/verifyotp";

    // Server members From Login
    public static String GET_MEMBERS_FROM_LOGIN = "http://rkvacations.in/sm/webservices/getmyfamily";

    // Server Image Path
    public static String IMAGE_PATH = "http://rkvacations.in/sm/img/member/";

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_food)
                .showImageForEmptyUri(R.drawable.ic_food)
                .showImageOnFail(R.drawable.ic_food)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getBaseContext())
                .memoryCacheExtraOptions(480, 800)
                .diskCacheExtraOptions(480, 800, null)
                .writeDebugLogs()
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);
    }
}
