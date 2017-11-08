package xyz.danshin.smartnotes.ui.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.repository.NotePriorityRepository;
import xyz.danshin.smartnotes.ui.adapters.RvPriorityTopDMAdapter;
import xyz.danshin.smartnotes.ui.commons.ActionBarHelper;
import xyz.danshin.smartnotes.interfaces.IRvPriorityClickListener;
import xyz.danshin.smartnotes.repository.NoteRepository;
import xyz.danshin.smartnotes.entity.Note;
import xyz.danshin.smartnotes.Enums;
import xyz.danshin.smartnotes.ui.adapters.RvPriorityDMAdapter;
import xyz.danshin.smartnotes.ui.decorators.RvPriorityTopDMDecorator;

/**
 * Activity создания/редактирования заметок
 */
@EActivity
@OptionsMenu(R.menu.menu_note)
public class NoteEditActivity extends AppCompatActivity implements IRvPriorityClickListener {
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
     * Переменная, содержащая типр приоритета заметки
     */
    protected Enums.NotePriority notePriority;

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
    protected View viewUndo;

    /**
     * Кнопка повторения изменений
     */
    protected View viewRedo;

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
     * Экзмепляр класса-хелпера для ActionBar'а
     */
    protected ActionBarHelper actionHelper;

    /**
     * Экзмепляр диалогового меню
     */
    protected DialogPlus dialogMenu;

    /**
     * Экзмепляр диалогового меню выбора приоритета
     */
    protected DialogPlus dialogPriorityMenu;

    /**
     * Переменая, значение которой содержит состояние открытия диалога выбора приоритета
     */
    private boolean dialogPriorityMenuIsOpen;

    /**
     * Адаптер recyclerView с приоритетами для диалогового меню, которе содерит только приоритеты
     */
    private RvPriorityDMAdapter rvPriorityDMAdapter;

    /**
     * Адаптер recyclerView с приоритетами для главного диалогового меню
     */
    private RvPriorityTopDMAdapter rvPriorityTopDMAdapter;

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
    private String[] noteTempData = new String[3];

