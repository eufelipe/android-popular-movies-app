package com.eufelipe.popularmovies.bases;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


/**
 * @description : class abstract com métodos globais a todas as activities
 * @author: Felipe Rosas <contato@eufelipe.com>
 */
public abstract class BaseActivity extends AppCompatActivity {

    Toast mToast;

    protected void showToast(int resourceId) {
        showToast(getString(resourceId));
    }

    protected void showToast(String message) {
        if (message == null) {
            return;
        }

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();

    }

}