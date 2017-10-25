package xyz.danshin.smartnotes.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nononsenseapps.filepicker.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.controller.ActivityController;
import xyz.danshin.smartnotes.controller.FileController;
import xyz.danshin.smartnotes.entity.Note;
import xyz.danshin.smartnotes.repository.NoteRepository;
import xyz.danshin.smartnotes.interfaces.IRvSelectedListener;
import xyz.danshin.smartnotes.view.RvAnimator;
import xyz.danshin.smartnotes.view.RvNoteAdapter;

/**
 * Главное activity
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity  implements IRvSelectedListener {

    /**
     * RecyclerView с заметками
     */
    @ViewById(R.id.notes_recyclerview)
    protected RecyclerView recyclerview;

    /**
     * Тулбар
     */
    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    /**
     * Кнопа добавления заметки
     */
    @ViewById(R.id.fab)
    protected FloatingActionButton fab;

    protected RecyclerView.LayoutManager RecyclerViewLayoutManager;

    /**
     * Адаптер для recyclerView с заметками
     */
    protected RvNoteAdapter adapter;
    /**
     * ActionMode для динамического тулбара
     */
    protected ActionMode actionMode;


    /**
     * Метод, вызываемый после загрузки
     */
    @AfterViews
    public void afterView() {
        ActivityController.setBaseActivity(this);
        setSupportActionBar(toolbar);
        initRecyclerView();
    }

    /**
     * Инициализация recyclerView
     */
    protected void initRecyclerView() {
        NoteRepository.addTestNotes();
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(RecyclerViewLayoutManager);
        adapter = new RvNoteAdapter(this);
        recyclerview.setItemAnimator(new RvAnimator());
        recyclerview.setAdapter(adapter);
    }

    /**
     * Метод, вызываемый при клине на кнопку добавления заметки
     */
    @Click(R.id.fab)
    protected void clickFabAdd() {
        adapter.notifyDataSetChanged();
        ActivityController.startNoteActivityAdd(this);
    }

    /**
     * Метод, вызываемый при принятии данных из NoteActivity при создании заметки
     */
    @OnActivityResult(1)
    void onCreateNote(int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) return;
        Bundle bundle = data.getExtras();
        Note newNote = new Note(bundle.getString("title"), bundle.getString("description"), null);
        adapter.AddItem(newNote);
        ((LinearLayoutManager) recyclerview.getLayoutManager()).scrollToPositionWithOffset(0, 0);
        adapter.notifyItemInserted(0);
    }

    /**
     * Метод, вызываемый при принятии данных из NoteActivity при изменении/удалении заметки
     */
    @OnActivityResult(2)
    void onEditRemoveNote(int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) return;
        Bundle bundle = data.getExtras();

        int id = bundle.getInt("id");
        int adapterPosition = bundle.getInt("adapterPosition");

        if (bundle.getBoolean("remove")) {
            adapter.removeNoteById(id, adapterPosition);
            return;
        }

        Note note = NoteRepository.getNoteById(id);
        if (note == null)
            throw new NullPointerException("note is null");
        note.setTitle(bundle.getString("title"));
        note.setDescription(bundle.getString("description"));
        note.setLastModifiedDate(null);

        try {
            NoteRepository.relocate(note);
        } catch (Exception e) {
            // empty
        }
        ((LinearLayoutManager) recyclerview.getLayoutManager()).scrollToPositionWithOffset(0, 0);
        adapter.notifyItemChanged(adapterPosition);
        adapter.notifyItemMoved(adapterPosition, 0);
    }

    /**
     * Метод, вызываемый при экспорте заметок
     */
    @OnActivityResult(3)
    void onExportNotes(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            FileController.exportNotes(NoteRepository.getSelectedNotes(), Utils.getSelectedFilesFromResult(data).get(0), this);
        }
        actionMode.finish();
    }
    /**
     * Метод, вызываемый при изменении состояния выделения элемента в recyclerView
     *
     * @param selectedItems Список выделенных элементов в recycleView с
     * @param selectedItem  Последний элемент на котором сработало событие
     */
    @Override
    public void onItemSelected(ArrayList<Note> selectedItems, Note selectedItem) {

        if (actionMode == null) return;
        actionMode.setTitle(String.valueOf(selectedItems.size()));
    }

    /**
     * Метод, вызываемый при клике на элементе в recyclerView
     *
     * @param selectedItem    Экземпляр заметки
     * @param adapterPosition Индекс элемена в адаптере recycleView
     */
    @Override
    public void onItemClick(Note selectedItem, int adapterPosition) {
        ActivityController.startNoteActivityView(this, selectedItem.getId(), adapterPosition);
    }

    /**
     * Метод, вызываемый при изменении режима выделения
     *
     * @param status Статус режима выделения
     */
    @Override
    public void onChangeSelectionMode(boolean status) {

        if (status) {
            actionMode = startSupportActionMode(callback);
            fab.setVisibility(View.INVISIBLE);
        } else {
            if (actionMode != null)
                actionMode.finish();
            fab.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Функционал actionBar'а при выделении заметок
     */
    private ActionMode.Callback callback = new ActionMode.Callback() {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_main_notes_action_bar, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            switch (id)
            {
                case R.id.action_delete:
                    deleteActionAlert();
                    return true;
                case R.id.action_export:
                    ActivityController.startExportDirectoryPicker(ActivityController.getBaseActivity(), 3);
                    return true;
            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
            if (adapter.getSelectableStatus())
                adapter.clearSelection();
            actionMode = null;
        }
    };

    /**
     * Сообщение при удалении выделенных заметок
     */
    private void deleteActionAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.deleting);
        alert.setMessage(R.string.delete_selected_notes);
        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.removeSelectedNotes();
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
}
