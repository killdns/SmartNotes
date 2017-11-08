package xyz.danshin.smartnotes.interfaces;

import android.view.View;

/**
 * Интерфейс для обработки кликов на элементах в recyclerView
 */
public interface IRvPriorityClickListener {
    /**
     * Метод вызывается при клике на элементе в recyclerView
     * @param v Экземпляр View
     * @param index Индекс приоритета
     */
    void onPriorityItemClick(View v, int index);
}
