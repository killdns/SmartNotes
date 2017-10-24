package xyz.danshin.smartnotes.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.listener.SwipeSwitchListener;

import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.entity.Note;
import xyz.danshin.smartnotes.repository.NoteRepository;


/**
 * ViewHolder для recyclerView с заметками
 */
public class RvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {

    /**
     * Поле заголовка заметки
     */
    protected TextView title;

    /**
     * Поле описания заметки
     */
    protected TextView description;

    /**
     * Поле даты последнего изменения заметки
     */
    protected TextView date;

    /**
     * Чекбокс выделения заметки
     */
    protected CheckBox checkbox;

    /**
     * Layout swipe-меню
     */
    protected SwipeHorizontalMenuLayout sml;

    /**
     * Layout swipe-меню. Родительский View
     */
    protected View smCv;

    /**
     * View элемента "Edit"
     */
    protected View btEdit;
    /**
     * View элемента "Delete"
     */
    protected View btDelete;

    /**
     * Адаптер
     */
    private RvNoteAdapter rvAdapter;

    /**
     * Активная заметка
     */
    private Note snote;


    /**
     * Конструктор
     *
     * @param adapter Адаптер
     */
    public RvViewHolder(View view, RvNoteAdapter adapter) {
        super(view);
        rvAdapter = adapter;
        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        date = (TextView) view.findViewById(R.id.date);
        checkbox = (CheckBox) view.findViewById(R.id.checkBox);

        sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
        btEdit = itemView.findViewById(R.id.btEdit);
        btDelete = itemView.findViewById(R.id.btDelete);
        smCv = itemView.findViewById(R.id.smContentView);

        smCv.setOnClickListener(this);
        smCv.setOnLongClickListener(this);
        checkbox.setOnCheckedChangeListener(this);
        bindSwipeMenu();
    }

    /**
     * Прявязка swipe-меню
     */
    protected void bindSwipeMenu(){

        smCv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sml.setSwipeEnable(!rvAdapter.getSelectableStatus());
                return false;
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothCloseMenu(1000);
                rvAdapter.onClickEdit(NoteRepository.get(getAdapterPosition()), getAdapterPosition());
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothCloseMenu(1000);
                rvAdapter.onClickDelete(NoteRepository.get(getAdapterPosition()), getAdapterPosition());
            }
        });
    }

    /**
     * Обновление Layout, элемента View
     */
    protected void updateLayout() {

        if (description.getText().toString().isEmpty())
            description.setVisibility(View.GONE);
        else
            description.setVisibility(View.VISIBLE);
        if (title.getText().toString().isEmpty()) title.setVisibility(View.GONE);
        else
            title.setVisibility(View.VISIBLE);
    }

    /**
     * Событие, вызываемое при клике на элементе
     */
    @Override
    public void onClick(View v) {
        if (rvAdapter.selectableStatus)
            checkbox.setChecked(!checkbox.isChecked());
        else
            rvAdapter.onItemClick(NoteRepository.get(getAdapterPosition()), getAdapterPosition());
    }

    /**
     * Событие, вызываемое при долгом клике на элементе
     */
    @Override
    public boolean onLongClick(View v) {
        checkbox.setChecked(!checkbox.isChecked());
        return true;
    }

    /**
     * Событие, вызываемое при именении значения чекбокса
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        snote = NoteRepository.get(getAdapterPosition());
        setChecked(isChecked);
        rvAdapter.onItemSelected(NoteRepository.getSelectedNotes(), snote);
    }

    /**
     * Установка выделения для заметки и изменение статуса выделения
     *
     * @param value Значения выделения
     */
    public void setChecked(boolean value) {
        snote.setSelected(value);
        checkbox.setVisibility(value ? View.VISIBLE : View.GONE);
        if (NoteRepository.getSelectedNotes().size() > 0 && !rvAdapter.selectableStatus)
            rvAdapter.onChangeSelectionMode(true);
        else if (NoteRepository.getSelectedNotes().size() == 0 && rvAdapter.selectableStatus)
            rvAdapter.onChangeSelectionMode(false);
    }

}
