package xyz.danshin.smartnotes.interfaces;



import xyz.danshin.smartnotes.entity.Note;

/**
 * Интерфейс для обработки кликов на элементах swipe-меню в recyclerView
 */
public interface IRvSwipeMenuListener {

    /**
     * Метод вызывается при клике на элементе "Edit"
     * @param clickedItem Элемент по которому кликнули
     * @param adapterPosition Позиция адаптера
     */
    void onClickEdit(Note clickedItem, int adapterPosition);

    /**
     * Метод вызывается при клике на элементе "Delete"
     * @param clickedItem Элемент по которому кликнули
     * @param adapterPosition Позиция адаптера
     */
    void onClickDelete(Note clickedItem, int adapterPosition);
}
