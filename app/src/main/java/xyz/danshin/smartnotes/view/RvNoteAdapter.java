package xyz.danshin.smartnotes.view;

/**
 * Created by Кирилл Даньшин on 13.10.2017.
 */

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;


import java.util.ArrayList;
import java.util.List;

import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.controller.ActivityController;
import xyz.danshin.smartnotes.entity.Note;
import xyz.danshin.smartnotes.interfaces.IRvSwipeMenuListener;
import xyz.danshin.smartnotes.repository.NoteRepository;
import xyz.danshin.smartnotes.interfaces.IRvSelectedListener;

/**
 * Адаптер заметок для recyclerView
 */
public class RvNoteAdapter extends RecyclerView.Adapter<RvViewHolder> implements IRvSelectedListener, IRvSwipeMenuListener {


    /**
     * Экземпляр обекта, поддерживающий интерфейс IRvSelectedListener
     */
    private IRvSelectedListener listener;


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
     * Пролучение количества заметок количество заметок
     *
     * @return Количество заметок
     */
    @Override
    public int getItemCount() {
        return NoteRepository.size();
    }

    /**
     * Метод добавления новой заметки в recyclerView
     *
     * @param note Экземпляр заметки
     */
    public void AddItem(Note note) {
        NoteRepository.add(note);
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
     * Удаление заметки по ее Id и позиции адаптера
     *
     * @param id              Id заметки
     * @param adapterPosition Позиция адаптера
     */
    public void removeNoteById(int id, int adapterPosition) {
        NoteRepository.removeById(id);
        notifyItemRemoved(adapterPosition);
    }

    /**
     * Очистка выделения
     */
    public void clearSelection() {
        NoteRepository.clearSelection();
        onChangeSelectionMode(false);
    }
}