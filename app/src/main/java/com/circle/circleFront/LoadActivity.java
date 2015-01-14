package com.circle.circleFront;


import utilHelper.UtilAssist;
import com.circle.circleFront.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;


public class LoadActivity extends Activity implements AnimationListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        TextView title = (TextView) findViewById(R.id.TextView1);
        Animation AlphaAnime = new AlphaAnimation(0, 1);
        AlphaAnime.setDuration(2000);
        AlphaAnime.setFillAfter(true);
        AlphaAnime.setFillEnabled(true);
        title.startAnimation(AlphaAnime);
        AlphaAnime.setAnimationListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.load, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
//		String tmp = UtilAssist.getSpString(this, "username");
//		if (tmp.equals("")){
//            UtilAssist.goAnother(this, NewUserActivity.class);
//		}
//		else{
			UtilAssist.goAnother(this, LinkActivity.class);
		//}
	}


	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
