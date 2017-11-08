package xyz.danshin.smartnotes.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.view.View;

import com.nononsenseapps.filepicker.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import xyz.danshin.smartnotes.Enums;
import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.controller.ActivityController;
import xyz.danshin.smartnotes.controller.FileController;

/**
 * "Обрезанное" Activity просмотра заметок
 */
@EActivity
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
     * Привязка setOnClickListener'ов в элементам меню
     */
    @Override
    protected void bindDialogMenuItemClick()
    {
        super.bindDialogMenuItemClick();

        // Пункт "Экспорт"
        dialogMenu.findViewById(R.id.dialog_menu_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu.dismiss();
                ActivityController.startExportDirectoryPicker(NoteViewActivity.this, 1);
            }
        });

        // Пункт "Изменить"
        dialogMenu.findViewById(R.id.dialog_menu_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu.dismiss();
                onClickEdit();
            }
        });
    }

    /**
     * Обновление Toolbox'a, в зависимости от вида взаимодействия с заметкой
     */
    @Override
    protected void updateDialogMenu() {
        dialogMenu.findViewById(R.id.dialog_menu_save).setVisibility(View.GONE);
        dialogMenu.findViewById(R.id.dialog_menu_undo).setVisibility(View.GONE);
        dialogMenu.findViewById(R.id.dialog_menu_redo).setVisibility(View.GONE);
        dialogMenu.findViewById(R.id.dm_priority_top_menu_layout).setVisibility(View.GONE);
    }
    /**
     * Метод, вызываемый при клике на кнопку редактирования
     */
    @Click(R.id.bottom_view_note_view_edit)
    protected void onClickEdit() {
        ActivityController.startNoteActivityEdit(noteId, adapterPosition);
        setResult(RESULT_CANCELED);
        finish();
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
}
