package com.eufelipe.popularmoviesapp.activities;

import android.os.Bundle;

import com.eufelipe.popularmoviesapp.R;
import com.eufelipe.popularmoviesapp.bases.BaseActivity;

/**
 * @description : Activity principal com o requesito de exibir uma lista de filmes populares
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}