package xyz.danshin.smartnotes.ui.adapters;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;


import java.util.ArrayList;
import java.util.Map;

import xyz.danshin.smartnotes.Enums;
import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.controller.ActivityController;
import xyz.danshin.smartnotes.entity.Note;
import xyz.danshin.smartnotes.interfaces.IRvSwipeMenuListener;
import xyz.danshin.smartnotes.repository.NotePriorityRepository;
import xyz.danshin.smartnotes.repository.NoteRepository;
import xyz.danshin.smartnotes.interfaces.IRvSelectedListener;
import xyz.danshin.smartnotes.ui.commons.NotePriority;
import xyz.danshin.smartnotes.ui.viewholders.RvViewHolder;

/**
 * Адаптер заметок для recyclerView
 */
public class RvNoteAdapter extends RecyclerView.Adapter<RvViewHolder> implements IRvSelectedListener, IRvSwipeMenuListener {


    /**
     * Экземпляр обекта, поддерживающий интерфейс IRvSelectedListener
     */
    private IRvSelectedListener listener;


    private Map<Enums.NotePriority, NotePriority> priorities = NotePriorityRepository.getInstance().getPrioritiesMap();

    /**
     * Статус выделения заметок
     */
    protected boolean selectableStatus;

    /**
     * Геттер для статуса выделения
     * @return
     */
    public boolean getSelectableStatus() {
        return selectableStatus;
    }

    /**
     * Конструктор
     *
     * @param listener Экземпляр класса, реализующий интерфейс IRvSelectedListener
     * @see Note
     */
    public RvNoteAdapter(IRvSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * Метод, вызываемый при создании ViewHolder'a
     */
    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new RvViewHolder(view1, this);
    }


    /**
     * Метод, вызываемый при привязке ViewHolder'a
     */
    @Override
    public void onBindViewHolder(RvViewHolder viewholder, int i) {
        viewholder.checkbox.setVisibility(selectableStatus && NoteRepository.get(i).isSelected() ? View.VISIBLE : View.GONE);
        viewholder.title.setText(NoteRepository.get(i).getTitle());
        viewholder.description.setText(NoteRepository.get(i).getDescription());
        viewholder.date.setText(NoteRepository.get(i).getFormatedLastModifiedDate());
        viewholder.checkbox.setChecked(NoteRepository.get(i).isSelected());

        viewholder.smCv.setBackground(new ColorDrawable(priorities.get(NoteRepository.get(i).getPriority()).getTextColorBackground()));
        viewholder.title.setTextColor(priorities.get(NoteRepository.get(i).getPriority()).getTextColor());
        viewholder.description.setTextColor(priorities.get(NoteRepository.get(i).getPriority()).getTextColor());
        viewholder.date.setTextColor(NoteRepository.get(i).getPriority() == Enums.NotePriority.DEFAULT ?
                ColorUtils.setAlphaComponent(priorities.get(NoteRepository.get(i).getPriority()).getTextColor(), 127) :
                priorities.get(NoteRepository.get(i).getPriority()).getTextColor());

        viewholder.updateLayout();
    }

    /**
     * Метод, необходимый для отображения элементов без дублирования
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Метод, вызываемый при изменении состояния выделения элемента в recyclerView
     *
     * @param selectedItems Список выделенных элементов
     * @param selectedItem  Последний элемент на котором сработало событие
     */
    @Override
    public void onItemSelected(ArrayList<Note> selectedItems, Note selectedItem) {
        listener.onItemSelected(selectedItems, selectedItem);
    }

    /**
     * Метод, вызываемый при клике на элементе в recyclerView
     *
     * @param clickedItem     Элемент по которому кликнули
     * @param adapterPosition Позиция адаптера
     */
    @Override
    public void onItemClick(Note clickedItem, int adapterPosition) {
        listener.onItemClick(clickedItem, adapterPosition);
    }

    /**
     * Метод, вызываемый при клике на элементе "Edit" в recyclerView
     *
     * @param clickedItem     Элемент по которому кликнули
     * @param adapterPosition Позиция адаптера
     */
    @Override
    public void onClickEdit(Note clickedItem, int adapterPosition) {
        ActivityController.startNoteActivityEdit(clickedItem.getId(), adapterPosition);
    }

    /**
     * Метод, вызываемый при клике на элементе "Delete" в recyclerView
     *
     * @param clickedItem     Элемент по которому кликнули
     * @param adapterPosition Позиция адаптера
     */
    @Override
    public void onClickDelete(Note clickedItem, int adapterPosition) {
        NoteRepository.remove(clickedItem);
        notifyItemRemoved(adapterPosition);
    }


    /**
     * Метод, вызываемый при изменении режима выделения
     *
     * @param status Статус режима выделения
     */
    @Override
    public void onChangeSelectionMode(boolean status) {

        selectableStatus = status;
        notifyDataSetChanged();
        listener.onChangeSelectionMode(status);
    }

    /**
     * Пролучение количества заметок
     *
     * @return Количество заметок
     */
    @Override
    public int getItemCount() {
        return NoteRepository.size();
    }

    /**
     * Метод вставки в recyclerView существующей заметки
     *
     * @param  adapterPosition Позиция адаптера
     */
    public void insertItem(int adapterPosition) {
        notifyItemInserted(adapterPosition);
    }


    /**
     * Удаление всех выделенных заметок
     */
    public void removeSelectedNotes() {

        selectableStatus = false;
        listener.onChangeSelectionMode(selectableStatus);
        for (Note note:  NoteRepository.getSelectedNotes()) {
            notifyItemRemoved(NoteRepository.getCollection().indexOf(note));
            NoteRepository.remove(note);
        }
    }

    /**
     * Очистка выделения
     */
    public void clearSelection() {
        NoteRepository.clearSelection();
        onChangeSelectionMode(false);
    }
}