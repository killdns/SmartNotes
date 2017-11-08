package xyz.danshin.smartnotes.ui.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.interfaces.IRvPriorityClickListener;

/**
 * ViewHolder для recyclerView в диалоговом меню с приоритетами заметок
 */
public class RvPriorityDMViewHolder extends RecyclerView.ViewHolder {

    /**
     * Карточка с контентом
     */
    public CardView cardView;
    /**
     * Поле предварительного оформления текста
     */
    public TextView preview;

    /**
     * Поле описания типа оформления
     */
    public TextView description;

    /**
     * Объект, реализующий интерфейс IRvPriorityClickListener
     */
    private IRvPriorityClickListener priorityClickListener;

    /**
     * OnClickListener для элемета приоритета
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            priorityClickListener.onPriorityItemClick(v, getAdapterPosition());
        }
    };

    /**
     * Конструктор
     */
    public RvPriorityDMViewHolder(View view, IRvPriorityClickListener priorityClickListener) {
        super(view);
        cardView = (CardView)  view.findViewById(R.id.dm_cv_priority_item);
        preview = (TextView) view.findViewById(R.id.dm_priority_item_preview);
        description = (TextView) view.findViewById(R.id.dm_priority_item_description);
        this.priorityClickListener = priorityClickListener;
        cardView.setOnClickListener(onClickListener);
    }
}
