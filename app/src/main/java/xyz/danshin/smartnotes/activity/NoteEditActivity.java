package xyz.danshin.smartnotes.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.repository.NoteRepository;
import xyz.danshin.smartnotes.entity.Note;
import xyz.danshin.smartnotes.Enums;

/**
 * Activity создания/редактирования заметок
 */
@EActivity
public class NoteEditActivity extends AppCompatActivity {
    /**
     * Нижний Navigation Bar
     */
    @ViewById(R.id.navigation)
    protected BottomNavigationViewEx navigation;

    /**
     * Поле ввода заголовка заметки
     */
    @ViewById(R.id.noteActivity_title)
    protected EditText noteTitle;

    /**
     * Поле ввода описания заметки
     */
    @ViewById(R.id.noteActivity_description)
    protected EditText noteDescription;

    /**
     * Кнопка сохранения заметки
     */
    @ViewById(R.id.bottom_view_note_edit_save)
    protected BottomNavigationItemView navigationSave;

    /**
     * Кнопка удаления заметки
     */
    @ViewById(R.id.bottom_view_note_edit_remove)
    protected BottomNavigationItemView navigationRemove;

    /**
     * Кнопка отмены изменений
     */
    @ViewById(R.id.bottom_view_note_edit_undo)
    protected BottomNavigationItemView navigationUndo;

    /**
     * Кнопка повторения изменений
     */
    @ViewById(R.id.bottom_view_note_edit_redo)
    protected BottomNavigationItemView navigationRedo;

    /**
     * Тип операции с заметкой
     */
    @Extra
    protected Enums.NoteActivityType activityType = Enums.NoteActivityType.CREATE;

    /**
     * Id заметки
     */
    @Extra
    protected int noteId = -1;

    /**
     * Позиция адаптера
     */
    @Extra
    protected int adapterPosition = -1;

    /**
     * Экзмепляр заметки
     */
    protected Note note;

    /**
     * Переменая, значение которой определяет была ли изменена заметка
     */
    private boolean noteHasEdited;

    /**
     * Массив, храняжий временные значения полей заметки. Необходим для отмены/повтора изменений
     */
    private String[] noteTempData = new String[2];

