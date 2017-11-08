package xyz.danshin.smartnotes.interfaces;

import java.util.ArrayList;

import xyz.danshin.smartnotes.entity.Note;

/**
 * Интерфейс для обработки выделения и кликов на элементах в recyclerView
 */
public interface IRvSelectedListener {
    /**
     * Метод вызывается при клике на элементе в recyclerView
     * @param clickedItem Элемент по которому кликнули
     * @param adapterPosition Позиция адаптера
     */
    void onItemClick(Note clickedItem, int adapterPosition);

    /**
     * Метод вызывается при смене выделения элемента в recyclerView
     * @param selectedItems Список выделенных элементов
     * @param selectedItem Последний элемент на котором сработало событие
     */
    void onItemSelected(ArrayList<Note> selectedItems, Note selectedItem);

    /**
     * Метод вызывается при смене режима выделения
     * @param status Статус режима выделения
     */
    void onChangeSelectionMode(boolean status);
}
