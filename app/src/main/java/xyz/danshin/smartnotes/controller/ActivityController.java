package xyz.danshin.smartnotes.controller;


import android.support.v7.app.AppCompatActivity;

import xyz.danshin.smartnotes.activity.NoteViewActivity_;
import xyz.danshin.smartnotes.activity.NoteEditActivity_;
import xyz.danshin.smartnotes.Enums;

/**
 * Класс контроллер для activity
 */
public final class ActivityController {

    /**
     * Главное Activity приложения
     */
    private static AppCompatActivity baseActivity;

    /**
     * Геттер главного Activity приложения
     */
    public static AppCompatActivity getBaseActivity() {
        return baseActivity;
    }

    /**
     * Сеттер главного Activity приложения
     */
    public static void setBaseActivity(AppCompatActivity baseActivity) {
        ActivityController.baseActivity = baseActivity;
    }

    /**
     * Запуск Activity создания заметки
     * @param activity Activity, куда отправить результат
     */
    public static void startNoteActivityAdd(AppCompatActivity activity) {
        NoteEditActivity_
                .intent(activity)
                .activityType(Enums.NoteActivityType.CREATE)
                .startForResult(1);
    }

    /**
     * Запуск Activity изменения заметки
     * @param id Id Заметки
     * @param adapterPosition Позиция адаптера
     */
    public static void startNoteActivityEdit(int id, int adapterPosition) {
        NoteEditActivity_
                .intent(getBaseActivity())
                .activityType(Enums.NoteActivityType.EDIT)
                .noteId(id)
                .adapterPosition(adapterPosition)
                .startForResult(2);
    }

    /**
     * Запуск Activity просмотра заметки
     * @param activity Activity, куда отправить результат
     * @param id Id Заметки
     * @param adapterPosition Позиция адаптера
     */
    public static void startNoteActivityView(AppCompatActivity activity, int id, int adapterPosition) {
        NoteViewActivity_
                .intent(activity)
                .activityType(Enums.NoteActivityType.VIEW)
                .noteId(id)
                .adapterPosition(adapterPosition)
                .startForResult(2);
    }
}