    /**
     * TextWatcher, следящий за именением текста в полях заметки
     */
    private final TextWatcher tw = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            setEditStatus();
            navigationRedo.setEnabled(false);
            navigationUndo.setEnabled(true);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    /**
     * Метод, вызываемый при создании экземпляра Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_note_edit);
        super.onCreate(savedInstanceState);
    }

    /**
     * Метод, вызываемый после onCreate
     */
    @AfterViews
    protected void afterView() {
        if (activityType == Enums.NoteActivityType.EDIT || activityType == Enums.NoteActivityType.VIEW) {
            note = NoteRepository.getNoteById(noteId);
            noteTitle.setText(note.getTitle());
            noteDescription.setText(note.getDescription());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        updateBottomMenuStyle();
        updateTitleBar();
        updateBottomNavBar();
        afterLoad();

    }

    /**
     * Возврат к предыдущей Activity
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Метод, обеспечивающий работу кнопки "Назад" в Toolbox
     */
    @OptionsItem(android.R.id.home)
    protected void closeActivity() {
        NoteEditActivity.this.finish();
    }

    /**
     * Дополнительный метод, выполняемый в конце загрузки Activity
     */
    protected void afterLoad() {
        setTextWatcherForEditText();
    }

    /**
     * Обновление Toolbox'a, в зависимости от вида взаимодействия с заметкой
     */
    protected void updateTitleBar() {
        if (activityType == Enums.NoteActivityType.EDIT)
            getSupportActionBar().setTitle(R.string.title_activity_note_edit);
        else if (activityType == Enums.NoteActivityType.CREATE)
            getSupportActionBar().setTitle(R.string.title_activity_note_view);
    }

    /**
     * Обновление нижнего Navigation Bar'a, в зависимости от вида взаимодействия с заметкой
     */
    protected void updateBottomNavBar() {
        if (activityType == Enums.NoteActivityType.CREATE) {
            navigationUndo.setVisibility(View.GONE);
            navigationRedo.setVisibility(View.GONE);
            navigationRemove.setVisibility(View.GONE);
        }
        if (activityType == Enums.NoteActivityType.EDIT) {
            if (noteHasEdited) {
                navigationUndo.setVisibility(View.VISIBLE);
                navigationRedo.setVisibility(View.VISIBLE);
                navigationRedo.setEnabled(false);
                navigationRemove.setVisibility(View.INVISIBLE);
            } else {
                navigationUndo.setVisibility(View.INVISIBLE);
                navigationRedo.setVisibility(View.INVISIBLE);
                navigationRemove.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Установка стиля для нижнего Navigation Bar'a
     */
    protected void updateBottomMenuStyle() {
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);
    }

    /**
     * Установка TextWatcher'a, остсежтвающего редактирование полей заметки
     */
    final void setTextWatcherForEditText() {
        noteTitle.addTextChangedListener(tw);
        noteDescription.addTextChangedListener(tw);
    }

    /**
     * Установка статуса редактирования заметки
     */
    final void setEditStatus() {
        noteHasEdited = true;
        updateBottomNavBar();
    }

    /**
     * Установка стилей кнопок отмены/повторения изменений заметки в нижнем Navigation Bar'е
     * @param isUndo Действие принадлежит отмене изменений?
     */
    final void changeEnableUndoRedo(boolean isUndo) {
        if (isUndo) {
            navigationUndo.setEnabled(false);
            navigationRedo.setEnabled(true);
        } else {
            navigationUndo.setEnabled(true);
            navigationRedo.setEnabled(false);
        }
    }

    /**
     * Метод, вызываемый при клике на кнопку сохранения
     */
    @Click(R.id.bottom_view_note_edit_save)
    protected void onClickSave() {
        if (!checkTextEditFields()) {
            showSaveErrorAlert();
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (activityType == Enums.NoteActivityType.CREATE) {
            bundle.putString("title", noteTitle.getText().toString());
            bundle.putString("description", noteDescription.getText().toString());
        } else if (activityType == Enums.NoteActivityType.EDIT) {
            bundle.putInt("id", note.getId());
            bundle.putInt("adapterPosition", adapterPosition);
            bundle.putString("title", noteTitle.getText().toString());
            bundle.putString("description", noteDescription.getText().toString());
        }
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Метод, вызываемый при клике на кнопку удаления
     */
    @Click(R.id.bottom_view_note_edit_remove)
    protected void onClickRemove() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.deleting);
        alert.setMessage(R.string.delete_current_note);
        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                onRemoveAccept();
                dialog.dismiss();
            }
        });

        alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    /**
     * Метод, вызываемый при подтверждении удаления
     */
    protected void onRemoveAccept() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (activityType == Enums.NoteActivityType.EDIT || activityType == Enums.NoteActivityType.VIEW) {
            bundle.putBoolean("remove", true);
            bundle.putInt("id", note.getId());
            bundle.putInt("adapterPosition", adapterPosition);
        }
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Метод, вызываемый при клике на кнопку отмены изменений
     */
    @Click(R.id.bottom_view_note_edit_undo)
    protected void onClickUndo() {
        noteTempData[Enums.NoteTempData.TITLE.ordinal()] = noteTitle.getText().toString();
        noteTempData[Enums.NoteTempData.DESCRIPTION.ordinal()] = noteDescription.getText().toString();
        noteTitle.setText(note.getTitle());
        noteDescription.setText(note.getDescription());
        changeEnableUndoRedo(true);
    }

    /**
     * Метод, вызываемый при клике на кнопку повтора измеенений
     */
    @Click(R.id.bottom_view_note_edit_redo)
    protected void onClickRedo() {
        noteTitle.setText(noteTempData[Enums.NoteTempData.TITLE.ordinal()]);
        noteDescription.setText(noteTempData[Enums.NoteTempData.DESCRIPTION.ordinal()]);
        changeEnableUndoRedo(false);
    }

    /**
     * Проверка на заполненность полей заметки
     */
    protected boolean checkTextEditFields()
    {
        return !(noteTitle.getText().length() == 0 || noteDescription.getText().length() == 0);
    }

    /**
     * Сообщение об ошибке сохранения
     */
    private void showSaveErrorAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.error);
        alert.setMessage(R.string.note_save_error);
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}