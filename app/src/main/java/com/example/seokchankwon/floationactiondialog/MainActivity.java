package com.example.seokchankwon.floationactiondialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.seokchankwon.floationactiondialog.dialog.FloatingActionDialog;

public class MainActivity extends AppCompatActivity {

    public static String DIALOG_TAG_FLOATING_ACTION_DIALOG = "dialog_tag.floating_action_menu";

    private Context mContext;

    private Toolbar mToolbar;

    private FloatingActionButton fabShowMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        initView();
        setSupportActionBar(mToolbar);

        fabShowMenu.setOnClickListener(v -> showFloatingActionDialog());
    }

    private void initView() {
        mToolbar = findViewById(R.id.tb_activity_main);
        fabShowMenu = findViewById(R.id.fab_activity_main_show_menu);
    }

    private void showFloatingActionDialog() {
        Fragment dialog = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG_FLOATING_ACTION_DIALOG);

        if (dialog != null && dialog.isAdded()) {
            return;
        }

        final FloatingActionDialog floatingActionDialog = new FloatingActionDialog.Builder(fabShowMenu)
                .setMenu(R.menu.menu_floating_dialog)
                .setItemBackgroundColor(R.color.colorPrimary)
                .setCloserBackgroundColor(R.color.colorAccent)
                .setOnItemClickListener(menuItem -> Toast.makeText(mContext, menuItem.getTitle(), Toast.LENGTH_SHORT).show())
                .build();

        floatingActionDialog.show(getSupportFragmentManager(), DIALOG_TAG_FLOATING_ACTION_DIALOG);
    }

}
