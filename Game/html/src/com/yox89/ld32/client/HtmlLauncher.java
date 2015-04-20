package com.yox89.ld32.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.yox89.ld32.Gajm;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1000, 700);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new Gajm();
        }
}