    /**
     * TextWatcher, следящий за именением текста в полях заметки
     */
    private final TextWatcher tw = new TextWatcher() {
        @SuppressLint("RestrictedApi")
        public void afterTextChanged(Editable s) {
            setEditStatus();
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
        actionHelper = new ActionBarHelper(getSupportActionBar(), this);

        if (activityType == Enums.NoteActivityType.EDIT || activityType == Enums.NoteActivityType.VIEW) {
            note = NoteRepository.getNoteById(noteId);
        } else if (activityType == Enums.NoteActivityType.CREATE)
        {
            note = new Note("", "", null);
        }

        notePriority = note.getPriority();
        noteTitle.setText(note.getTitle());
        noteDescription.setText(note.getDescription());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createDialogMenu();
        createNotePriorityDialogMenu();
        updateDialogMenu();
        bindDialogMenuItemClick();
        updateLayout();
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
     * Метод, вызывемый при клике на кнопке меню "Развернуть"
     */
    @OptionsItem(R.id.menu_item_more)
    void onClickMenuItemMore() {
        if (dialogPriorityMenuIsOpen) {
            dialogPriorityMenu.dismiss();
            return;
        }

        if (actionHelper.isOpen()) {
            dialogMenu.dismiss();
            return;
        }

        if (actionHelper.getMenuItem() == null)
            actionHelper.setMenuItem((ActionMenuItemView)findViewById(R.id.menu_item_more));
        actionHelper.showMenu();
        dialogMenu.show();
    }

    /**
     * Привязка setOnClickListener'ов в элементам меню
     */
    protected void bindDialogMenuItemClick()
    {
        // Пункт "Удалить"
        dialogMenu.findViewById(R.id.dialog_menu_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu.dismiss();
                onClickRemove();
            }
        });

        // Пункт "Сохранить"
        dialogMenu.findViewById(R.id.dialog_menu_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu.dismiss();
                onClickSave();
            }
        });

        // Пункт "Отменить"
        dialogMenu.findViewById(R.id.dialog_menu_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu.dismiss();
                onClickUndo();
            }
        });

        // Пункт "Повторить"
        dialogMenu.findViewById(R.id.dialog_menu_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu.dismiss();
                onClickRedo();
            }
        });
    }

    /**
     * Метод, вызываемый при клике на кнопку с выбором приоритета в BottomNavigationBar
     */
    @Click(R.id.bottom_view_note_view_select_priority)
    protected void onClickPriority() {
        dialogPriorityMenuIsOpen = true;
        dialogPriorityMenu.show();

    }

    /**
     * Метод, вызываемый при клике на элемент в recyclerView с приоритетами
     * @param v Экземпляр View
     * @param index Индекс приоритета
     */
    public void onPriorityItemClick(View v, int index) {
        notePriority = NotePriorityRepository.getInstance().getPrioritiesList().get(index).getPriorityType();

        if (((View)v.getParent()).getId() == R.id.dm_priority_top_menu_recycler_view)
            dialogMenu.dismiss();

        updateLayout();
        setEditStatus();
    }

    /**
     * Дополнительный метод, выполняемый в конце загрузки Activity
     */
    protected void afterLoad() {
        setTextWatcherForEditText();
    }

    /**
     * Обновление Layout в зависимости от приоритета заметки
     */
    protected void updateLayout() {
        noteDescription.setTextColor(NotePriorityRepository.getInstance().getPrioritiesMap().get(notePriority).getTextColor());
        noteTitle.setTextColor(NotePriorityRepository.getInstance().getPrioritiesMap().get(notePriority).getTextColor());
        findViewById(R.id.note_view_content).setBackground(new ColorDrawable(NotePriorityRepository.getInstance().getPrioritiesMap().get(notePriority).getTextColorBackground()));
        navigation.setBackground(new ColorDrawable(NotePriorityRepository.getInstance().getPrioritiesMap().get(notePriority).getTextColorBackground()));

        rvPriorityDMAdapter.setActivePriority(notePriority);
        rvPriorityTopDMAdapter.setActivePriority(notePriority);
        rvPriorityDMAdapter.notifyDataSetChanged();
        rvPriorityTopDMAdapter.notifyDataSetChanged();
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
     * Обновление диалогового меню, в зависимости от вида взаимодействия с заметкой
     */
    protected void updateDialogMenu() {
        if (!(activityType == Enums.NoteActivityType.CREATE || activityType == Enums.NoteActivityType.EDIT))
            return;
        dialogMenu.findViewById(R.id.dialog_menu_export).setVisibility(View.GONE);
        dialogMenu.findViewById(R.id.dialog_menu_edit).setVisibility(View.GONE);
        dialogMenu.findViewById(R.id.dialog_menu_undo).setVisibility(View.GONE);
        dialogMenu.findViewById(R.id.dialog_menu_redo).setVisibility(View.GONE);

         if (activityType == Enums.NoteActivityType.CREATE) {
            dialogMenu.findViewById(R.id.dialog_menu_delete).setVisibility(View.GONE);
        }
    }

    /**
     * Обновление нижнего Navigation Bar'a, в зависимости от вида взаимодействия с заметкой
     */
    @SuppressLint("RestrictedApi")
    protected void updateBottomNavBar() {
        if (activityType == Enums.NoteActivityType.CREATE) {
            navigationRemove.setVisibility(View.GONE);
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
        if (!noteHasEdited) {
            noteHasEdited = true;
            viewRedo.setVisibility(View.VISIBLE);
            viewUndo.setVisibility(View.VISIBLE);
            updateBottomNavBar();
        }
        changeEnableUndoRedo(false);
    }

    /**
     * Установка стилей кнопок отмены/повторения изменений заметки в нижнем Navigation Bar'е
     * @param isUndo Действие принадлежит отмене изменений?
     */
    final void changeEnableUndoRedo(boolean isUndo) {
        if (isUndo) {
            viewUndo.setEnabled(false);
            viewRedo.setEnabled(true);
        } else {
            viewUndo.setEnabled(true);
            viewRedo.setEnabled(false);
        }
        updateLayout();
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
        int index = -1;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        note.setPriority(notePriority);
        note.setTitle(noteTitle.getText().toString());
        note.setDescription(noteDescription.getText().toString());
        note.setLastModifiedDate(null);

        if (activityType == Enums.NoteActivityType.CREATE)
            NoteRepository.add(note);

        try {
            index = NoteRepository.relocateToUp(note);
        } catch (Exception e) {
            Log.e("EditNoteActivity", "onClickSave() > " + activityType + " > note is null");
        }

        if (activityType == Enums.NoteActivityType.CREATE) {
            bundle.putInt("adapterPosition", index);
        } else if (activityType == Enums.NoteActivityType.EDIT) {
            bundle.putInt("adapterPosition", adapterPosition);
            bundle.putInt("newAdapterPosition", index);
        }

        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Метод, вызываемый при клике на кнопку удаления
     */
    @Click(R.id.bottom_view_note_edit_remove)
    public void onClickRemove() {
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
        NoteRepository.removeById(note.getId());
        if (activityType == Enums.NoteActivityType.EDIT || activityType == Enums.NoteActivityType.VIEW) {
            bundle.putBoolean("remove", true);
            bundle.putInt("adapterPosition", adapterPosition);
        }
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Метод, вызываемый при клике на кнопку отмены изменений
     */
    protected void onClickUndo() {
        noteTempData[Enums.NoteTempData.TITLE.ordinal()] = noteTitle.getText().toString();
        noteTempData[Enums.NoteTempData.DESCRIPTION.ordinal()] = noteDescription.getText().toString();
        noteTempData[Enums.NoteTempData.PRIORITY.ordinal()] = notePriority.toString();
        noteTitle.setText(note.getTitle());
        noteDescription.setText(note.getDescription());
        notePriority = note.getPriority();
        changeEnableUndoRedo(true);
    }

    /**
     * Метод, вызываемый при клике на кнопку повтора измеенений
     */
    protected void onClickRedo() {
        noteTitle.setText(noteTempData[Enums.NoteTempData.TITLE.ordinal()]);
        noteDescription.setText(noteTempData[Enums.NoteTempData.DESCRIPTION.ordinal()]);
        notePriority = Enums.NotePriority.valueOf(noteTempData[Enums.NoteTempData.PRIORITY.ordinal()]);
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

    /**
     * Инициализируем экземпляр диалогового меню
     */
    protected void createDialogMenu()
    {
        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
                actionHelper.hideMenu();
            }
        };

        dialogMenu  = DialogPlus.newDialog(this)
                .setOutAnimation(R.anim.no_animation)
                .setContentHolder( new ViewHolder(R.layout.dm_notes_menu))
                .setGravity(Gravity.TOP)
                .setOnDismissListener(dismissListener)
                .setExpanded(false)
                .create();

        viewUndo = dialogMenu.findViewById(R.id.dialog_menu_undo);
        viewRedo = dialogMenu.findViewById(R.id.dialog_menu_redo);

        RecyclerView rv = (RecyclerView) dialogMenu.findViewById(R.id.dm_priority_top_menu_recycler_view);
        rv.addItemDecoration(new RvPriorityTopDMDecorator(this));
        LinearLayoutManager RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(RecyclerViewLayoutManager);
        rvPriorityTopDMAdapter = new RvPriorityTopDMAdapter(this);
        rvPriorityTopDMAdapter.setActivePriority(notePriority);
        rv.setAdapter(rvPriorityTopDMAdapter);
    }
    /**
     * Инициализируем экземпляр диалогового меню приоритетов заметки
     */
    protected void createNotePriorityDialogMenu()
    {

        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
                dialogPriorityMenuIsOpen = false;
            }
        };

        dialogPriorityMenu = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.dm_priority_menu))
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(dismissListener)
                .setExpanded(false)
                .setOverlayBackgroundResource(android.R.color.transparent)
                .create();

        RecyclerView rv = (RecyclerView) dialogPriorityMenu.findViewById(R.id.dm_priority_recycler_view);
        LinearLayoutManager RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(RecyclerViewLayoutManager);
        rvPriorityDMAdapter = new RvPriorityDMAdapter(this);
        rvPriorityDMAdapter.setActivePriority(notePriority);
        rv.setAdapter(rvPriorityDMAdapter);
    }
}