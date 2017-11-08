package xyz.danshin.smartnotes.ui.commons;


import android.graphics.drawable.Drawable;

import xyz.danshin.smartnotes.Enums;

/**
 * Класс приоритета заметки
 */
public class NotePriority {

    /**
     * Элемент перечисления типов приоритета заметок
     */
    private Enums.NotePriority priorityType;

    /**
     * Бэкграунд элемента в общем диалоговом меню заметок
     */
    private Drawable topMenuBackground;

    /**
     * Бэкграунд элемента в диалоговом окне выбора приоритета заметок
     */
    private Drawable dialogMenuItemBackground;

    /**
     * Цвет текста при выбранном приоритете
     */
    private int textColor;

    /**
     * Бэкграунд элементов вывода заметок при выбранном приоритете
     */
    private int textColorBackground;

    /**
     * Название приоритета
     */
    private String name;

    /**
     * Конструктор
     * @param priorityType Тип приоритета
     * @param name Название
     * @param topMenuBackground Бэкграунд элемента в общем диалоговом меню заметок
     * @param dialogMenuItemBackground Бэкграунд элемента в диалоговом окне выбора приоритета заметок
     * @param textColor Цвет текста при выбранном приоритете
     * @param textColorBackground Бэкграунд элементов вывода заметок при выбранном приоритете
     */
    public NotePriority(Enums.NotePriority priorityType, String name, Drawable topMenuBackground, Drawable dialogMenuItemBackground, int textColor, int textColorBackground)
    {
        this.priorityType = priorityType;
        this.name = name;
        this.topMenuBackground = topMenuBackground;
        this.dialogMenuItemBackground = dialogMenuItemBackground;
        this.textColor = textColor;
        this.textColorBackground = textColorBackground;
    }

    /**
     * Геттер для элемента перечисления типов приоритета заметок
     * @return Элемент перечисления типов приоритета заметок
     */
    public Enums.NotePriority getPriorityType() {
        return priorityType;
    }

    /**
     * Геттер для бэкграунда элемента в общем диалоговом меню заметок
     * @return Бэкграунд для элемента в общем диалоговом меню заметок
     */
    public Drawable getTopMenuBackground() {
        return topMenuBackground;
    }

    /**
     * Геттер для бэкграунда элемента в диалоговом окне выбора приоритета заметок
     * @return Бэкграунд элемента в диалоговом окне выбора приоритета заметок
     */
    public Drawable getDialogMenuItemBackground() {
        return dialogMenuItemBackground;
    }

    /**
     * Геттер для названия приоритета
     * @return Название приоритета
     */
    public String getName() {
        return name;
    }

    /**
     * Геттер для цвета текста при выбранном приоритете
     * @return Цвет текста при выбранном приоритете
     */
    public int getTextColor() {
        return textColor;
    }

    /**
     * Геттер для бэкграунда элементов вывода заметок при выбранном приоритете
     * @return Бэкграунд элементов вывода заметок при выбранном приоритете
     */
    public int getTextColorBackground() {
        return textColorBackground;
    }
}
