package xyz.danshin.smartnotes.ui.commons;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import xyz.danshin.smartnotes.R;

/**
 * Класс-хелпер для ActionBar'а при открытии меню заметок
 */
public class ActionBarHelper {
    /**
     * ActionBar
     */
    private ActionBar actionBar;

    /**
     * Activity
     */
    private AppCompatActivity activity;

    /**
     * Высота ActionBar'а
     */
    private float elevation;

    /**
     * Элемент меню, который нужно перобразовать
     */
    private ActionMenuItemView menuItem;

    /**
     * Анимация элемента меню
     */
    private Animation rotation;

    /**
     * Содержит состояние открытия меню
     */
    private boolean isOpen;
    /**
     * Геттер для элемента меню
     * @return Элемент меню
     */
    public ActionMenuItemView getMenuItem() {
        return menuItem;
    }

    /**
     * Сеттер для элемента меню
     * @param menuItem Элемент меню
     */
    public void setMenuItem(ActionMenuItemView menuItem) {
        this.menuItem = menuItem;
    }

    /**
     * Геттер для состояния открытия меню
     * @return Состояние меню
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Конструктор
     * @param actionBar ActionBar
     * @param activity Activity
     */
    public ActionBarHelper(ActionBar actionBar, AppCompatActivity activity)
    {
        this.actionBar = actionBar;
        this.activity = activity;
        this.elevation = actionBar.getElevation();
        this.rotation = AnimationUtils.loadAnimation(activity, R.anim.rotate);
    }

    /**
     * Менет стиль ActionBar'а, который необходим при открытом меню
     */
    public void showMenu()
    {
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setElevation(0);
        if (menuItem != null)
            changeMenuItem(true);
        isOpen = true;
    }

    /**
     * Менет стиль ActionBar'а на стандартный
     */
    public void hideMenu()
    {
        actionBar.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.colorPrimary)));
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(elevation);
        if (menuItem != null)
            changeMenuItem(false);
        isOpen = false;
    }

    /**
     * Смена иконки элемента меню при открытом/закрытом меню
     * @param isOpen Статус меню
     */
    @SuppressLint("RestrictedApi")
    private void changeMenuItem(boolean isOpen)
    {
        if (isOpen) {
            menuItem.startAnimation(rotation);
            menuItem.setIcon(activity.getResources().getDrawable(R.drawable.ic_close_black_24dp));
        } else {
            menuItem.setIcon(activity.getResources().getDrawable( R.drawable.ic_menu_white_24dp ));
        }
    }


}
