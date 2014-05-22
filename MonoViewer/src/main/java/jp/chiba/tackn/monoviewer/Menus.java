package jp.chiba.tackn.monoviewer;


import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import jp.chiba.tackn.monoviewer.man.DisclaimerActivity;
import jp.chiba.tackn.monoviewer.man.ManualActivity;
import jp.chiba.tackn.monoviewer.map.MapsActivity;
import jp.chiba.tackn.monoviewer.time.TimeTable;
import jp.chiba.tackn.monoviewer.train.TrainTable;

/**
 * 共通メニューを実行する
 * @author Takumi Ito
 * @since 2014/05/20
 */
public class Menus {
    public static boolean actionMenu(Context context,MenuItem item){
        switch (item.getItemId()){
            case R.id.action_back_home:
                Intent home = new Intent(context,MainActivity.class);
                context.startActivity(home);
                break;
            case R.id.action_map:
                Intent maps = new Intent(context,MapsActivity.class);
                context.startActivity(maps);
                break;
            case R.id.action_train:
                Intent train = new Intent(context,TrainTable.class);
                context.startActivity(train);
                break;
            case R.id.action_time:
                Intent time = new Intent(context,TimeTable.class);
                context.startActivity(time);
                break;
            case R.id.action_disclaimer:
                Intent disclaimer = new Intent(context,DisclaimerActivity.class);
                context.startActivity(disclaimer);
                break;
            case R.id.action_manual:
                Intent manual = new Intent(context, ManualActivity.class);
                context.startActivity(manual);
                break;
            case R.id.action_exit:
                Intent exit = new Intent(context, ExitActivity.class);
                context.startActivity(exit);
                break;
            default:
                return true;
        }
        return false;

    }
}
