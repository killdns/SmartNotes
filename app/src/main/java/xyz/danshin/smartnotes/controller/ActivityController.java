package xyz.danshin.smartnotes.controller;


import android.support.v7.app.AppCompatActivity;

import xyz.danshin.smartnotes.activity.NoteViewActivity_;
import xyz.danshin.smartnotes.activity.NoteEditActivity_;
import xyz.danshin.smartnotes.Enums;

/**
 * Класс контроллер для activity
 */
public class ActivityController {

    private static AppCompatActivity baseActivity;

    public static AppCompatActivity getBaseActivity() {
        return baseActivity;
    }

    public static void setBaseActivity(AppCompatActivity baseActivity) {
        ActivityController.baseActivity = baseActivity;
    }

    public static void startNoteActivityAdd(AppCompatActivity activity) {
        NoteEditActivity_
                .intent(activity)
                .activityType(Enums.NoteActivityType.CREATE)
                .startForResult(1);
    }

    public static void startNoteActivityEdit(int id, int adapterPosition) {
        NoteEditActivity_
                .intent(getBaseActivity())
                .activityType(Enums.NoteActivityType.EDIT)
                .noteId(id)
                .adapterPosition(adapterPosition)
                .startForResult(2);
    }
    public static void startNoteActivityView(AppCompatActivity activity, int id, int adapterPosition) {
        NoteViewActivity_
                .intent(activity)
                .activityType(Enums.NoteActivityType.VIEW)
                .noteId(id)
                .adapterPosition(adapterPosition)
                .startForResult(2);
    }
}
