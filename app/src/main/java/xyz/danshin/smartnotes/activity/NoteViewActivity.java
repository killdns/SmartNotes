package xyz.danshin.smartnotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.view.View;

import com.nononsenseapps.filepicker.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import xyz.danshin.smartnotes.Enums;
import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.controller.ActivityController;
import xyz.danshin.smartnotes.controller.FileController;

/**
 * "Обрезанное" Activity просмотра заметок заметок
 */
@EActivity
@OptionsMenu(R.menu.menu_note_view)
public class NoteViewActivity extends NoteEditActivity {
    /**
     * Кнопка редактирования заметки
     */
    @ViewById(R.id.bottom_view_note_view_edit)
    protected BottomNavigationItemView navigationEdit;

    /**
     * Кнопка удаления заметки
     */
    @ViewById(R.id.bottom_view_note_view_remove)
    protected BottomNavigationItemView navigationRemove;

    /**
     * Метод, вызываемый при создании экземпляра Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
    }

    /**
     * Метод, вызываемый после onCreate
     */
    @AfterViews
    public void afterView() {
        super.afterView();
        noteDescription.setEnabled(false);
        noteTitle.setEnabled(false);
        noteTitle.setVisibility(View.GONE);
    }

    /**
     * Обновление текста в TitleBar'e
     */
    @Override
    protected void updateTitleBar() {
        if (activityType == Enums.NoteActivityType.VIEW)
            getSupportActionBar().setTitle(noteTitle.getText());
    }

    /**
     * Дополнительный метод, выполняемый в конце загрузки Activity.
     * Пустой, т.к. пропускаем настройку TextWatcher'a
     */
    @Override
    protected void afterLoad() {
    }

    /**
     * Метод, вызываемый при клике на кнопку редактирования
     */
    @Click(R.id.bottom_view_note_view_edit)
    protected void OnClickEdit() {
        ActivityController.startNoteActivityEdit(noteId, adapterPosition);
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Метод, обеспечивающий работу кнопки "Экспорт" в меню
     */
    @OptionsItem(R.id.menu_item_note_export)
    void exportNote() {
        ActivityController.startExportDirectoryPicker(this, 1);
    }
    /**
     * Метод, вызываемый при экспорте заметок
     */
    @OnActivityResult(1)
    void onExportNotes(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            FileController.exportNote(note, Utils.getSelectedFilesFromResult(data).get(0), this);
        }
    }
    /**
     * Метод, вызываемый при клике на кнопку удаления
     */
    @Click(R.id.bottom_view_note_view_remove)
    protected void OnClickRemove() {
        super.OnClickRemove();
    }
}
