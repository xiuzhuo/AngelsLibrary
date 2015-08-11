package angel.zhuoxiu.util;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zxui on 11/08/15.
 */
public class ToolbarUtils {
    public static TextView getToolbarTitleView(AppCompatActivity activity, Toolbar toolbar){
        ActionBar actionBar = activity.getSupportActionBar();
        CharSequence actionbarTitle = null;
        if(actionBar != null)
            actionbarTitle = actionBar.getTitle();
        actionbarTitle = TextUtils.isEmpty(actionbarTitle) ? toolbar.getTitle() : actionbarTitle;
        if(TextUtils.isEmpty(actionbarTitle)) return null;
        // can't find if title not set
        for(int i= 0; i < toolbar.getChildCount(); i++){
            View v = toolbar.getChildAt(i);
            if(v != null && v instanceof TextView){
                TextView t = (TextView) v;
                CharSequence title = t.getText();
                if(!TextUtils.isEmpty(title) && actionbarTitle.equals(title) && t.getId() == View.NO_ID){
                    //Toolbar does not assign id to views with layout params SYSTEM, hence getId() == View.NO_ID
                    //in same manner subtitle TextView can be obtained.
                    return t;
                }
            }
        }
        return null;
    }
}
