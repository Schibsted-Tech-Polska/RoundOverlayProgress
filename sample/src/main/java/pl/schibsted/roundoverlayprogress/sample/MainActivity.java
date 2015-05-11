package pl.schibsted.roundoverlayprogress.sample;

import android.app.Activity;
import android.os.Bundle;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.schibsted.roundoverlayprogress.RoundOverlayProgressView;


public class MainActivity extends Activity {

    @InjectView(R.id.progress_view)
    protected RoundOverlayProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.random_button)
    protected void onRandomProgressClicked() {
        Random rd = new Random();
        progressView.setProgress(rd.nextInt(progressView.getMaxProgress()));
    }
}
